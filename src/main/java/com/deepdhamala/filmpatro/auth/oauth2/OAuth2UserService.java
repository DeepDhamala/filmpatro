package com.deepdhamala.filmpatro.auth.oauth2;

import com.deepdhamala.filmpatro.auth.principalUser.Role;
import com.deepdhamala.filmpatro.user.User;
import com.deepdhamala.filmpatro.user.exception.UserLockedException;
import com.deepdhamala.filmpatro.user.exception.UserNotEnabledException;
import com.deepdhamala.filmpatro.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserService {

    private final UserRepository userRepository;

    public User getOrCreateUser(String email, String fullName, String avatarUrl) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    validateUserStatus(user);
                    return user;
                })
                .orElseGet(() -> createUser(email, fullName, avatarUrl));
    }

    private void validateUserStatus(User user) {
        if (!user.isEnabled()) {
            throw new UserNotEnabledException("Email verification pending. Please verify your email.");
        }
        if (!user.isAccountNonLocked() || !user.isCredentialsNonExpired() || !user.isAccountNonExpired()) {
            throw new UserLockedException("Your account is either locked or expired. Please contact support.");
        }
    }

    private User createUser(String email, String fullName, String avatarUrl) {
        var baseUsername = email.split("@")[0];
        var username = generateUniqueUsername(baseUsername);
        User user = User.builder()
                .email(email)
                .fullName(fullName)
                .enabled(true)
                .avatarUrl(avatarUrl)
                .username(username)
                .password(null)
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }

    private String generateUniqueUsername(String baseUsername) {
        String username = baseUsername;
        int counter = 1;
        while(userRepository.existsByUsername(username)) {
            username = baseUsername + counter++;
        }
        return username;
    }

}
