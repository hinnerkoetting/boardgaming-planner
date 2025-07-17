package de.oetting.bgp.infrastructure;

import de.oetting.bgp.entities.Player;
import de.oetting.bgp.game.entity.Game;
import de.oetting.bgp.gamegroup.persistence.GameGroup;
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