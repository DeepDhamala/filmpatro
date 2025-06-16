package com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode;

import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodePurpose;
import com.deepdhamala.filmpatro.auth.common.OneTimeCodeEntity;
import com.deepdhamala.filmpatro.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationCode extends OneTimeCodeEntity {

    @Column(nullable = false, unique = true)
    private String emailVerificationToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public OneTimeCodePurpose getOneTimeCodePurpose() {
        return OneTimeCodePurpose.EMAIL_VERIFICATION;
    }
}