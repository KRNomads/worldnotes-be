package org.example.note.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.note.domain.enums.MemberRole;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Project {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> members = new ArrayList<>();

    private String title;

    private String overview;

    private String synopsis;

    private String genre;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ==== 비즈니스 로직 ====
    public static Project create(UUID userId, String name, String overview) {
        validate(name);

        Project project = new Project();
        project.title = name;
        project.overview = overview;
        project.addMember(userId, MemberRole.OWNER);
        return project;
    }

    public void updateTitle(String title) {

        this.title = title;
    }

    public void updateOverview(String overview) {

        this.overview = overview;
    }

    public void updateSynopsis(String synopsis) {

        this.synopsis = synopsis;
    }

    public void updateGenre(String genre) {

        this.genre = genre;
    }

    private static void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("프로젝트 이름은 필수입니다.");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("프로젝트 이름은 100자 이하로 입력해주세요.");
        }
    }

    public void addMember(UUID userId, MemberRole role) {
        ProjectMember member = new ProjectMember(this, userId, role);
        this.members.add(member);
    }

    public void removeMember(UUID userId) {
        members.removeIf(member -> member.getUserId().equals(userId));
    }

}
