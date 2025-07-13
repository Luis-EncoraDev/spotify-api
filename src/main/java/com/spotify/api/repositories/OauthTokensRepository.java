package com.spotify.api.repositories;
import com.spotify.api.models.OauthTokensModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthTokensRepository extends JpaRepository<OauthTokensModel, Long> {
    //OauthTokens findByUserId (String userId);
}
