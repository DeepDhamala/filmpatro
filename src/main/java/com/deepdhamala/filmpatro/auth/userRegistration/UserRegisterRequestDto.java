package com.deepdhamala.filmpatro.auth.userRegistration;

import com.deepdhamala.filmpatro.auth.password.PasswordMatches;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@PasswordMatches
public class UserRegisterRequestDto {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#.,_-])[A-Za-z\\d@$!%*?&#.,_-]{8,}$",
            message = "Password must be at least 8 characters, include upper & lower case letters, a number, and a special character"
    )
    private String password;

    @NotBlank(message = "Confirm password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#.,_-])[A-Za-z\\d@$!%*?&#.,_-]{8,}$",
            message = "Confirm password must match the password criteria"
    )
    private String confirmPassword;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be 3-30 characters")
    @Pattern(
            regexp = "^(?=.{3,30}$)(?![_.])(?!.*[_.]{2})[A-Za-z0-9._]+(?<![_.])$",
            message = "Username must be 3-30 characters, only letters, numbers, dots, and underscores; cannot start/end with dot/underscore, or have consecutive dots/underscores"
    )
    private String username;
}
