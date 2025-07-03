package de.oetting.wwp.bgg.service;

import com.github.marcioos.bggclient.BGG;
import com.github.marcioos.bggclient.common.ThingType;
import com.github.marcioos.bggclient.fetch.FetchException;
import com.github.marcioos.bggclient.fetch.domain.FetchItem;
import com.github.marcioos.bggclient.search.SearchException;
import com.github.marcioos.bggclient.search.domain.SearchItem;
import com.github.marcioos.bggclient.search.domain.SearchOutput;
import de.oetting.wwp.game.entity.Game;
import de.oetting.wwp.game.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BggUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(BggUpdateService.class);

    @Autowired
    private GameRepository gameRepository;

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
        gameRepository.findAll().forEach(game -> updateGame(game));
    }

    public Optional<Game> importFromBgg(int bggId) throws FetchException {
        Collection<FetchItem> fetchItems = BGG.fetch(List.of(bggId), ThingType.BOARDGAME_EXPANSION, ThingType.BOARDGAME);
        if (fetchItems.size() == 0) {
            LOG.error("Fetch from BGG did not find anything for {}", bggId);
            return Optional.empty();
        }

        FetchItem firstItem = fetchItems.stream().findFirst().orElseThrow();
        Game game = new Game();
        game.setName(firstItem.getName());
        Game writtenGame = updateGameFromBgg(game, firstItem);
        return Optional.of(writtenGame);
    }

    private void updateGame(Game game) {
        new TransactionTemplate(transactionManager).executeWithoutResult((__) -> {
            try {
                LOG.info("Updating game {}", game.getName());
                Optional<SearchItem> optionalItem = findMatchingSearchItem(game);
                optionalItem.ifPresent(item -> {
                    sleepRandom(500);
                    updateGame(game, item);
                });
                sleepRandom(2000);
            } catch (SearchException e) {
                LOG.warn("Could not update game {}: {}", game.getName(), e.getMessage());
            }
        });
    }

    private void updateGame(Game game, SearchItem item) {
        try {
            Collection<FetchItem> fetchItems = BGG.fetch(List.of(item.getId()), ThingType.BOARDGAME_EXPANSION, ThingType.BOARDGAME);
            if (fetchItems.size() == 0) {
                LOG.error("Fetch from BGG did not find anything for {}", game.getName());
                return;
            }
            FetchItem firstItem = fetchItems.stream().findFirst().orElseThrow();
            try {
                updateGameFromBgg(game, firstItem);
            } catch (Exception e){
                LOG.error("Could not update item", e);
            }
        } catch (FetchException e) {
            LOG.warn("Could not update game during fetch {}: {}", game.getName(), e.getMessage());
        }
    }

    private Optional<SearchItem> findMatchingSearchItem(Game game) throws SearchException {
        SearchOutput searchOutput = BGG.search(game.getName(), ThingType.BOARDGAME);
        if (searchOutput.getItems() ==null) {
            LOG.info("Did not find matching game game {}", game.getName());
            return Optional.empty();
        }
        if (searchOutput.getItems().size() == 1) {
            return Optional.of( searchOutput.getItems().get(0));
        }
        return searchOutput.getItems().stream().filter(item -> item.getName().getValue().equals(game.getName())).findAny();
    }

    private Game updateGameFromBgg(Game game, FetchItem fetchItem) {
        game.setUrl(String.format("https://boardgamegeek.com/boardgame/%s", fetchItem.getId()));
        game.setMaxPlayers(Integer.parseInt(fetchItem.getMaxPlayers().getValue()));
        game.setMinPlayers(Integer.parseInt(fetchItem.getMinPlayers().getValue()));
        game.setDescription(fetchItem.getDescription());
        game.setImageUrl(fetchItem.getImageUrl());
        game.setThumbnailUrl(fetchItem.getThumbnailUrl());
        game.setPlayingTimeMinutes(Integer.parseInt(fetchItem.getPlayingTime().getValue()));
        return gameRepository.save(game);
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
