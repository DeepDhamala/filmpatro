package com.deepdhamala.filmpatro.user;

import com.deepdhamala.filmpatro.auth.principalUser.Role;
import com.deepdhamala.filmpatro.auth.userRegistration.UserRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User fromRegisterRequestDto(UserRegisterRequestDto userRegisterRequestDto) {
        return User.builder()
                .username(userRegisterRequestDto.getUsername())
                .email(userRegisterRequestDto.getEmail())
                .password(passwordEncoder.encode(userRegisterRequestDto.getPassword())) // secure the password here
                .role(Role.USER)
                .build();
    }
}

