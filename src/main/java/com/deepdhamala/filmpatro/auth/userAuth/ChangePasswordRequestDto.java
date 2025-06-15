package com.deepdhamala.filmpatro.auth.userAuth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordRequestDto {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
