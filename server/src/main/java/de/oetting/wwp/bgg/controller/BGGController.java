package de.oetting.wwp.bgg.controller;

import com.github.marcioos.bggclient.BGG;
import com.github.marcioos.bggclient.common.ThingType;
import com.github.marcioos.bggclient.fetch.FetchException;
import com.github.marcioos.bggclient.fetch.domain.FetchItem;
import com.github.marcioos.bggclient.search.SearchException;
import com.github.marcioos.bggclient.search.domain.SearchItem;
import com.github.marcioos.bggclient.search.domain.SearchOutput;
import de.oetting.wwp.bgg.service.BggUpdateService;
import de.oetting.wwp.exceptions.BadRequestException;
import de.oetting.wwp.exceptions.ConflictException;
import de.oetting.wwp.game.entity.Game;
import de.oetting.wwp.game.repository.GameRepository;
import de.oetting.wwp.security.Role;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping(path = "api/bgg")
public class BGGController {

    private LocalDateTime lastBggUpdateRun;

    @Autowired
    private BggUpdateService bggUpdateService;

    @Autowired
    private GameRepository gameRepository;

    @GetMapping(path = "/search/{searchTerm}")
    public List<SearchItem> searchBgg(@PathVariable("searchTerm") String searchTerm) throws SearchException {
        SearchOutput output = BGG.search(searchTerm, ThingType.BOARDGAME, ThingType.BOARDGAME_EXPANSION);
        if (output == null) {
            return Collections.emptyList();
        }
        return output.getItems();
    }

    @PutMapping(path = "/sync/game/{gameId}")
    @Transactional
    public Game syncGameFromBgg(@PathVariable("gameId") long gameId) throws FetchException {
        Game game = gameRepository.findById(gameId).orElseThrow();

        try {
            return bggUpdateService.updateGame(game)
                    .orElseThrow(() -> new ConflictException("Could not sync game"));
        } catch (SearchException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @PutMapping(path = "/import/{id}")
    public Game importGameFromBgg(@PathVariable("id") int id) throws FetchException {
        return bggUpdateService.importFromBgg(id).orElseThrow();
    }

    @PostMapping(path = "/sync")
    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateGamesFromBgg() {
        if (lastBggUpdateRun != null && lastBggUpdateRun.isAfter(LocalDateTime.now().minusDays(1))) {
            throw new BadRequestException("Cannot run updates right now. Please wait some time. We do not want to overload bgg servers");
        }
        lastBggUpdateRun = LocalDateTime.now();
        bggUpdateService.updateAsynchronous();
    }
}
