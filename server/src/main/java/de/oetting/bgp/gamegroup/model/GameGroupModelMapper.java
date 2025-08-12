package de.oetting.bgp.gamegroup.model;

import de.oetting.bgp.gamegroup.persistence.GameGroupEntity;

public class GameGroupModelMapper {

    public static GameGroupModel map(GameGroupEntity savedEntity) {
        GameGroupModel model = new GameGroupModel();
        model.setId(savedEntity.getId());
        model.setName(savedEntity.getName());
        model.setType(savedEntity.getType());
        model.setOpenForNewPlayers(savedEntity.isOpenForNewPlayers());
        return model;
    }

}
