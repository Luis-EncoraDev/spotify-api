package com.spotify.api.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewRefreshTokenResponseDTO {
    private String access_token;
    private String refresh_token;
    private int expires_in;
}
