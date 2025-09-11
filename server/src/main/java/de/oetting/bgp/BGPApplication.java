package de.oetting.bgp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BGPApplication {

    public static void main(String[] args) {
        SpringApplication.run(BGPApplication.class, args);
    }

}
