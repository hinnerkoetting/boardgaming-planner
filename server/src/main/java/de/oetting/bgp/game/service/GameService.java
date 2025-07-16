package de.oetting.bgp.game.service;

import de.oetting.bgp.game.repository.GameRepository;
import de.oetting.bgp.gamegroup.persistence.Game2GameGroupRepository;
import de.oetting.bgp.repositories.RatingRepository;
import de.oetting.bgp.tags.repository.GameGroupTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private Game2GameGroupRepository game2GameGroupRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private GameGroupTagRepository gameGroupTagRepository;

    public void deleteByGameId(long gameId) {
        ratingRepository.deleteByGameId(gameId);
        game2GameGroupRepository.deleteByGameId(gameId);
        gameGroupTagRepository.deleteByGameId(gameId);
        gameRepository.deleteById(gameId);
    }
}
