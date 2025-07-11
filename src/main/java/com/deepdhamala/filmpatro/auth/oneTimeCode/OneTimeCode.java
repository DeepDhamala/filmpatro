package com.deepdhamala.filmpatro.auth.oneTimeCode;

import com.deepdhamala.filmpatro.user.User;

import java.time.Instant;

public interface OneTimeCode<T extends OneTimeCode<T>> {
    String getCode();

    OneTimeCodeType getType();

    OneTimeCodePurpose getPurpose();

    Instant getExpiresAt();

    User getUser();
}
