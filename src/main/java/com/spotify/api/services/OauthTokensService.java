package com.spotify.api.services;

import com.spotify.api.CustomException;
import com.spotify.api.DTOs.NewRefreshTokenResponseDTO;
import com.spotify.api.OauthTokensException;
import com.spotify.api.models.OauthTokensModel;
import com.spotify.api.repositories.OauthTokensRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import java.util.Base64;
import java.util.List;


@Service
public class OauthTokensService {

    private final OauthTokensRepository oauthTokensRepository;
    private final RestClient restClient;

    @Value("${SPOTIFY_CLIENT_ID}")
    private String clientId;

    @Value("${SPOTIFY_CLIENT_SECRET}")
    private String clientSecret;

    public OauthTokensService(OauthTokensRepository oauthTokensRepository, OAuth2AuthorizedClientManager authorizedClientManager) {
        this.oauthTokensRepository = oauthTokensRepository;
        this.restClient = RestClient.builder().build();
    }

    public List<OauthTokensModel> getAllOauthTokens () {
        try {
            return oauthTokensRepository.findAll();
        } catch (Exception  ex) {
            throw new CustomException (ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public OauthTokensModel createOauthTokens (OauthTokensModel oauthTokensModel) {
        try {
            return oauthTokensRepository.save(oauthTokensModel);
        } catch (Exception  ex) {
            throw new CustomException ("Error saving oauth tokens: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public OauthTokensModel deleteOauthTokens (OauthTokensModel oauthTokensModel) {
        try {
            oauthTokensRepository.delete(oauthTokensModel);
            return oauthTokensModel;
        } catch (Exception ex) {
            throw new OauthTokensException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String refreshAccessTokenWithSpotify() {
        OauthTokensModel currentToken = oauthTokensRepository.findAll().getFirst(); // or by user

        String refreshToken = currentToken.getRefreshToken();

        if (refreshToken == null) {
            throw new RuntimeException("No refresh token available.");
        }

        // Call Spotify to refresh the access token
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshToken);

        NewRefreshTokenResponseDTO response = restClient.post()
                .uri("https://accounts.spotify.com/api/token")
                .header("Authorization", "Basic " + encodedCredentials)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(formData)
                .retrieve()
                .body(NewRefreshTokenResponseDTO.class);

        assert response != null;
        String newAccessToken = response.getAccess_token();
        int expiresIn = response.getExpires_in();
        String newRefreshToken = response.getRefresh_token();

        OauthTokensModel newToken = new OauthTokensModel();
        newToken.setAccessToken(newAccessToken);
        newToken.setRefreshToken(newRefreshToken != null ? newRefreshToken : currentToken.getRefreshToken());
        newToken.setUserId(currentToken.getUserId());
        newToken.setExpiresIn(expiresIn);

        oauthTokensRepository.delete(currentToken);
        oauthTokensRepository.save(newToken);

        return newAccessToken;
    }

}
