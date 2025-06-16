package com.deepdhamala.filmpatro.auth.common;

import com.deepdhamala.filmpatro.auth.token.TokenPurpose;
import com.deepdhamala.filmpatro.auth.token.TokenType;
import com.deepdhamala.filmpatro.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class TokenEntity extends AuditableEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(nullable = false)
    private boolean expired = false;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean isUsed = false;

    public abstract TokenPurpose getTokenPurpose();
}
