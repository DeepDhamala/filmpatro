package com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode;

import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCode;
import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodePurpose;
import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodeType;
import com.deepdhamala.filmpatro.user.User;
import lombok.Builder;

import java.time.Instant;

@Builder
public class ForgetPasswordRecoveryCode implements OneTimeCode<ForgetPasswordRecoveryCode> {

    private String code;
    private OneTimeCodeType type;
    private OneTimeCodePurpose purpose;
    private Instant expiresAt;
    private User user;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public OneTimeCodeType getType() {
        return this.type;
    }

    @Override
    public OneTimeCodePurpose getPurpose() {
        return OneTimeCodePurpose.FORGET_PASSWORD_RESET;
    }

    @Override
    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    @Override
    public User getUser() {
        return this.user;
    }
}
