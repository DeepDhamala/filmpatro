package com.deepdhamala.filmpatro.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void validateUserDoesNotExist(String email, String username) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Registration Failed: Email already registered.");
        }
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Registration Failed: Username already taken.");
        }
    }
}
