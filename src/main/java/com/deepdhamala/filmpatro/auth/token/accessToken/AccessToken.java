package com.deepdhamala.filmpatro.auth.token.accessToken;

import com.deepdhamala.filmpatro.auth.token.Token;
import com.deepdhamala.filmpatro.auth.token.TokenPurpose;
import com.deepdhamala.filmpatro.auth.token.TokenType;
import com.deepdhamala.filmpatro.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class AccessToken implements Token {

    private String token;
    private User belongsTo;

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public User getUser() {
        return this.belongsTo;
    }

    @Override
    public TokenPurpose getTokenPurpose() {
        return TokenPurpose.ACCESS;
    }
}
