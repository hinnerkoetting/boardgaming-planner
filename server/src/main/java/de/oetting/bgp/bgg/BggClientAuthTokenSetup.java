package de.oetting.bgp.bgg;

import jakarta.annotation.PostConstruct;
import org.audux.bgg.BggClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BggClientAuthTokenSetup {

    private static final Logger LOG = LoggerFactory.getLogger(BggClientAuthTokenSetup.class);

    @PostConstruct
    public void initAuthToken() {
        String bggAuthToken = System.getenv("BGG_AUTH_TOKEN");
        if (bggAuthToken == null) {
            LOG.warn("BGG_AUTH_TOKEN not configured. Calling BGG will not work");
        } else {
            LOG.info("BGG_AUTH_TOKEN configured");
            BggClient.authToken(bggAuthToken);
        }
    }
}
