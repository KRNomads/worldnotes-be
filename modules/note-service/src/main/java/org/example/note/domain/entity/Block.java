package org.example.note.domain.entity;

import java.time.LocalDateTime;

import org.example.note.domain.enums.BlockType;
import org.example.note.domain.property.BlockProperties;
import org.example.note.domain.property.BlockPropertiesConverter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

    private boolean isDefault;

    private String fieldKey;

    @Enumerated(EnumType.STRING)
    private BlockType type;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = BlockPropertiesConverter.class)
    private BlockProperties properties;

    private Integer position;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ==== 비즈니스 로직 ==== ( 유효성 검사 ?)
    public static Block create(Note note, String title, boolean isDefault, String fieldKey, BlockType type, BlockProperties properties,
            Integer position) {

        Block block = new Block();
        block.note = note;
        block.title = title;
        block.isDefault = isDefault;
        block.fieldKey = fieldKey;
        block.type = type;
        block.properties = properties;
        block.position = position;
        return block;
    }

    public void updateTitle(String title) {

        this.title = title;
    }

    public void updateType(BlockType type) {

        this.type = type;
    }

    public void updateProperty(BlockProperties properties) {

        this.properties = properties;
    }

    public void updatePosition(Integer position) {

        this.position = position;
    }
}
