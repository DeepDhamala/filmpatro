package com.deepdhamala.filmpatro.auth;

import com.deepdhamala.filmpatro.auth.token.Token;
import com.deepdhamala.filmpatro.auth.token.TokenRepository;
import com.deepdhamala.filmpatro.auth.token.TokenType;
import com.deepdhamala.filmpatro.user.User;
import com.deepdhamala.filmpatro.user.UserMapper;
import com.deepdhamala.filmpatro.user.UserRepository;
import com.deepdhamala.filmpatro.user.UserService;
import com.deepdhamala.filmpatro.user.security.UserAuthenticationResponseDto;
import com.deepdhamala.filmpatro.user.security.UserPrincipal;
import com.deepdhamala.filmpatro.user.security.UserRegisterRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Transactional
    public UserAuthenticationResponseDto userRegistration(UserRegisterRequestDto userRequestRequestDto) {

        userService.validateUserDoesNotExist(userRequestRequestDto.getEmail());

        var user = userMapper.fromRegisterRequestDto(userRequestRequestDto);

        var savedUser = userRepository.save(user);
        var userPrincipal = UserPrincipal.builder()
                .user(savedUser)
                .build();

        var jwtToken = jwtService.generateUserResourceAccessToken(userPrincipal);
        var refreshToken = jwtService.generateRefreshToken(userPrincipal);
        saveUserToken(savedUser, jwtToken);

        return UserAuthenticationResponseDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
