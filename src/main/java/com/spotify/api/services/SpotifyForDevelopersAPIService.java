package com.spotify.api.services;

import com.spotify.api.CustomException;
import com.spotify.api.DTOs.*;
import com.spotify.api.DTOs.SearchResponseDTOs.SearchResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class SpotifyForDevelopersAPIService {

    RestClient restClient = RestClient.create();
    RestClient spotifyRestClient;

    public SpotifyForDevelopersAPIService(RestClient spotifyRestClient) {
        this.restClient = RestClient.builder().build();
        this.spotifyRestClient = spotifyRestClient;
    }

    public TopArtistsResponseDTO getTopArtists(String accessToken, int limit) {
        try {
            return spotifyRestClient.get()
                    .uri("https://api.spotify.com/v1/me/top/artists?limit=" + limit + "&time_range=long_term")
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(TopArtistsResponseDTO.class);
        } catch (RestClientException ex) {
            System.out.println("Error calling the Spotify API: " + ex.getMessage());
            throw new CustomException("Error calling the Spotify API: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ArtistDTO getArtist(String accessToken, String id) {
        try {
            return spotifyRestClient.get()
                    .uri("https://api.spotify.com/v1/artists/" + id)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(ArtistDTO.class);
        } catch (RestClientException ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public RelatedArtistsDTO getRelatedArtists(String accessToken) {
        try {
            return spotifyRestClient.get()
                    .uri("https://api.spotify.com/v1/artists/0TnOYISbd1XYRBk9myaseg/related-artists")
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(RelatedArtistsDTO.class);
        } catch (RestClientException ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ArtistPopularTracksDTO getArtistPopularTracks(String accessToken, String id) {
        try {
            ArtistPopularTracksDTO tracks = spotifyRestClient.get()
                    .uri("https://api.spotify.com/v1/artists/"+ id + "/top-tracks?market=US")
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(ArtistPopularTracksDTO.class);
            return tracks;
        } catch (RestClientException ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ArtistAlbumsResponseDTO getArtistAlbums(String accessToken, String artistId, String limit) {
        try {
            return spotifyRestClient.get()
                    .uri("https://api.spotify.com/v1/artists/" + artistId + "/albums?market=ES&limit=" + limit)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(ArtistAlbumsResponseDTO.class);
        } catch (RestClientException ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public AlbumDTO getAlbum(String accessToken, String id) {
        try {
            return spotifyRestClient.get()
                    .uri("https://api.spotify.com/v1/albums/" + id)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(AlbumDTO.class);
        } catch (RestClientException ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public AlbumTracksResponseDTO getAlbumTracks(String accessToken, String albumId) {
        try {
            return spotifyRestClient.get()
                    .uri("https://api.spotify.com/v1/albums/" + albumId + "/tracks")
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(AlbumTracksResponseDTO.class);
        } catch (RestClientException ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public SearchResponseDTO searchItem(String accessToken, ItemsSearchRequestDTO searchRequest) {
        if (searchRequest.getQuery() == null || searchRequest.getQuery().isEmpty()) {
            throw new IllegalArgumentException("Search query 'q' cannot be empty.");
        }
        if (searchRequest.getTypes() == null || searchRequest.getTypes().isEmpty()) {
            throw new IllegalArgumentException("Search type 'type' cannot be empty. Must be one or more of: album, artist, playlist, track, show, episode, audiobook.");
        }

        try {
            return spotifyRestClient.get()
                    .uri("https://api.spotify.com/v1/search" + "?q=" +
                            searchRequest.getQuery() +
                            "&type=" +
                            searchRequest.getTypes() +
                            "&limit=10"
                    )
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(SearchResponseDTO.class);
        } catch (RestClientException ex) {
            throw new CustomException("Error calling Spotify Search API: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
