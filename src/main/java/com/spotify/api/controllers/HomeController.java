package com.spotify.api.controllers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private final OAuth2AuthorizedClientService clientService;

    public HomeController(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/")
    @ResponseBody
    public String home(@AuthenticationPrincipal OAuth2User principal, Authentication authentication) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                "spotify", authentication.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        String refreshToken = client.getRefreshToken().getTokenValue();

        return "<h1>Welcome, " + principal.getAttribute("display_name") + "!</h1>" +
                "<p>Email: " + principal.getAttribute("email") + "</p>" +
                "<p>Access Token: " + accessToken + "</p>" +
                "<p>Refresh Token: " + refreshToken + "</p>";
    }
}


