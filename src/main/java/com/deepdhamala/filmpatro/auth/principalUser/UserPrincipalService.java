package com.deepdhamala.filmpatro.auth.principalUser;

import com.deepdhamala.filmpatro.user.User;
import com.deepdhamala.filmpatro.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPrincipalService{

    private UserService userService;

    public UserPrincipal getUserPrincipalByEmailOrThrow(String email) {
    User user = userService.getByEmailOrThrow(email);
    return UserPrincipal.builder()
            .user(user)
            .build();
    }
}
