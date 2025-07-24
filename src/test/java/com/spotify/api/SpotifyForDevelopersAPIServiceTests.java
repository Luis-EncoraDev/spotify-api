package com.spotify.api;

import com.spotify.api.DTOs.*;
import com.spotify.api.DTOs.SearchResponseDTOs.SearchResponseDTO;
import com.spotify.api.services.SpotifyForDevelopersAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpotifyForDevelopersAPIServiceTests {

    private String mockAccessToken;
    private String mockUserId;

    @Mock
    private RestClient spotifyRestClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private SpotifyForDevelopersAPIService spotifyService;

    private TopArtistsResponseDTO mockTopArtistsResponse;
    private ArtistDTO mockArtistResponse;
    private RelatedArtistsDTO mockRelatedArtistsResponse;
    private ArtistPopularTracksDTO mockPopularTracksResponse;
    private ArtistAlbumsResponseDTO mockArtistAlbumsResponse;
    private AlbumDTO mockAlbumResponse;
    private AlbumTracksResponseDTO mockAlbumTracksResponse;
    private SearchResponseDTO mockSearchResponse;

    @BeforeEach
    void setUp() {
        mockAccessToken = "access-token";
        mockUserId = "mockId";

        // Create mock artists
        ImageDTO image1 = new ImageDTO("https://example.com/image1.jpg", 300, 300);
        ImageDTO image2 = new ImageDTO("https://example.com/image2.jpg", 150, 150);

        ArtistFollowers followers1 = new ArtistFollowers();
        followers1.setTotal(1000000);

        ArtistFollowers followers2 = new ArtistFollowers();
        followers2.setTotal(500000);

        ArtistDTO artist1 = new ArtistDTO();
        artist1.setId("artist1");
        artist1.setName("Artist One");
        artist1.setImages(Arrays.asList(image1));
        artist1.setGenres(Arrays.asList("rock", "pop"));
        artist1.setFollowers(followers1);

        ArtistDTO artist2 = new ArtistDTO();
        artist2.setId("artist2");
        artist2.setName("Artist Two");
        artist2.setImages(Arrays.asList(image2));
        artist2.setGenres(Arrays.asList("jazz", "blues"));
        artist2.setFollowers(followers2);

        List<ArtistDTO> artists = Arrays.asList(artist1, artist2);
        mockTopArtistsResponse = new TopArtistsResponseDTO(artists);

        // Create mock single artist response
        mockArtistResponse = new ArtistDTO();
        mockArtistResponse.setId("artist123");
        mockArtistResponse.setName("Mock Artist");
        mockArtistResponse.setImages(Arrays.asList(image1));
        mockArtistResponse.setGenres(Arrays.asList("pop", "rock"));
        mockArtistResponse.setFollowers(followers1);

        // Create other mock responses
        mockRelatedArtistsResponse = new RelatedArtistsDTO();
        mockPopularTracksResponse = new ArtistPopularTracksDTO();
        mockArtistAlbumsResponse = new ArtistAlbumsResponseDTO();
        mockAlbumResponse = new AlbumDTO();
        mockAlbumTracksResponse = new AlbumTracksResponseDTO();
        mockSearchResponse = new SearchResponseDTO();
    }

    @Test
    @DisplayName("Should return top artists")
    void getTopArtists() {
        String accessToken = "valid-token";
        int limit = 10;
        String expectedUri = "https://api.spotify.com/v1/me/top/artists?limit=" + limit + "&time_range=long_term";

        // Mock the complete chain of RestClient calls
        when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(expectedUri)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header("Accept", MediaType.APPLICATION_JSON_VALUE)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TopArtistsResponseDTO.class)).thenReturn(mockTopArtistsResponse);

        TopArtistsResponseDTO result = spotifyService.getTopArtists(accessToken, limit);

        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        assertEquals("Artist One", result.getItems().get(0).getName());
        assertEquals("Artist Two", result.getItems().get(1).getName());
        verify(spotifyRestClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(expectedUri);
        verify(requestHeadersSpec, times(1)).header("Accept", MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    @DisplayName("Should return artist by ID")
    void getArtist() {
        String accessToken = "valid-token";
        String artistId = "artist123";
        String expectedUri = "https://api.spotify.com/v1/artists/" + artistId;

        when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(expectedUri)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header("Accept", MediaType.APPLICATION_JSON_VALUE)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(ArtistDTO.class)).thenReturn(mockArtistResponse);

        ArtistDTO result = spotifyService.getArtist(accessToken, artistId);

        assertNotNull(result);
        assertEquals("artist123", result.getId());
        assertEquals("Mock Artist", result.getName());
        verify(spotifyRestClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(expectedUri);
    }

    @Test
    @DisplayName("Should return related artists")
    void getRelatedArtists() {
        String accessToken = "valid-token";
        String expectedUri = "https://api.spotify.com/v1/artists/0TnOYISbd1XYRBk9myaseg/related-artists";

        when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(expectedUri)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header("Accept", MediaType.APPLICATION_JSON_VALUE)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(RelatedArtistsDTO.class)).thenReturn(mockRelatedArtistsResponse);

        RelatedArtistsDTO result = spotifyService.getRelatedArtists(accessToken);

        assertNotNull(result);
        verify(spotifyRestClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(expectedUri);
    }

    @Test
    @DisplayName("Should return artist popular tracks")
    void getArtistPopularTracks() {
        String accessToken = "valid-token";
        String artistId = "artist123";
        String expectedUri = "https://api.spotify.com/v1/artists/" + artistId + "/top-tracks?market=US";

        when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(expectedUri)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header("Accept", MediaType.APPLICATION_JSON_VALUE)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(ArtistPopularTracksDTO.class)).thenReturn(mockPopularTracksResponse);

        ArtistPopularTracksDTO result = spotifyService.getArtistPopularTracks(accessToken, artistId);

        assertNotNull(result);
        verify(spotifyRestClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(expectedUri);
    }

    @Test
    @DisplayName("Should return artist albums")
    void getArtistAlbums() {
        String accessToken = "valid-token";
        String artistId = "artist123";
        String limit = "10";
        String expectedUri = "https://api.spotify.com/v1/artists/" + artistId + "/albums?market=ES&limit=" + limit;

        when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(expectedUri)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header("Accept", MediaType.APPLICATION_JSON_VALUE)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(ArtistAlbumsResponseDTO.class)).thenReturn(mockArtistAlbumsResponse);

        ArtistAlbumsResponseDTO result = spotifyService.getArtistAlbums(accessToken, artistId, limit);

        assertNotNull(result);
        verify(spotifyRestClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(expectedUri);
    }

    @Test
    @DisplayName("Should return album by ID")
    void getAlbum() {
        String accessToken = "valid-token";
        String albumId = "album123";
        String expectedUri = "https://api.spotify.com/v1/albums/" + albumId;

        when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(expectedUri)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header("Accept", MediaType.APPLICATION_JSON_VALUE)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(AlbumDTO.class)).thenReturn(mockAlbumResponse);

        AlbumDTO result = spotifyService.getAlbum(accessToken, albumId);

        assertNotNull(result);
        verify(spotifyRestClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(expectedUri);
    }

    @Test
    @DisplayName("Should return album tracks")
    void getAlbumTracks() {
        String accessToken = "valid-token";
        String albumId = "album123";
        String expectedUri = "https://api.spotify.com/v1/albums/" + albumId + "/tracks";

        when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(expectedUri)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header("Accept", MediaType.APPLICATION_JSON_VALUE)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(AlbumTracksResponseDTO.class)).thenReturn(mockAlbumTracksResponse);

        AlbumTracksResponseDTO result = spotifyService.getAlbumTracks(accessToken, albumId);

        assertNotNull(result);
        verify(spotifyRestClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(expectedUri);
    }

    @Test
    @DisplayName("Should return search results")
    void searchItem() {
        String accessToken = "valid-token";
        ItemsSearchRequestDTO searchRequest = new ItemsSearchRequestDTO();
        searchRequest.setQuery("test query");
        searchRequest.setTypes("artist,track");

        String expectedUri = "https://api.spotify.com/v1/search?q=test query&type=artist,track&limit=3";

        when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(expectedUri)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header("Accept", MediaType.APPLICATION_JSON_VALUE)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SearchResponseDTO.class)).thenReturn(mockSearchResponse);

        SearchResponseDTO result = spotifyService.searchItem(accessToken, searchRequest);

        assertNotNull(result);
        verify(spotifyRestClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(expectedUri);
    }

//    @Test
//    @DisplayName("Should throw IllegalArgumentException when search query is empty")
//    void searchItemWithEmptyQuery() {
//        String accessToken = "valid-token";
//        ItemsSearchRequestDTO searchRequest = new ItemsSearchRequestDTO();
//        searchRequest.setQuery("");
//        searchRequest.setTypes("artist");
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            spotifyService.searchItem(accessToken, searchRequest);
//        });
//
//        assertEquals("Search query 'q' cannot be empty.", exception.getMessage());
//    }

//    @Test
//    @DisplayName("Should throw IllegalArgumentException when search types is empty")
//    void searchItemWithEmptyTypes() {
//        String accessToken = "valid-token";
//        ItemsSearchRequestDTO searchRequest = new ItemsSearchRequestDTO();
//        searchRequest.setQuery("test");
//        searchRequest.setTypes("");
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            spotifyService.searchItem(accessToken, searchRequest);
//        });
//
//        assertEquals("Search type 'type' cannot be empty. Must be one or more of: album, artist, playlist, track, show, episode, audiobook.", exception.getMessage());
//    }

}