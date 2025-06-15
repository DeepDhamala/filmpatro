package com.deepdhamala.filmpatro.auth.userAuth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgetPasswordResetRepository extends JpaRepository<ForgetPasswordResetToken, Long> {

    Optional<ForgetPasswordResetToken> findByForgetPasswordResetToken(String forgetPasswordResetToken);


}
