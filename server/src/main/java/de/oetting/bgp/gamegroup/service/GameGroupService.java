package de.oetting.bgp.gamegroup.service;

import de.oetting.bgp.entities.Player;
import de.oetting.bgp.exceptions.ConflictException;
import de.oetting.bgp.exceptions.ForbiddenException;
import de.oetting.bgp.exceptions.UnprocessableEntityException;
import de.oetting.bgp.game.repository.GameRepository;
import de.oetting.bgp.gamegroup.model.CreateGameGroupRequest;
import de.oetting.bgp.gamegroup.persistence.Game2GameGroupRelation;
import de.oetting.bgp.gamegroup.persistence.Game2GameGroupRepository;
import de.oetting.bgp.gamegroup.persistence.GameGroupEntity;
import de.oetting.bgp.gamegroup.persistence.GameGroupMemberType;
import de.oetting.bgp.gamegroup.persistence.GameGroupMembership;
import de.oetting.bgp.gamegroup.persistence.GameGroupRepository;
import de.oetting.bgp.gamegroup.persistence.GameGroupType;
import de.oetting.bgp.infrastructure.CurrentUser;
import de.oetting.bgp.player.persistence.PlayerRepository;
import de.oetting.bgp.repositories.RatingRepository;
import de.oetting.bgp.service.events.GameGroupEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GameGroupService {

    @Autowired
    private GameGroupRepository gameGroupRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameGroupEventService gameGroupEventService;

    @Autowired
    private Game2GameGroupRepository game2GameGroupRepository;

    @Autowired
    private RatingRepository ratingRepository;

    public GameGroupEntity createGameGroup(CreateGameGroupRequest request) {
        if (gameGroupRepository.existsByName(request.getName())) {
            throw new ConflictException("Group already exists");
        }
        GameGroupEntity groupEntity = new GameGroupEntity();
        groupEntity.setName(request.getName());
        groupEntity.setType(request.getType());
        groupEntity.setOpenForNewPlayers(request.isOpenForNewPlayers());
        return gameGroupRepository.save(groupEntity);
    }

    public void addPlayerById(long playerId, long gameGroupId) {
        if (gameGroupRepository.playerAssignedToGameGroup(playerId, gameGroupId).isPresent()) {
            throw new ConflictException("Player already exists");
        }
        Player player = playerRepository.findById(playerId).orElseThrow();
        GameGroupEntity gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();
        if (!gameGroup.isOpenForNewPlayers()) {
            throw new ConflictException("Group is not open for new players");
        }
        if (gameGroup.getType() == GameGroupType.PERSONAL) {
            throw new ConflictException("Cannot join personal group");
        }
        var membership = new GameGroupMembership(playerId, gameGroupId);
        gameGroup.addPlayer(membership);

        gameGroupEventService.playerAdded(gameGroupId, player);
    }

    public void removePlayerById(long playerId, long gameGroupId) {
        checkUserIsAdminOrOwnerInGroupOrGlobalAdmin(gameGroupId);
        Player player = playerRepository.findById(playerId).orElseThrow();
        GameGroupEntity gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();

        if (gameGroup.getType() == GameGroupType.PERSONAL) {
            throw new ConflictException("Cannot remove players in personal group");
        }
        var membership = gameGroup.getPlayers().stream().filter(m -> m.getPlayer().getId() == playerId).findFirst();
        if (membership.map(m -> m.getType() == GameGroupMemberType.OWNER).orElse(false)) {
            throw new ForbiddenException("Owner cannot be removed");
        }
        if (membership.isEmpty()) {
            return;
        }

        gameGroup.deletePlayer(membership.get());

        gameGroupEventService.playerRemoved(gameGroupId, player);
    }

    public void addPlayedGameById(long gameId, long gameGroupId) {
        var game = gameRepository.findById(gameId).orElseThrow();
        var gameGroup = gameGroupRepository.findById(gameGroupId).orElseThrow();
        var relation = new Game2GameGroupRelation(gameId, gameGroupId);
        relation.setGameGroup(gameGroup);
        relation.setGame(game);
        relation.setAddedToGroupTime(LocalDateTime.now());
        var savedRelation = game2GameGroupRepository.save(relation);
        gameGroup.addGame(savedRelation);

        gameGroupEventService.gameAdded(gameGroupId, game);
    }

    public void checkUserIsPartOfGroup(long gameGroupId) {
        var myPlayer = findMyPlayer();
        if (gameGroupRepository.playerAssignedToGameGroup(myPlayer.getId(), gameGroupId).isEmpty()) {
            throw new UnprocessableEntityException("Only allowed if you are member of group");
        }
    }

    public void checkUserIsAdminOrOwnerInGroupOrGlobalAdmin(long gameGroupId) {
        if (!CurrentUser.checkIfUserIsGlobalAdmin() && !isUserAdminOrOwnerInGroup(gameGroupId)) {
            throw new ForbiddenException("Only allowed if you are admin or owner of group");
        }
    }

    public void checkUserIsOwnerInGroupOrGlobalAdmin(long gameGroupId) {
        if (!CurrentUser.checkIfUserIsGlobalAdmin() && !isUserOwnerInGroup(gameGroupId)) {
            throw new ForbiddenException("Only allowed if you are owner of group");
        }
    }

    private boolean isUserAdminOrOwnerInGroup(long gameGroupId) {
        var myPlayer = findMyPlayer();
        var membership = gameGroupRepository.playerAssignedToGameGroup(myPlayer.getId(), gameGroupId);
        return membership.map(m -> m.getType() == GameGroupMemberType.ADMIN || m.getType() == GameGroupMemberType.OWNER).orElse(false);
    }

    private boolean isUserOwnerInGroup(long gameGroupId) {
        var myPlayer = findMyPlayer();
        var membership = gameGroupRepository.playerAssignedToGameGroup(myPlayer.getId(), gameGroupId);
        return membership.map(m -> m.getType() == GameGroupMemberType.OWNER).orElse(false);
    }

    public Optional<GameGroupEntity> find(long gameGroupId) {
        return gameGroupRepository.findById(gameGroupId);
    }

    public void deleteGameGroup(long gameGroupId) {
        game2GameGroupRepository.deleteByGameGroupId(gameGroupId);
        ratingRepository.deleteByGameGroupId(gameGroupId);
        gameGroupRepository.deleteById(gameGroupId);
    }

    private Player findMyPlayer() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return playerRepository.findByName(username).orElseThrow(() -> new NoSuchElementException("Player not found"));
    }

    public GameGroupEntity createPersonalCollection() {
        var myPlayer = findMyPlayer();
        var newPersonalCollection = new GameGroupEntity();
        newPersonalCollection.setType(GameGroupType.PERSONAL);
        newPersonalCollection.setName(String.format("%s's personal collection", myPlayer.getName()));
        newPersonalCollection.setOpenForNewPlayers(false);
        if (gameGroupRepository.existsByName(newPersonalCollection.getName())) {
            throw new ConflictException("Group already exists");
        }
        var storedPersonalCollection = gameGroupRepository.save(newPersonalCollection);
        var membership = new GameGroupMembership(myPlayer.getId(), storedPersonalCollection.getId());
        storedPersonalCollection.setPlayers(Collections.singleton(membership));
        return storedPersonalCollection;
    }

}
