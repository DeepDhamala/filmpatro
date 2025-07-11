package com.deepdhamala.filmpatro.auth.oneTimeCode.otp;

import com.deepdhamala.filmpatro.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findByUser(User user);
}