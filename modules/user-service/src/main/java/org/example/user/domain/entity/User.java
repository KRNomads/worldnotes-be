package org.example.user.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;

    private String name;

    private String email;

    private String provider;

    private String providerId;

    private LocalDateTime createdAt;

    private Boolean idDeleted;

    private LocalDateTime deletedAt;

}
