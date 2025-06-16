package com.deepdhamala.filmpatro.auth.token.accessToken;

import com.deepdhamala.filmpatro.auth.token.TokenPurpose;
import com.deepdhamala.filmpatro.auth.common.TokenEntity;
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
public class AccessToken extends TokenEntity {

    @Column(unique = true)
    private String accessToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Override
    public TokenPurpose getTokenPurpose() {
        return TokenPurpose.ACCESS;
    }
}
