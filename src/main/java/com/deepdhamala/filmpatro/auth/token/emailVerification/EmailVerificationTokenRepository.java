package com.deepdhamala.filmpatro.auth.token.emailVerification;

import com.deepdhamala.filmpatro.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByUser(User user);

    Optional<EmailVerificationToken> findByEmailVerificationToken(String emailVerificationToken);
}
