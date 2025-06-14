package com.deepdhamala.filmpatro.auth;

import com.deepdhamala.filmpatro.auth.otp.*;
import com.deepdhamala.filmpatro.auth.userAuth.*;
import com.deepdhamala.filmpatro.auth.token.TokenRepository;
import com.deepdhamala.filmpatro.email.EmailService;
import com.deepdhamala.filmpatro.user.User;
import com.deepdhamala.filmpatro.user.UserMapper;
import com.deepdhamala.filmpatro.user.UserRepository;
import com.deepdhamala.filmpatro.user.UserService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final OtpService otpService;
    private final OtpRepository otpRepository;
    private final OtpValidator otpValidator;

    @Transactional
    public void userRegistration(UserRegisterRequestDto userRequestRequestDto) {

        userService.validateUserDoesNotExist(userRequestRequestDto.getEmail(), userRequestRequestDto.getUsername());

        var user = userMapper.fromRegisterRequestDto(userRequestRequestDto);

        var savedUser = userRepository.save(user);

        String otp = otpService.generateOtp();
        var otpToken = otpService.saveOtp(savedUser, otp);
        emailService.sendOtpEmail(savedUser.getEmail(), otp);

    }

    public UserAuthenticationResponseDto userAuthentication(AuthenticationRequestDto authenticationRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequestDto.getEmail(),
                        authenticationRequestDto.getPassword()
                )
        );
        var user = userRepository.findByEmail(authenticationRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + authenticationRequestDto.getEmail()));
        return getUserAuthenticationResponseDto(user);

    }

    private UserAuthenticationResponseDto getUserAuthenticationResponseDto(User user) {
        var userPrincipal = UserPrincipal.builder()
                .user(user)
                .build();
        var jwtToken = jwtService.generateUserResourceAccessToken(userPrincipal);
        var refreshToken = jwtService.generateRefreshToken(userPrincipal);
        jwtService.saveUserToken(user, jwtToken, refreshToken);
        return UserAuthenticationResponseDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    public UserAuthenticationResponseDto exchangeAuthCodeForTokens(@Valid AuthCodeForTokensDto authCodeForTokensDto) {
        var code = authCodeForTokensDto.getAuthorizationCode();
        var expiration = jwtService.extractClaimFromJwt(code, Claims::getExpiration);
        LocalDateTime expirationTime = expiration.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        if (expirationTime.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code has expired.");
        }

        var token = tokenRepository.findByAuthCodeForTokens(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired code."));

        if (token.isUsedAuthCodeForTokens()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code has already been used.");
        }
        token.setUsedAuthCodeForTokens(true);
        tokenRepository.save(token);
        return UserAuthenticationResponseDto.builder()
                .accessToken(token.getToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    public UserAuthenticationResponseDto verifyOtp(OtpVerificationRequestDto otpVerificationRequestDto) {

        var user = userRepository.findByEmail(otpVerificationRequestDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var otpEntity = otpRepository.findByUser(user).orElse(null);
        otpValidator.validateOtp(otpEntity, otpVerificationRequestDto.getOtp());
        otpEntity.setUsed(true);
        otpRepository.save(otpEntity);

        user.setEnabled(true);
        userRepository.save(user);
        return getUserAuthenticationResponseDto(user);
    }
}
