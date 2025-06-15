package com.deepdhamala.filmpatro.auth;

import com.deepdhamala.filmpatro.auth.token.TokenRepository;
import com.deepdhamala.filmpatro.auth.token.emailVerification.*;
import com.deepdhamala.filmpatro.auth.userAuth.*;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

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
    public final EmailVerificationTokenService emailVerificationTokenService;
    private final PasswordEncoder passwordEncoder;
    private final ForgetPasswordResetRepository forgetPasswordResetRepository;
    private final OtpValidator otpValidator;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Transactional
    public void registerUser(UserRegisterRequestDto userRequestRequestDto) {

        userService.validateUserDoesNotExist(userRequestRequestDto.getEmail(), userRequestRequestDto.getUsername());

        var user = userMapper.fromRegisterRequestDto(userRequestRequestDto);

        var savedUser = userRepository.save(user);

        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.prepareEmailVerificationToken(savedUser);

        emailService.sendEmailVerificationEmail(savedUser.getEmail(), emailVerificationToken);

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

    public UserAuthenticationResponseDto getUserAuthenticationResponseDto(User user) {
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

    public UserAuthenticationResponseDto refreshTokenForTokens(RefreshTokenForTokensDto refreshTokenForTokensDto) {
        String refreshToken = refreshTokenForTokensDto.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is required");
        }

        String userEmail = jwtService.extractUsernameFromJwt(refreshToken);
        if (userEmail == null || userEmail.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var userPrincipal = UserPrincipal.builder()
                .user(user)
                .build();

        if (!jwtService.isTokenValid(refreshToken, userPrincipal)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is expired or invalid");
        }

        var accessToken = jwtService.generateUserResourceAccessToken(userPrincipal);
        jwtService.saveUserToken(user, accessToken, refreshToken);

        return UserAuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken) // can also rotate if needed
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(changePasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!changePasswordRequestDto.getNewPassword().equals(changePasswordRequestDto.getConfirmNewPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        revokeAllUserTokens(user);

        user.setPassword(passwordEncoder.encode(changePasswordRequestDto.getNewPassword()));

        userRepository.save(user);
    }

    public void recoverForgetPassword(@Valid ForgotPasswordRequestDto forgotPasswordRequestDto) {
        var user = userRepository.findByEmail(forgotPasswordRequestDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);
        var resetToken = ForgetPasswordResetToken.builder()
                .user(user)
                .forgetPasswordResetToken(token)
                .expiryDate(expiry)
                .build();
        forgetPasswordResetRepository.save(resetToken);
        String link = "https://localhost:8080/?forgetPasswordRecoverytoken=" + token;
        emailService.sendEmail(
                user.getEmail(),
                "Password Reset Request",
                "Click the link to reset your password: " + link);
    }

    public void resetForgetPassword(@Valid ForgetPasswordResetRequestDto forgetPasswordResetRequestDto) {
        var token = forgetPasswordResetRepository.findByForgetPasswordResetToken(forgetPasswordResetRequestDto.getForgetPasswordResetToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid or expired token"));

        if (token.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has already been used");
        }

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has expired");
        }

        var user = token.getUser();
        user.setPassword(passwordEncoder.encode(forgetPasswordResetRequestDto.getNewPassword()));
        userRepository.save(user);
        token.setUsed(true);
        forgetPasswordResetRepository.save(token);
    }

    public UserAuthenticationResponseDto verifyOtp(OtpVerificationRequestDto otpVerificationRequestDto) {

        var user = userRepository.findByEmail(otpVerificationRequestDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var otpEntity = emailVerificationTokenRepository.findByUser(user).orElse(null);
        otpValidator.validateOtp(otpEntity, otpVerificationRequestDto.getOtp());
        otpEntity.setUsed(true);
        emailVerificationTokenRepository.save(otpEntity);

        user.setEnabled(true);
        userRepository.save(user);
        return getUserAuthenticationResponseDto(user);
    }
}