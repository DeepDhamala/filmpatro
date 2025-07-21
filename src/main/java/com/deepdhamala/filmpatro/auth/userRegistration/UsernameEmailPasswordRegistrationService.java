package com.deepdhamala.filmpatro.auth.userRegistration;

import com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode.EmailVerificationCodeService;
import com.deepdhamala.filmpatro.email.EmailDeliveryException;
import com.deepdhamala.filmpatro.email.JavaMailEmailService;
import com.deepdhamala.filmpatro.email.message.EmailVerificationEmailMessage;
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
public class UsernameEmailPasswordRegistrationService implements UserRegistrationService<UsernameEmailPasswordRegisterRequestDto> {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailVerificationCodeService emailVerificationCodeService;
    private final JavaMailEmailService emailService;

    @Override
    @Transactional(noRollbackFor = EmailDeliveryException.class)
    public void registerNewUser(UsernameEmailPasswordRegisterRequestDto userRequestRequestDto) {

        userService.isUsernameEmailAlreadyRegistered(userRequestRequestDto.getEmail(), userRequestRequestDto.getUsername());

        var userEntity = userMapper.fromRegisterRequestDto(userRequestRequestDto);

        var savedUser = userRepository.save(userEntity);

        var emailVerificationCode = emailVerificationCodeService.issue(savedUser);

        emailService.sendEmail(new EmailVerificationEmailMessage(emailVerificationCode));
    }

    @Override
    public Class<UsernameEmailPasswordRegisterRequestDto> getSupportedDtoType() {
        return UsernameEmailPasswordRegisterRequestDto.class;
    }
}
