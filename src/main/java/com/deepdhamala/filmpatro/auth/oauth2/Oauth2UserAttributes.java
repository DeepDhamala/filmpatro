package com.deepdhamala.filmpatro.auth.oauth2;

public record Oauth2UserAttributes(String email, String fullName, String avatarUrl) {
    public Oauth2UserAttributes{
        if(email == null || fullName == null) {
            throw new Oauth2AttributeException("Email and full name cannot be null");
        }
    }
}
