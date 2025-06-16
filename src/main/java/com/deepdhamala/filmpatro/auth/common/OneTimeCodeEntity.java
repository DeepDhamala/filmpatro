package com.deepdhamala.filmpatro.auth.common;

import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodePurpose;
import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodeType;
import com.deepdhamala.filmpatro.common.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
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
public abstract class OneTimeCodeEntity extends AuditableEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OneTimeCodeType codeType;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(nullable = false)
    private boolean expired = false;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean isUsed = false;

    public abstract OneTimeCodePurpose getOneTimeCodePurpose();
}
