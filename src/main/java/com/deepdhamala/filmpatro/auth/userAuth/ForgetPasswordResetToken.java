package com.deepdhamala.filmpatro.auth.userAuth;

import com.deepdhamala.filmpatro.common.BaseEntity;
import com.deepdhamala.filmpatro.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordResetToken extends BaseEntity {

    private String forgetPasswordResetToken;

    private LocalDateTime expiryDate;

    @ManyToOne
    private User user;

    private boolean isUsed = false;
}
