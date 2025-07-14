package com.deepdhamala.filmpatro.auth.token.refreshToken;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, Long> {
    @Query(value= """
            select t from RefreshTokenEntity
            t inner join User u
            on t.user.id = u.id
            where u.id = :id and (t.expired = false or t.revoked = false)
            """)
    List<RefreshTokenEntity> findAllValidRefreshTokenByUser(Long id);

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
