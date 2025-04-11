package org.example.user.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class User {

    @Id
    private UUID id;

    private String name;

    private String email;

    private String provider;

    private String providerId;

    private LocalDateTime createdAt;

}
