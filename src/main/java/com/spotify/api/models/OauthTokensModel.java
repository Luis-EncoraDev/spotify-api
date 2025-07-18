package com.spotify.api.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oauth_tokens")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OauthTokensModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
}
