package org.example.note.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.note.domain.enums.NoteType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Note {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private String title;

    private String subTitle;

    private String summary;

    private String imgUrl;

    private String color;

    @Enumerated(EnumType.STRING)
    private NoteType type;

    private Integer position;

    // 블록들
    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Block> blocks = new ArrayList<>();

    // 태그들
    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoteTag> noteTags = new ArrayList<>();

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

    public void updateTitle(String title) {

        this.title = title;
    }

    public void updateSubTitle(String subTitle) {

        this.subTitle = subTitle;
    }

    public void updateSummary(String summary) {

        this.summary = summary;
    }

    public void updateImgUrl(String imgUrl) {

        this.imgUrl = imgUrl;
    }

    public void updateColor(String color) {

        this.color = color;
    }

    public void updatePosition(Integer position) {

        this.position = position;
    }

}
