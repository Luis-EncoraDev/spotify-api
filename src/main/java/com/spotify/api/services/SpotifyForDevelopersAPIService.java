package com.spotify.api.services;

import com.spotify.api.CustomException;
import com.spotify.api.DTOs.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaName;
import java.util.Collections;
import java.util.List;

@Service
public class SpotifyForDevelopersAPIService {

    RestClient restClient = RestClient.create();

    public SpotifyForDevelopersAPIService() {
        // Since you're not setting a base URL on the builder,
        // the default empty base URL will be used.
        this.restClient = RestClient.builder().build(); // Assuming default builder is fine
    }


    public List<ArtistDTO> getTopArtists(String accessToken, int limit) {
        try {
            TopArtistsResponseDTO response = restClient.get()
                    .uri("https://api.spotify.com/v1/me/top/artists?limit=" + limit + "&time_range=long_term")
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(TopArtistsResponseDTO.class);
            return response != null ? response.getItems() : List.of();
        } catch (RestClientException ex) {
            System.out.println("Error calling the Spotify API: " + ex.getMessage());
            throw new CustomException("Error calling the Spotify API: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ArtistDTO getArtist(String accessToken, String id) {
        try {
            return restClient.get()
                    .uri("https://api.spotify.com/v1/artists/" + id)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(ArtistDTO.class);
        } catch (RestClientException ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public RelatedArtistsDTO getRelatedArtists(String accessToken) {
        try {
            return restClient.get()
                    .uri("https://api.spotify.com/v1/artists/0TnOYISbd1XYRBk9myaseg/related-artists")
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(RelatedArtistsDTO.class);
        } catch (RestClientException ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ArtistPopularTracksDTO getArtistPopularTracks(String accessToken, String artistId) {
            try{
                return restClient.get()
                        .uri("https://api.spotify.com/v1/artists/" + artistId + "/top-tracks")
                        .header("Authorization", "Bearer " + accessToken)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .retrieve()
                        .body(ArtistPopularTracksDTO.class);
            } catch (RestClientException ex) {
                throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    public ArtistAlbumsResponseDTO getArtistAlbums(String accessToken, String artistId, String limit) {
            try {
                return restClient.get()
                        .uri("https://api.spotify.com/v1/artists/" + artistId + "/albums?market=ES&limit=" + limit)
                        .header("Authorization", "Bearer " + accessToken)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .retrieve()
                        .body(ArtistAlbumsResponseDTO.class);
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
