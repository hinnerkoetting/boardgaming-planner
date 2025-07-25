package de.oetting.bgp.bgg.controller;


import de.oetting.bgp.bgg.service.BggUpdateService;
import de.oetting.bgp.exceptions.BadRequestException;
import de.oetting.bgp.exceptions.ConflictException;
import de.oetting.bgp.game.entity.Game;
import de.oetting.bgp.game.repository.GameRepository;
import de.oetting.bgp.security.Role;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.audux.bgg.BggClient;
import org.audux.bgg.common.ThingType;
import org.audux.bgg.response.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "api/bgg")
public class BGGController {

    private LocalDateTime lastBggUpdateRun;

    @Autowired
    private BggUpdateService bggUpdateService;

    @Autowired
    private GameRepository gameRepository;

    @GetMapping(path = "/search/{searchTerm}")
    public List<SearchResult> searchBgg(@PathVariable("searchTerm") String searchTerm) throws ExecutionException, InterruptedException {
        var searchOutput = BggClient.search(searchTerm, new ThingType[]{}).callAsync().get();
        if (searchOutput == null || searchOutput.getData() == null) {
            return Collections.emptyList();
        }
        return searchOutput.getData().getResults();
    }

    @PutMapping(path = "/sync/game/{gameId}")
    @Transactional
    public Game syncGameFromBgg(@PathVariable("gameId") long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();

        try {
            return bggUpdateService.updateGame(game)
                    .orElseThrow(() -> new ConflictException("Could not sync game"));
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @PostMapping(path = "/import/{id}")
    public Game importGameFromBgg(@PathVariable("id") int id) {
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


    @PostMapping(path = "/sync/collection/{name}")
    @Transactional
    public void importBggCollection(@PathVariable(name = "name") String name) {
        if (StringUtils.isEmpty(name)) {
            throw new NoSuchElementException("Name is empty");
        }
        bggUpdateService.syncBggCollection(name);
    }
}
