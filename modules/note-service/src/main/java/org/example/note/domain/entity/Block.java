package org.example.note.domain.entity;

import java.time.LocalDateTime;
import java.util.Map;

import org.example.note.domain.enums.BlockType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

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
public class Block {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    private String title;

    private boolean isDefault;

    @Enumerated(EnumType.STRING)
    private BlockType type;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> content;

    private Integer position;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ==== 비즈니스 로직 ==== ( 유효성 검사 ?)
    public static Block create(Note note, String title, boolean isDefault, BlockType type, Map<String, Object> content, Integer position) {

        Block block = new Block();
        block.note = note;
        block.title = title;
        block.isDefault = isDefault;
        block.type = type;
        block.content = content;
        block.position = position;
        return block;
    }

    
    public void update(String title, BlockType type, Map<String, Object> content, Integer position) {

        this.title = title;
        this.type = type;
        this.content = content;
        this.position = position;
    }
}
