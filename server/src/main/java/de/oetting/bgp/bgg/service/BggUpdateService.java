package de.oetting.bgp.bgg.service;

import de.oetting.bgp.entities.Player;
import de.oetting.bgp.exceptions.ConflictException;
import de.oetting.bgp.game.entity.Game;
import de.oetting.bgp.game.repository.GameRepository;
import de.oetting.bgp.gamegroup.persistence.Game2GameGroupId;
import de.oetting.bgp.gamegroup.persistence.Game2GameGroupRelation;
import de.oetting.bgp.gamegroup.persistence.Game2GameGroupRepository;
import de.oetting.bgp.gamegroup.persistence.GameGroupEntity;
import de.oetting.bgp.player.persistence.PlayerRepository;
import de.oetting.bgp.rating.RatingRequest;
import de.oetting.bgp.rating.controller.RatingService;
import de.oetting.bgp.tags.entity.TagEntity;
import de.oetting.bgp.tags.entity.TagType;
import de.oetting.bgp.tags.repository.TagRepository;
import org.audux.bgg.BggClient;
import org.audux.bgg.common.ThingType;
import org.audux.bgg.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.Thread;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
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

    @Autowired
    private Game2GameGroupRepository game2GameGroupRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RatingService ratingService;

    private final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

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

    public Optional<Game> importFromBgg(int bggId) {
        try {
            Response<Things> things = BggClient.things(new Integer[]{bggId}, new ThingType[]{}, false, false, false, false, false).callAsync().get();
            if (things.isError() || things.getData() == null || things.getData().getThings().isEmpty()) {
                LOG.error("Fetch from BGG did not find anything for {}", bggId);
                return Optional.empty();
            }

            var firstItem = things.getData().getThings().getFirst();
            var globalTags = tagRepository.findByType(TagType.GLOBAL);

            if (gameRepository.findByName(firstItem.getName()).isPresent()) {
                throw new ConflictException("Game already exists");
            }
            Game game = new Game();
            game.setName(firstItem.getName());
            Game writtenGame = updateGameFromBgg(game, firstItem, globalTags);
            return Optional.of(writtenGame);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void syncBggCollection(String ownerName) {
        try {
            Response<Collection> userCollection = BggClient.collection(ownerName).callAsync().get();
            if (userCollection.getData() == null) {
                throw new NoSuchElementException("User not found");
            }
            var personalCollection = findMyPlayer().getPersonalCollection();
            userCollection.getData().getItems().forEach(item -> {
                sleepRandom(100);
                var game = loadOrImportGame(item);
                if (game2GameGroupRepository.existsById(new Game2GameGroupId(game.getId(), personalCollection.getId()))) {
                    game2GameGroupRepository.save(new Game2GameGroupRelation(game.getId(), personalCollection.getId()));
                }
                String ownRating = item.getStats() != null ? item.getStats().getRatings().getValue() : null;
                if (ownRating != null && !"N/A".equals(ownRating)) {
                    updateRating(ownRating, game, personalCollection);
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateRating(String ownRating, Game game, GameGroupEntity gameGroup) {
        try {
            var request = new RatingRequest();
            request.setRating(Integer.parseInt(ownRating));
            request.setGameId(game.getId());
            request.setGameGroupId(gameGroup.getId());
            request.setPlayerId(findMyPlayer().getId());
            ratingService.updateRating(request);
        } catch (Exception e) {
            LOG.error("Could not update rating {} for game {}", ownRating, game.getName());
        }
    }

    private Player findMyPlayer() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playerRepository.findByName(username).orElseThrow(() -> new NoSuchElementException("Player not found"));
    }

    private Game loadOrImportGame(CollectionItem item) {
        var optionalGame = gameRepository.findByName(item.getName());
        return optionalGame.orElseGet(() -> importFromBgg(item.getObjectId())
                .orElseThrow(() -> new IllegalStateException("Cannot find game from boardgamegeek although it exists in my collection. Id=" + item.getObjectId())));

    }

    private List<TagEntity> findMatchingTags(Thing fetchItem, List<TagEntity> tags) {
        return tags.stream()
                .filter(tag -> isMatchingTag(fetchItem, tag))
                .toList();
    }

    private boolean isMatchingTag(Thing fetchItem, TagEntity tag) {
        return fetchItem.getLinks().stream().anyMatch(link -> link.getValue().contains(tag.getImportedSourceName()));
    }

    private void updateGameInOwnTransaction(Game game, List<TagEntity> globalTags) {
        new TransactionTemplate(transactionManager).executeWithoutResult((__) -> {
            try {
                Game gameInNewTransaction = gameRepository.findById(game.getId()).orElseThrow();
                updateGame(gameInNewTransaction, globalTags);
                sleepRandom(2000);
            } catch (Exception e) {
                LOG.warn("Could not update game {}: {}", game.getName(), e.getMessage());
            }
        });
    }

    public Optional<Game> updateGame(Game game) {
        var globalTags = tagRepository.findByType(TagType.GLOBAL);
        return updateGame(game, globalTags);
    }

    private Optional<Game> updateGame(Game game, List<TagEntity> globalTags) {
        LOG.info("Updating game {}", game.getName());
        Optional<SearchResult> optionalItem = findMatchingSearchItem(game);
        return optionalItem.flatMap(item -> {
            sleepRandom(100);
            return updateGame(game, item, globalTags);
        });
    }

    private Optional<Game> updateGame(Game game, SearchResult item, List<TagEntity> globalTags) {
        try {
            Response<Things> things = BggClient.things(new Integer[]{item.getId()}, new ThingType[]{}, false, false, false, false, false).callAsync().get();
            if (things.isError() || things.getData() == null || things.getData().getThings().isEmpty()) {
                LOG.error("Fetch from BGG did not find anything for {}", item.getId());
                return Optional.empty();
            }
            Thing firstItem = things.getData().getThings().getFirst();
            try {
                return Optional.of(updateGameFromBgg(game, firstItem, globalTags));
            } catch (Exception e) {
                LOG.error("Could not update item", e);
            }
        } catch (Exception e) {
            LOG.warn("Could not update game during fetch {}: {}", game.getName(), e.getMessage());
        }
        return Optional.empty();
    }

    private Optional<SearchResult> findMatchingSearchItem(Game game) {
        try {
            var searchOutput = BggClient.search(game.getName(), new ThingType[]{}).callAsync().get();
            if (searchOutput == null || searchOutput.getData() == null) {
                LOG.info("Did not find matching game game {}", game.getName());
                return Optional.empty();
            }
            if (searchOutput.getData().getTotal() == 1) {
                return Optional.of(searchOutput.getData().getResults().getFirst());
            }
            return searchOutput.getData().getResults().stream()
                    .filter(item -> item.getName().getValue().equals(game.getName()))
                    .findAny();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Game updateGameFromBgg(Game game, Thing fetchItem, List<TagEntity> globalTags) {
        game.setUrl(String.format("https://boardgamegeek.com/boardgame/%s", fetchItem.getId()));
        game.setMaxPlayers(fetchItem.getMaxPlayers() == null ? null : (int) Math.round(fetchItem.getMaxPlayers()));
        game.setMinPlayers(fetchItem.getMinPlayers());
        game.setDescription(fetchItem.getDescription());
        game.setImageUrl(fetchItem.getImage());
        game.setThumbnailUrl(fetchItem.getThumbnail());
        game.setPlayingTimeMinutes(fetchItem.getPlayingTimeInMinutes());
        game.setBestNumberOfPlayers(getBestNumberOfPlayers(fetchItem));
        game.setRecommendedNumberOfPlayers(getRecommendedNumberOfPlayers(fetchItem));
        findMatchingTags(fetchItem, globalTags)
                .forEach(game::addGlobalTag);
        return gameRepository.save(game);
    }

    private Set<Integer> getRecommendedNumberOfPlayers(Thing fetchItem) {
        List<NumberOfPlayersResults> numberPlayersPoll = fetchItem.getPolls().stream()
                .filter(NumberOfPlayersPoll.class::isInstance)
                .map(NumberOfPlayersPoll.class::cast)
                .flatMap(poll -> poll.getResults().stream())
                .toList();

        return numberPlayersPoll.stream()
                .filter(BggUpdateService::isPlayerNumberRecommended)
                .map(BggUpdateService::parseNumberOfPlayers)
                .collect(Collectors.toSet());
    }

    private Set<Integer> getBestNumberOfPlayers(Thing fetchItem) {
        List<NumberOfPlayersResults> numberPlayersPoll = fetchItem.getPolls().stream()
                .filter(NumberOfPlayersPoll.class::isInstance)
                .map(NumberOfPlayersPoll.class::cast)
                .flatMap(poll -> poll.getResults().stream())
                .toList();

        return numberPlayersPoll.stream()
                .filter(BggUpdateService::isPlayerNumberBest)
                .map(BggUpdateService::parseNumberOfPlayers)
                .collect(Collectors.toSet());
    }

    private static int parseNumberOfPlayers(NumberOfPlayersResults results) {
        String numPlayers = results.getNumberOfPlayers();
        if (numPlayers.contains("+")) {
            return Integer.parseInt(numPlayers.substring(0, numPlayers.indexOf("+"))) + 1;
        }
        return Integer.parseInt(numPlayers);
    }

    private static boolean isPlayerNumberRecommended(NumberOfPlayersResults pollResults) {
        int numberOfBest = getNumberOfVotesForBest(pollResults);
        int numberOfRecommended = getNumberOfVotesForRecommended(pollResults);
        int numberOfNotRecommended = getNumberOfVotesForNotRecommended(pollResults);
        var sum = numberOfBest + numberOfRecommended + numberOfNotRecommended;
        return numberOfBest * 2 < sum && (numberOfBest + numberOfRecommended) > numberOfNotRecommended;
    }

    private static boolean isPlayerNumberBest(NumberOfPlayersResults pollResults) {
        int numberOfBest = getNumberOfVotesForBest(pollResults);
        int numberOfRecommended = getNumberOfVotesForRecommended(pollResults);
        int numberOfNotRecommended = getNumberOfVotesForNotRecommended(pollResults);
        var sum = numberOfBest + numberOfRecommended + numberOfNotRecommended;
        return numberOfBest * 2 >= sum;
    }

    private static int getNumberOfVotesForBest(NumberOfPlayersResults pollResults) {
        return getNumberOfVotesForOption(pollResults, "Best");
    }

    private static int getNumberOfVotesForRecommended(NumberOfPlayersResults pollResults) {
        return getNumberOfVotesForOption(pollResults, "Recommended");
    }

    private static int getNumberOfVotesForNotRecommended(NumberOfPlayersResults pollResults) {
        return getNumberOfVotesForOption(pollResults, "Not Recommended");
    }

    private static int getNumberOfVotesForOption(NumberOfPlayersResults pollResults, String option) {
        return pollResults.getResults().stream()
                .filter(result -> option.equals(result.getValue()))
                .map(PollResult::getNumberOfVotes)
                .findAny()
                .orElseThrow();
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
