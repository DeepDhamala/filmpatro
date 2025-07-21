package com.deepdhamala.filmpatro.auth.userRegistration;


public interface UserRegistrationService <T>{
    void registerNewUser(T requestDto);
    Class<T> getSupportedDtoType();
}
