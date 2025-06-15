package com.deepdhamala.filmpatro.auth.token.emailVerification;

import com.deepdhamala.filmpatro.common.AuditableEntity;
import com.deepdhamala.filmpatro.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationToken extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private String emailVerificationToken;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean used = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}