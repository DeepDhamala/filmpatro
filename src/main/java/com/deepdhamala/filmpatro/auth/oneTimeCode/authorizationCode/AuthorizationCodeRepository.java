package com.deepdhamala.filmpatro.auth.oneTimeCode.authorizationCode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, Long> {

    Optional<AuthorizationCode> findByAuthorizationCode(String authCode);
}
