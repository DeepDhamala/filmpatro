package com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode;

import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodePurpose;
import com.deepdhamala.filmpatro.auth.common.OneTimeCodeEntity;
import com.deepdhamala.filmpatro.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPasswordResetCode extends OneTimeCodeEntity {

    @Column(nullable = false, unique = true)
    private String forgetPasswordResetCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public OneTimeCodePurpose getOneTimeCodePurpose() {
        return OneTimeCodePurpose.FORGET_PASSWORD_RESET;
    }
}
