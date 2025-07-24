package de.oetting.bgp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
public class RootController {

    @Autowired
    private ApplicationContext ctx;

    // Map all router locations from vue app
    @GetMapping(value = {
            "/",
            "/index.html",
            "/register",
            "/gameGroups",
            "/account",
            "/gameGroup/**",
            "/admin/**",
            "/game/**",
    })
    @ResponseBody
    public Resource respondForVueRoutes() throws URISyntaxException {
        return new ClassPathResource("/static/index.html");
    }

    @GetMapping(value = {"/favicon.ico"})
    @ResponseBody
    public Resource favicon() {
        return new ClassPathResource("/static/favicon.ico");
    }
}
