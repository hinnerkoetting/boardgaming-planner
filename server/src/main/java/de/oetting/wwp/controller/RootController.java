package de.oetting.wwp.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class RootController {

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/")
    public ResponseEntity<Void> redirectFromRoot() throws URISyntaxException {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(new URI("/index.html"));
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.FOUND);
    }
}
