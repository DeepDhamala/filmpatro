package com.deepdhamala.filmpatro.user;

import com.deepdhamala.filmpatro.user.security.UserRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User fromRegisterRequestDto(UserRegisterRequestDto userRegisterRequestDto) {
        return User.builder()
                .firstName(userRegisterRequestDto.getFirstname())
                .lastName(userRegisterRequestDto.getLastname())
                .email(userRegisterRequestDto.getEmail())
                .password(passwordEncoder.encode(userRegisterRequestDto.getPassword())) // secure the password here
                .role(userRegisterRequestDto.getRole())
                .build();
    }
}

