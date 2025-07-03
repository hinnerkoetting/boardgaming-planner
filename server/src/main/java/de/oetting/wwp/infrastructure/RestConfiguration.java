package de.oetting.wwp.infrastructure;

import de.oetting.wwp.game.entity.Game;
import de.oetting.wwp.entities.GameGroup;
import de.oetting.wwp.entities.Player;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(Player.class);
        config.exposeIdsFor(Game.class);
        config.exposeIdsFor(GameGroup.class);
    }
}