package com.spotify.api.services;

import com.spotify.api.CustomException;
import com.spotify.api.DTOs.ItemsSearchRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SpotifyForDevelopersAPIService {

    RestClient restClient = RestClient.create();

    public Object getTopArtists(String accessToken, int limit) {
        try {
            return restClient.get()
                    .uri("https://api.spotify.com/v1/me/top/artists?limit=" + limit)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(Object.class);
        } catch (RestClientException ex) {
            throw new CustomException("Error calling the Spotify API: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Object getArtist(String accessToken, String id) {
        try {
            return restClient.get()
                    .uri("https://api.spotify.com/v1/artists/" + id)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(Object.class);
        } catch (RestClientException ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Object getAlbum(String accessToken, String id) {
        try {
            return restClient.get()
                    .uri("https://api.spotify.com/v1/albums/" + id)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(Object.class);
        } catch (RestClientException ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Object searchItem(String accessToken, ItemsSearchRequestDTO searchRequest) {

        if (searchRequest.getQuery() == null || searchRequest.getQuery().isEmpty()) {
            throw new IllegalArgumentException("Search query 'q' cannot be empty.");
        }
        if (searchRequest.getTypes() == null || searchRequest.getTypes().isEmpty()) {
            throw new IllegalArgumentException("Search type 'type' cannot be empty. Must be one or more of: album, artist, playlist, track, show, episode, audiobook.");
        }

        try {
            return restClient.get()
                    .uri("https://api.spotify.com/v1/search" + "?q=" +
                            searchRequest.getQuery() +
                            "&type=" +
                            String.join(",", searchRequest.getTypes()))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(Object.class);
        } catch (RestClientException ex) {
            throw new CustomException("Error calling Spotify Search API: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
