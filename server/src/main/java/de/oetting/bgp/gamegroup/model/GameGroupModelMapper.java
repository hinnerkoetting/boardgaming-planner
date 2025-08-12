package de.oetting.bgp.gamegroup.model;

import de.oetting.bgp.gamegroup.persistence.GameGroupEntity;
import de.oetting.bgp.gamegroup.persistence.GameGroupMembership;

public class GameGroupModelMapper {

    public static GameGroupModel map(GameGroupEntity savedEntity) {
        GameGroupModel model = new GameGroupModel();
        model.setId(savedEntity.getId());
        model.setName(savedEntity.getName());
        model.setType(savedEntity.getType());
        model.setOpenForNewPlayers(savedEntity.isOpenForNewPlayers());
        return model;
    }

    public static GameGroupMemberModel map(GameGroupMembership membership) {
        var playerModel = new GameGroupMemberModel();
        playerModel.setId(membership.getPlayer().getId());
        playerModel.setName(membership.getPlayer().getName());
        playerModel.setType(membership.getType());
        return playerModel;
    }

}
