package org.example.note.domain.enums;

import org.example.note.domain.property.BlockProperties;
import org.example.note.domain.property.ImageBlockProperties;
import org.example.note.domain.property.TagBlockProperties;
import org.example.note.domain.property.TextBlockProperties;

public enum BlockType {
    TEXT(TextBlockProperties.class),
    TAGS(TagBlockProperties.class),
    IMAGE(ImageBlockProperties.class);

    private final Class<? extends BlockProperties> propertyClass;

    BlockType(Class<? extends BlockProperties> clazz) {
        this.propertyClass = clazz;
    }

    public Class<? extends BlockProperties> getPropertyClass() {
        return propertyClass;
    }
}
