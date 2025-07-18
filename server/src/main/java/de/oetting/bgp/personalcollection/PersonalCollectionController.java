package de.oetting.bgp.personalcollection;

import de.oetting.bgp.gamegroup.model.GameGroupModel;
import de.oetting.bgp.gamegroup.model.GameGroupModelMapper;
import de.oetting.bgp.infrastructure.CurrentUser;
import de.oetting.bgp.player.persistence.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/players/{playerId}/personalCollection")
public class PersonalCollectionController {

    @Autowired
    private PersonalCollectionService personalCollectionService;

    @Autowired
    private PlayerRepository playerRepository;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional
    public GameGroupModel createPersonalCollection() {
        var newPersonalCollection = personalCollectionService.createPersonalCollection();
        return GameGroupModelMapper.map(newPersonalCollection);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public GameGroupModel getPersonalCollection() {
        long currentPlayerId = CurrentUser.getCurrentPlayerId();
        var myPersonalCollection = playerRepository.findById(currentPlayerId).orElseThrow().getPersonalCollection();
        if (myPersonalCollection == null) {
            throw new NoSuchElementException("Player has no personal collection");
        }
        return GameGroupModelMapper.map(myPersonalCollection);
    }

}
