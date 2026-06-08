package com.example.lgauthservice.auth.domain.entities;

import com.example.lgauthservice.auth.enums.Provider;
import com.example.lgauthservice.auth.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name="users", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_user_emai",
                columnNames = "email"
        )
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Column(name = "email", unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private long roleId;
}

