package com.deepdhamala.filmpatro.auth.userRegistration;

import com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode.EmailVerificationCode;
import com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode.EmailVerificationCodeService;
import com.deepdhamala.filmpatro.email.EmailDeliveryException;
import com.deepdhamala.filmpatro.email.EmailService;
import com.deepdhamala.filmpatro.user.UserMapper;
import com.deepdhamala.filmpatro.user.UserRepository;
import com.deepdhamala.filmpatro.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailVerificationCodeService emailVerificationCodeService;
    private final EmailService emailService;

    @Transactional(noRollbackFor = EmailDeliveryException.class)
    public void registerNewUser(UserRegisterRequestDto userRequestRequestDto) {

        userService.validateUserDoesNotExist(userRequestRequestDto.getEmail(), userRequestRequestDto.getUsername());

        var user = userMapper.fromRegisterRequestDto(userRequestRequestDto);

        var savedUser = userRepository.save(user);

        EmailVerificationCode emailVerificationCode = emailVerificationCodeService.issueEmailVerificationCode(savedUser);

        emailService.sendEmailVerificationEmailHtml(savedUser.getEmail(), emailVerificationCode);
    }
}
