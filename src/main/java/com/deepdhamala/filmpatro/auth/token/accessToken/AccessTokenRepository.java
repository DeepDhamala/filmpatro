package com.deepdhamala.filmpatro.auth.token.accessToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessTokenEntity, Integer> {
    @Query(value= """
            select t from AccessTokenEntity
            t inner join User u
            on t.user.id = u.id
            where u.id = :id and (t.expired = false or t.revoked = false)
            """)
    List<AccessTokenEntity> findAllValidAccessTokenByUser(Long id);

    Optional<AccessTokenEntity> findByAccessToken(String token);

//    Optional<Token> findByAuthCodeForTokens(String authCodeForTokens);
}