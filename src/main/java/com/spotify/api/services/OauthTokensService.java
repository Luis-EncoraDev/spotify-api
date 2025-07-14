package com.spotify.api.services;

import com.spotify.api.CustomException;
import com.spotify.api.models.OauthTokensModel;
import com.spotify.api.repositories.OauthTokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OauthTokensService {

    private final OauthTokensRepository oauthTokensRepository;

    @Autowired
    public OauthTokensService (OauthTokensRepository oauthTokensRepository) {
        this.oauthTokensRepository = oauthTokensRepository;
    }

    public List<OauthTokensModel> getAllOauthTokens () {
        try {
            return oauthTokensRepository.findAll();
        } catch (CustomException  ex) {
            throw new CustomException (ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public OauthTokensModel createOauthTokens (OauthTokensModel oauthTokensModel) {
        try {
            return oauthTokensRepository.save(oauthTokensModel);
        } catch (CustomException  ex) {
            throw new CustomException ("Error saving oauth tokens: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
