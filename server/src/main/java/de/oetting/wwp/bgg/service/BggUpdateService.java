package de.oetting.wwp.bgg.service;

import com.github.marcioos.bggclient.BGG;
import com.github.marcioos.bggclient.common.ThingType;
import com.github.marcioos.bggclient.fetch.FetchException;
import com.github.marcioos.bggclient.fetch.domain.FetchItem;
import com.github.marcioos.bggclient.fetch.domain.Poll;
import com.github.marcioos.bggclient.search.SearchException;
import com.github.marcioos.bggclient.search.domain.SearchItem;
import com.github.marcioos.bggclient.search.domain.SearchOutput;
import de.oetting.wwp.game.entity.Game;
import de.oetting.wwp.game.repository.GameRepository;
import de.oetting.wwp.tags.entity.TagEntity;
import de.oetting.wwp.tags.entity.TagType;
import de.oetting.wwp.tags.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class BggUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(BggUpdateService.class);

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    public void updateAsynchronous() {
        // TODO: Not the best way to start an asynchronous job, but we avoid having to use spring schedulers for now
        Thread thread = new Thread(this::updateFromBgg);
        thread.setDaemon(true);
        thread.start();
    }

    public void updateFromBgg() {
        var globalTags = tagRepository.findByType(TagType.GLOBAL);
        gameRepository.findAll().forEach(game -> updateGameInOwnTransaction(game, globalTags));
    }

    public Optional<Game> importFromBgg(int bggId) throws FetchException {
        Collection<FetchItem> fetchItems = BGG.fetch(List.of(bggId), ThingType.BOARDGAME_EXPANSION, ThingType.BOARDGAME);
        if (fetchItems.size() == 0) {
            LOG.error("Fetch from BGG did not find anything for {}", bggId);
            return Optional.empty();
        }

        FetchItem firstItem = fetchItems.stream().findFirst().orElseThrow();
        var globalTags = tagRepository.findByType(TagType.GLOBAL);

        Game game = new Game();
        game.setName(firstItem.getName());
        Game writtenGame = updateGameFromBgg(game, firstItem, globalTags);
        return Optional.of(writtenGame);
    }

    private List<TagEntity> findMatchingTags(FetchItem fetchItem, List<TagEntity> tags) {
        return tags.stream()
                .filter(tag -> isMatchingTag(fetchItem, tag))
                .toList();
    }

    private boolean isMatchingTag(FetchItem fetchItem, TagEntity tag) {
        return fetchItem.getCategories().contains(tag.getImportedSourceName()) ||
                fetchItem.getMechanics().contains(tag.getImportedSourceName());
    }

    private void updateGameInOwnTransaction(Game game, List<TagEntity> globalTags) {
        new TransactionTemplate(transactionManager).executeWithoutResult((__) -> {
            try {
                updateGame(game, globalTags);
                sleepRandom(2000);
            } catch (SearchException e) {
                LOG.warn("Could not update game {}: {}", game.getName(), e.getMessage());
            }
        });
    }

    public Optional<Game> updateGame(Game game) throws SearchException {
        var globalTags = tagRepository.findByType(TagType.GLOBAL);
        return updateGame(game, globalTags);
    }

    private Optional<Game> updateGame(Game game, List<TagEntity> globalTags) throws SearchException {
        LOG.info("Updating game {}", game.getName());
        Optional<SearchItem> optionalItem = findMatchingSearchItem(game);
        return optionalItem.flatMap(item -> {
            sleepRandom(100);
            return updateGame(game, item, globalTags);
        });
    }

    private Optional<Game> updateGame(Game game, SearchItem item, List<TagEntity> globalTags) {
        try {
            Collection<FetchItem> fetchItems = BGG.fetch(List.of(item.getId()), ThingType.BOARDGAME_EXPANSION, ThingType.BOARDGAME);
            if (fetchItems.size() == 0) {
                LOG.error("Fetch from BGG did not find anything for {}", game.getName());
                return Optional.empty();
            }
            FetchItem firstItem = fetchItems.stream().findFirst().orElseThrow();
            try {
                return Optional.of(updateGameFromBgg(game, firstItem, globalTags));
            } catch (Exception e){
                LOG.error("Could not update item", e);
            }
        } catch (FetchException e) {
            LOG.warn("Could not update game during fetch {}: {}", game.getName(), e.getMessage());
        }
        return Optional.empty();
    }

    private Optional<SearchItem> findMatchingSearchItem(Game game) throws SearchException {
        SearchOutput searchOutput = BGG.search(game.getName(), ThingType.BOARDGAME, ThingType.BOARDGAME_EXPANSION);
        if (searchOutput.getItems() ==null) {
            LOG.info("Did not find matching game game {}", game.getName());
            return Optional.empty();
        }
        if (searchOutput.getItems().size() == 1) {
            return Optional.of( searchOutput.getItems().get(0));
        }
        return searchOutput.getItems().stream()
                .filter(item -> item.getName().getValue().equals(game.getName()))
                .findAny();
    }

    private Game updateGameFromBgg(Game game, FetchItem fetchItem, List<TagEntity> globalTags) {
        game.setUrl(String.format("https://boardgamegeek.com/boardgame/%s", fetchItem.getId()));
        game.setMaxPlayers(Integer.parseInt(fetchItem.getMaxPlayers().getValue()));
        game.setMinPlayers(Integer.parseInt(fetchItem.getMinPlayers().getValue()));
        game.setDescription(fetchItem.getDescription());
        game.setImageUrl(fetchItem.getImageUrl());
        game.setThumbnailUrl(fetchItem.getThumbnailUrl());
        game.setPlayingTimeMinutes(Integer.parseInt(fetchItem.getPlayingTime().getValue()));
        game.setBestNumberOfPlayers(getBestNumberOfPlayers(fetchItem));
        game.setRecommendedNumberOfPlayers(getRecommededNumberOfPlayers(fetchItem));
        findMatchingTags(fetchItem, globalTags)
                .forEach(game::addGlobalTag);
        return gameRepository.save(game);
    }

    private Set<Integer> getRecommededNumberOfPlayers(FetchItem fetchItem) {
        Optional<Poll> numberPlayersPoll = fetchItem.getPolls().stream()
                .filter(Poll::isSuggestedNumPlayersPoll)
                .findAny();
        return numberPlayersPoll.map(poll -> poll.getResultsList().stream()
                .filter(BggUpdateService::isPlayerNumberRecommended)
                .map(results -> Integer.parseInt(results.getNumPlayers()))
                .collect(Collectors.toSet())).orElse(Collections.emptySet());
    }

    private Set<Integer> getBestNumberOfPlayers(FetchItem fetchItem) {
        Optional<Poll> numberPlayersPoll = fetchItem.getPolls().stream()
                .filter(Poll::isSuggestedNumPlayersPoll)
                .findAny();
        return numberPlayersPoll.map(poll -> poll.getResultsList().stream()
                .filter(BggUpdateService::isPlayerNumberBest)
                .map(results -> Integer.parseInt(results.getNumPlayers()))
                .collect(Collectors.toSet())).orElse(Collections.emptySet());
    }

    private static boolean isPlayerNumberRecommended(Poll.Results pollResults) {
        int numberOfBest = getNumberOfVotesForBest(pollResults);
        int numberOfRecommended = getNumberOfVotesForRecommended(pollResults);
        int numberOfNotRecommended = getNumberOfVotesForNotRecommended(pollResults);
        var sum = numberOfBest +  numberOfRecommended + numberOfNotRecommended;
        return numberOfBest * 2 < sum && (numberOfBest + numberOfRecommended) > numberOfNotRecommended;
    }

    private static boolean isPlayerNumberBest(Poll.Results pollResults) {
        int numberOfBest = getNumberOfVotesForBest(pollResults);
        int numberOfRecommended = getNumberOfVotesForRecommended(pollResults);
        int numberOfNotRecommended = getNumberOfVotesForNotRecommended(pollResults);
        var sum = numberOfBest +  numberOfRecommended + numberOfNotRecommended;
        return numberOfBest * 2 >= sum;
    }

    private static int getNumberOfVotesForBest(Poll.Results pollResults) {
        return getNumberOfVotesForOption(pollResults, "Best");
    }

    private static int getNumberOfVotesForRecommended(Poll.Results pollResults) {
        return getNumberOfVotesForOption(pollResults, "Recommended");
    }

    private static int getNumberOfVotesForNotRecommended(Poll.Results pollResults) {
        return getNumberOfVotesForOption(pollResults, "Not Recommended");
    }

    private static int getNumberOfVotesForOption(Poll.Results pollResults, String option) {
        return getNumberOfVotesForOption(pollResults, option);
    }

    private void sleepRandom(int maxMillis) {
        // Sleeping for some time during requests to not overload BGG servers
        long random = threadLocalRandom.nextInt(maxMillis);
        try {
            Thread.sleep(random);
        } catch (InterruptedException e) {
            // empty
        }

    }

}
