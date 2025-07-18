package de.oetting.bgp.gamegroup.model;

import de.oetting.bgp.gamegroup.persistence.GameGroup;

public class GameGroupModelMapper {

    public static GameGroupModel map(GameGroup savedEntity) {
        GameGroupModel model = new GameGroupModel();
        model.setId(savedEntity.getId());
        model.setName(savedEntity.getName());
        model.setType(savedEntity.getType());
        return model;
    }

}
