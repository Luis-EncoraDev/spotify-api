package com.spotify.api.controllers;

import com.spotify.api.CustomException;
import com.spotify.api.DTOs.*;
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
    public ResponseEntity<TopArtistsResponseDTO> getTopArtists(Authentication authentication, @RequestParam(defaultValue = "10") int limit) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        TopArtistsResponseDTO topArtists = spotifyForDevelopersAPIService.getTopArtists(accessToken, limit);
        return new ResponseEntity<>(topArtists, HttpStatus.OK);
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<ArtistDTO> getArtist(Authentication authentication, @PathVariable String id) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        ArtistDTO artist = spotifyForDevelopersAPIService.getArtist(accessToken, id);
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }

    @GetMapping("/artists/{id}/relatedArtists")
    public ResponseEntity<RelatedArtistsDTO> getRelatedArtists(Authentication authentication, @PathVariable String id) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        RelatedArtistsDTO artist = spotifyForDevelopersAPIService.getRelatedArtists(accessToken);
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }

    @GetMapping("/artists/{artistId}/popularTracks")
    public ResponseEntity<ArtistPopularTracksDTO> getArtistPopularTracks(Authentication authentication, @PathVariable String artistId) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        ArtistPopularTracksDTO artistTopTracks = spotifyForDevelopersAPIService.getArtistPopularTracks(accessToken, artistId);
        return new ResponseEntity<>(artistTopTracks, HttpStatus.OK);
    }

    @GetMapping("/artists/{artistId}/albums")
    public ResponseEntity<ArtistAlbumsResponseDTO> getArtistAlbums(Authentication authentication, @PathVariable String artistId, @RequestParam(defaultValue = "9") String limit) {
        try {
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());
            String accessToken = client.getAccessToken().getTokenValue();
            ArtistAlbumsResponseDTO albums = spotifyForDevelopersAPIService.getArtistAlbums(accessToken, artistId, limit);
            return new ResponseEntity<>(albums, HttpStatus.OK);
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<AlbumDTO> getAlbum(Authentication authentication, @PathVariable String id) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        AlbumDTO album = spotifyForDevelopersAPIService.getAlbum(accessToken, id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @GetMapping("/albums/{id}/tracks")
    public ResponseEntity<AlbumTracksResponseDTO> getAlbumTracks(Authentication authentication, @PathVariable String id) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        AlbumTracksResponseDTO albumTracks = spotifyForDevelopersAPIService.getAlbumTracks(accessToken, id);
        return new ResponseEntity<>(albumTracks, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            Authentication authentication,
            @RequestParam String q,
            @RequestParam List<String> type) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        ItemsSearchRequestDTO searchRequest = new ItemsSearchRequestDTO(q, type);

        Object searchResults = spotifyForDevelopersAPIService.searchItem(accessToken, searchRequest);
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

}
