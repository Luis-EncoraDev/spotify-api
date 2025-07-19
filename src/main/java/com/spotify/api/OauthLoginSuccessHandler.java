package com.spotify.api;

import com.spotify.api.models.OauthTokensModel;
import com.spotify.api.services.OauthTokensService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.oauth2.client.*;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Component
public class OauthLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthorizedClientService clientService;
    private final OauthTokensService oauthTokensService;
    private final JwtUtil jwtUtil;

    public OauthLoginSuccessHandler(OAuth2AuthorizedClientService clientService,
                                     OauthTokensService oauthTokensService,
                                    JwtUtil jwtUtil) {
        this.clientService = clientService;
        this.oauthTokensService = oauthTokensService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String jwtToken = jwtUtil.generateToken(authentication.getName());

        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                "spotify", authentication.getName());

        if (client != null) {
            String accessToken = client.getAccessToken().getTokenValue();
            Instant issuedAt = client.getAccessToken().getIssuedAt();
            Instant expiresAt = client.getAccessToken().getExpiresAt();
            String refreshToken = client.getRefreshToken() != null
                    ? client.getRefreshToken().getTokenValue()
                    : null;

            int expiresIn = (issuedAt != null && expiresAt != null)
                    ? (int) Duration.between(issuedAt, expiresAt).getSeconds()
                    : 3600;

            OauthTokensModel model = new OauthTokensModel();
            model.setUserId(authentication.getName());
            model.setAccessToken(accessToken);
            model.setRefreshToken(refreshToken);
            model.setExpiresIn(expiresIn);

            oauthTokensService.createOauthTokens(model);
        }

        response.sendRedirect("http://localhost:5173/oauth2/callback?token=" + jwtToken);
    }
}

