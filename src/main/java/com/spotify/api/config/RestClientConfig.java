package com.spotify.api.config;

import com.spotify.api.services.OauthTokensService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    private final OauthTokensService oauthTokensService;

    public RestClientConfig(OauthTokensService oauthTokensService) {
        this.oauthTokensService = oauthTokensService;
    }

    @Bean
    public RestClient spotifyRestClient() {
        return RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    String accessToken = oauthTokensService.getAllOauthTokens().getFirst().getAccessToken();

                    // Set Authorization header
                    request.getHeaders().setBearerAuth(accessToken);

                    ClientHttpResponse response = execution.execute(request, body);

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println("----------------------------------");
                    System.out.println("Status code:" + response.getStatusCode());
                    System.out.println("----------------------------------");
                    System.out.println();
                    System.out.println();

                    if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                        // Token is probably expired
                        System.out.println();
                        System.out.println();
                        System.out.println();
                        System.out.println("----------------------------------");
                        System.out.println("Token expired. Refreshing...");
                        System.out.println("----------------------------------");
                        System.out.println();
                        System.out.println();
                        System.out.println();

                        String newAccessToken = oauthTokensService.refreshAccessTokenWithSpotify(); // Refresh + update DB

                        // Retry request with new token
                        HttpRequest retryRequest = new HttpRequestWrapper(request) {
                            @Override
                            public HttpHeaders getHeaders() {
                                HttpHeaders headers = new HttpHeaders();
                                headers.putAll(super.getHeaders());
                                headers.setBearerAuth(newAccessToken);
                                return headers;
                            }
                        };

                        return execution.execute(retryRequest, body);
                    }

                    return response;
                })
                .build();
    }
}
