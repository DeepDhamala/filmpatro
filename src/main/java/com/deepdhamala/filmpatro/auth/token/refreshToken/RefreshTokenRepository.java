package com.deepdhamala.filmpatro.auth.token.refreshToken;

import com.deepdhamala.filmpatro.auth.token.accessToken.AccessToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    @Query(value= """
            select t from RefreshToken
            t inner join User u
            on t.user.id = u.id
            where u.id = :id and (t.expired = false or t.revoked = false)
            """)
    List<RefreshToken> findAllValidRefreshTokenByUser(Long id);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
