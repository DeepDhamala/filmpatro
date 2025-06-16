package com.deepdhamala.filmpatro.user;

import com.deepdhamala.filmpatro.user.exception.UserAlreadyExistsException;
import com.deepdhamala.filmpatro.user.exception.UserNotFoundException;
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

    public User getByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

}
