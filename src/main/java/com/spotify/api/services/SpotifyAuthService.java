package com.spotify.api.services;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SpotifyAuthService {

    private WebClient webClient;
    private final String clientId = "5e5cf3d00970460f8015795a1d7aa408";
    private final String clientSecret = "989480887c5648698afbe626ea96e860";
    private String accessToken;


    public SpotifyAuthService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getAccessToken() {

      return webClient.post()
                .uri("https://accounts.spotify.com/api/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(String.class);
    }

    private String extractAndStoreToken(String responseBody) {
        String regex = "\"access_token\"\\s*:\\s*\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(responseBody);
        if (matcher.find()) {
            this.accessToken = matcher.group(1);
            return this.accessToken;
        }
        throw new RuntimeException("Access token not found in response");
    }

    public Mono<String> getInfo() {
        System.out.println(accessToken);
      return webClient.get()
                .uri("https://api.spotify.com/v1/me")
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class);
    }
}
