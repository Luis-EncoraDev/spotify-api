package com.spotify.api.controllers;

import com.spotify.api.services.SpotifyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

    private SpotifyAuthService spotifyAuthService;

    @Autowired
    public SpotifyController(SpotifyAuthService spotifyAuthService) {
        this.spotifyAuthService = spotifyAuthService;
    }

    @GetMapping("/getAccessToken")
    public Mono<String> getAccessToken() {
        return spotifyAuthService.getAccessToken();
    }

    @GetMapping("/getUserInfo")
    public Mono<String> getUserInf() {
        return spotifyAuthService.getInfo();
    }
}
