package org.example.note.domain.entity;

import java.util.UUID;

import org.example.note.domain.enums.MemberRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class ProjectMember {

    @Id
    @GeneratedValue
    private Long id;

    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    // ==== 비즈니스 로직 ====
    public ProjectMember(Project project, UUID userId, MemberRole role) {
        this.project = project;
        this.userId = userId;
        this.role = role;
    }

}
