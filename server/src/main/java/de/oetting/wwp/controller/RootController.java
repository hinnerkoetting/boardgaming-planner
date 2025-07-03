package de.oetting.wwp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
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
            "/gameGroup/*",
            "/admin/**"
    })
    @ResponseBody
    public Resource respondForVueRoutes() throws URISyntaxException {
        return new ClassPathResource("/static/index.html");
    }
}
