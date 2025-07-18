package com.spotify.api;

import com.spotify.api.models.OauthTokensModel;
import com.spotify.api.repositories.OauthTokensRepository;
import com.spotify.api.services.OauthTokensService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OauthTokensServiceTests {

    @Mock
    private OauthTokensRepository oauthTokensRepository;

    @InjectMocks
    private OauthTokensService oauthTokensService;
    private OauthTokensModel mockToken;

    @BeforeEach
    void setUp () {

        OauthTokensModel token = new OauthTokensModel();
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setUserId("1");
        token.setExpiresIn(3600);
        mockToken = token;
    }

    @Test
    @DisplayName("Should return token stocked in database")
    void getTokens() {
        when(oauthTokensRepository.findByUserId(mockToken.getUserId())).thenReturn(mockToken);
        OauthTokensModel token = oauthTokensService.getOauthTokens(mockToken.getUserId());
        Assertions.assertNotNull(token);
        verify(oauthTokensRepository, times(1)).findByUserId(mockToken.getUserId());
    }
}
