package org.example.note.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Project {

    @Id
    @GeneratedValue
    private UUID id;

    // FK
    private UUID userId;

    private String name;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ==== 비즈니스 로직 ====
    public static Project create(UUID userId, String name, String description) {
        validate(name);

        Project project = new Project();
        project.userId = userId;
        project.name = name;
        project.description = description;
        return project;
    }

    public void update(String name, String description) {
        validate(name);
        this.name = name;
        this.description = description;
    }

    private static void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("프로젝트 이름은 필수입니다.");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("프로젝트 이름은 100자 이하로 입력해주세요.");
        }
    }

}
