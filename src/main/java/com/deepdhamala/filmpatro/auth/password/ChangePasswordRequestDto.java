package com.deepdhamala.filmpatro.auth.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@PasswordMatches
public class ChangePasswordRequestDto {
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#.,_-])[A-Za-z\\d@$!%*?&#.,_-]{8,}$",
            message = "Password must be at least 8 characters, include upper & lower case letters, a number, and a special character"
    )
    private String oldPassword;
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#.,_-])[A-Za-z\\d@$!%*?&#.,_-]{8,}$",
            message = "Password must be at least 8 characters, include upper & lower case letters, a number, and a special character"
    )
    private String Password;
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#.,_-])[A-Za-z\\d@$!%*?&#.,_-]{8,}$",
            message = "Password must be at least 8 characters, include upper & lower case letters, a number, and a special character"
    )
    private String confirmPassword;
}
