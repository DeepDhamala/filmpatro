package com.deepdhamala.filmpatro.user;

import com.deepdhamala.filmpatro.common.AccountEntity;
import com.deepdhamala.filmpatro.user.security.Role;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class User extends AccountEntity {

    private String fullName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    private String password;

    private String avatarUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

}