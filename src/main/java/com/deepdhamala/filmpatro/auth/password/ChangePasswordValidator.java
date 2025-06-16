package com.deepdhamala.filmpatro.auth.password;

import com.deepdhamala.filmpatro.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangePasswordValidator {

    private final PasswordEncoder passwordEncoder;

    public void validate(ChangePasswordRequestDto dto, User user) {
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("The current password is incorrect.");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordMismatchException("New passwords do not match.");
        }
    }
}
