package com.spotify.api.controllers;

import com.spotify.api.models.OauthTokensModel;
import com.spotify.api.services.OauthTokensService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/oauthTokens")
public class OauthTokensController {

    private final OauthTokensService oauthTokensService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public OauthTokensController(OauthTokensService oauthTokensService,
                                 OAuth2AuthorizedClientService authorizedClientService) {
        this.oauthTokensService = oauthTokensService;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/getOauthTokens")
    public ResponseEntity<OauthTokensModel> getAllOauthTokens(Authentication authentication) {
        OauthTokensModel oauthTokens = oauthTokensService.getOauthTokens(authentication.getName());
        return new ResponseEntity<>(oauthTokens, HttpStatus.OK);
    }

    @PostMapping("/createTokens")
    public ResponseEntity<OauthTokensModel> createOauthTokens(Authentication authentication) {
        // Load client by registration ID (same one used in application.properties)
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                "spotify", authentication.getName());

        if (client == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String accessToken = client.getAccessToken().getTokenValue();
        Instant expiresAt = client.getAccessToken().getExpiresAt();
        Instant issuedAt = client.getAccessToken().getIssuedAt();

        String refreshToken = client.getRefreshToken() != null
                ? client.getRefreshToken().getTokenValue()
                : null;

        int expiresIn = (expiresAt != null && issuedAt != null)
                ? (int) Duration.between(issuedAt, expiresAt).getSeconds()
                : 3600;

        OauthTokensModel tokenModel = new OauthTokensModel();
        tokenModel.setUserId(authentication.getName());
        tokenModel.setAccessToken(accessToken);
        tokenModel.setRefreshToken(refreshToken);
        tokenModel.setExpiresIn(expiresIn);

        OauthTokensModel saved = oauthTokensService.createOauthTokens(tokenModel);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteTokens")
    public ResponseEntity<OauthTokensModel> deleteOauthTokens(Authentication authentication) {
        OauthTokensModel oauthTokens = oauthTokensService.getOauthTokens(authentication.getName());
        OauthTokensModel deletedOauthTokens = oauthTokensService.deleteOauthTokens(oauthTokens);
        return new ResponseEntity<>(deletedOauthTokens, HttpStatus.OK);
    }
}
