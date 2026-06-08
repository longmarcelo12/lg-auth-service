package com.example.lgauthservice.auth.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name="roles")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {
    private String code;

    private String name;
}
