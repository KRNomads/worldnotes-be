package org.example.note.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.example.note.domain.enums.NoteType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Note {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private String title;

    @Enumerated(EnumType.STRING)
    private NoteType type;

    private Integer position;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ==== 비즈니스 로직 ==== ( 유효성 검사 ?)
    public static Note create(Project project, String title, NoteType type, Integer position) {

        Note note = new Note();
        note.project = project;
        note.title = title;
        note.type = type;
        note.position = position;
        return note;
    }

    public void update(String title) {

        this.title = title;
    }

    public void updatePosition(Integer position) {

        this.position = position;
    }

}
