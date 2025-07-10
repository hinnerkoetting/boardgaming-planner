package de.oetting.bgp.game.model;

import de.oetting.bgp.game.entity.Game;

public class GameConverter {

    public static GameModel convert(Game game) {
        var model = new GameModel();
        model.setThumbnailUrl(game.getThumbnailUrl());
        model.setDescription(game.getDescription());
        model.setId(game.getId());
        model.setImageUrl(game.getImageUrl());
        model.setName(game.getName());
        model.setMaxPlayers(game.getMaxPlayers());
        model.setBestNumberOfPlayers(game.getBestNumberOfPlayers().stream().toList());
        model.setRecommendedNumberOfPlayers(game.getRecommendedNumberOfPlayers().stream().toList());
        return model;
    }
}
