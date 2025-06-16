package com.deepdhamala.filmpatro.auth.password;

import com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode.ForgetPasswordResetRequestDto;
import com.deepdhamala.filmpatro.auth.userRegistration.UserRegisterRequestDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof ForgetPasswordResetRequestDto dto) {
            return dto.getPassword() != null && dto.getConfirmPassword() != null &&
                    dto.getPassword().equals(dto.getConfirmPassword());
        }

        if (obj instanceof UserRegisterRequestDto dto) {
            return dto.getPassword() != null && dto.getConfirmPassword() != null &&
                    dto.getPassword().equals(dto.getConfirmPassword());
        }

        return true;
    }
}