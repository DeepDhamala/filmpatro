package com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgetPasswordResetCodeRepository extends JpaRepository<ForgetPasswordResetCode, Long> {
    Optional<ForgetPasswordResetCode> findByForgetPasswordResetCode(String forgetPasswordResetCode);
}
