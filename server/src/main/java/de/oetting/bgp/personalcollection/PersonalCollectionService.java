package de.oetting.bgp.personalcollection;

import de.oetting.bgp.exceptions.ConflictException;
import de.oetting.bgp.gamegroup.persistence.GameGroupEntity;
import de.oetting.bgp.gamegroup.service.GameGroupService;
import de.oetting.bgp.infrastructure.CurrentUser;
import de.oetting.bgp.player.persistence.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalCollectionService {


    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameGroupService gameGroupService;

    public GameGroupEntity createPersonalCollection() {
        var myPlayer = playerRepository.findById(CurrentUser.getCurrentPlayerId()).orElseThrow();
        if (myPlayer.getPersonalCollection() != null) {
            throw new ConflictException("Player already has a personal collection");
        }
        var personalCollection = gameGroupService.createPersonalCollection();
        myPlayer.setPersonalCollection(personalCollection);
        return personalCollection;
    }

}
