package com.deepdhamala.filmpatro.auth.token;

import com.deepdhamala.filmpatro.user.User;

public interface Token {
    String getToken();
    User getUser();
    TokenPurpose getTokenPurpose();
}
