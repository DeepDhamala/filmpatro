package com.deepdhamala.filmpatro.auth.otp;

import com.deepdhamala.filmpatro.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByUser(User user);
}
