package com.spotify.api.controllers;

import com.spotify.api.DTOs.ItemsSearchRequestDTO;
import com.spotify.api.services.SpotifyForDevelopersAPIService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class SpotifyForDevelopersAPIController {

    private final SpotifyForDevelopersAPIService spotifyForDevelopersAPIService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public SpotifyForDevelopersAPIController(SpotifyForDevelopersAPIService spotifyForDevelopersAPIService, OAuth2AuthorizedClientService autorizedClientService) {
        this.spotifyForDevelopersAPIService = spotifyForDevelopersAPIService;
        this.authorizedClientService = autorizedClientService;
    }

    @GetMapping("/me/top/artists")
    public ResponseEntity<Object> getTopArtists(Authentication authentication, @RequestParam(defaultValue = "10") int limit) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String accessToken = client.getAccessToken().getTokenValue();
        Object topArtists = spotifyForDevelopersAPIService.getTopArtists(accessToken, limit);
        return new ResponseEntity<>(topArtists, HttpStatus.OK);
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<Object> getArtist(Authentication authentication, @PathVariable String id) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String accessToken = client.getAccessToken().getTokenValue();
        Object topArtists = spotifyForDevelopersAPIService.getArtist(accessToken, id);
        return new ResponseEntity<>(topArtists, HttpStatus.OK);
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<Object> getAlbum(Authentication authentication, @PathVariable String id) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String accessToken = client.getAccessToken().getTokenValue();
        Object topArtists = spotifyForDevelopersAPIService.getAlbum(accessToken, id);
        return new ResponseEntity<>(topArtists, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            Authentication authentication,
            @RequestParam String q, // The search query
            @RequestParam List<String> type)
    {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String accessToken = client.getAccessToken().getTokenValue();

        // Create the request DTO
        ItemsSearchRequestDTO searchRequest = new ItemsSearchRequestDTO(q, type);

        try {
            Object searchResults = spotifyForDevelopersAPIService.searchItem(accessToken, searchRequest);
            return new ResponseEntity<>(searchResults, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace for debugging
            return new ResponseEntity<>("Error searching Spotify: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

}
