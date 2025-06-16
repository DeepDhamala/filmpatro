package com.deepdhamala.filmpatro.auth.oneTimeCode.authorizationCode;

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
public class AuthorizationCode extends OneTimeCodeEntity {

    @Column(unique = true)
    private String authorizationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Override
    public OneTimeCodePurpose getOneTimeCodePurpose() {
        return OneTimeCodePurpose.AUTHORIZATION_CODE;
    }
}
