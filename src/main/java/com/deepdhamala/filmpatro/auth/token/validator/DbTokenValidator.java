package com.deepdhamala.filmpatro.auth.token.validator;

import com.deepdhamala.filmpatro.auth.token.Token;
import com.deepdhamala.filmpatro.auth.token.TokenPurpose;

public interface DbTokenValidator {
    boolean isValid(Token token);
    boolean supports(TokenPurpose purpose);
}
