package org.example.note.domain.entity;

import java.time.LocalDateTime;

import org.example.note.domain.enums.BlockType;
import org.example.note.domain.property.BlockProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
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
public class Block {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    private String title;

    private String fieldKey;

    @Enumerated(EnumType.STRING)
    private BlockType type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private BlockProperties properties;

    @Column(nullable = false)
    private Boolean isCollapsed = false;

    private Integer position;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ==== 비즈니스 로직 ==== ( 유효성 검사 ?)
    public static Block create(Note note, String title, String fieldKey, BlockType type, BlockProperties properties,
            Integer position) {

        Block block = new Block();
        block.note = note;
        block.title = title;
        block.fieldKey = fieldKey;
        block.type = type;
        block.properties = properties;
        block.position = position;
        block.isCollapsed = false;
        return block;
    }

    // 기본 블록 여부
    public boolean isDefault() {
        return fieldKey != null && !fieldKey.isBlank();
    }

    public void updateTitle(String title) {

        this.title = title;
    }

    public void updateTypeAndProperty(BlockType type, BlockProperties properties) {

        this.type = type;
        this.properties = properties;
    }

    public void updateIsCollapsed(Boolean isCollapsed) {

        this.isCollapsed = isCollapsed;
    }

    public void updatePosition(Integer position) {

        this.position = position;
    }
}
