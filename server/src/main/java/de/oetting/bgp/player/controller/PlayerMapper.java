package de.oetting.bgp.player.controller;

import de.oetting.bgp.entities.Player;
import de.oetting.bgp.player.model.PublicPlayerModel;

public class PlayerMapper {

    public static PublicPlayerModel map(Player player) {
        var playerModel = new PublicPlayerModel();
        playerModel.setId(player.getId());
        playerModel.setName(player.getName());
        return playerModel;
    }

}
