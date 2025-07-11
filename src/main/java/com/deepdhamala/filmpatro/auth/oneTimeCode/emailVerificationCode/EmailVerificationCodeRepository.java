package com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode;

import com.deepdhamala.filmpatro.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCodeEntity, Long> {

    Optional<EmailVerificationCodeEntity> findByUser(User user);

    Optional<EmailVerificationCodeEntity> findByEmailVerificationToken(String emailVerificationToken);
}
