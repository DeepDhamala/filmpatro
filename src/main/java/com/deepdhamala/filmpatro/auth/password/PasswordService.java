package com.deepdhamala.filmpatro.auth.password;

import com.deepdhamala.filmpatro.auth.principalUser.UserPrincipal;
import com.deepdhamala.filmpatro.auth.token.TokenManager;
import com.deepdhamala.filmpatro.user.User;
import com.deepdhamala.filmpatro.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenManager tokenManager;
    private final ChangePasswordValidator changePasswordValidator;

    @Transactional
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = ((UserPrincipal) authentication.getPrincipal()).user();

        changePasswordValidator.validate(changePasswordRequestDto, user);

        tokenManager.revokeAllTokensByUser(user);

        user.setPassword(passwordEncoder.encode(changePasswordRequestDto.getPassword()));

    }


}
