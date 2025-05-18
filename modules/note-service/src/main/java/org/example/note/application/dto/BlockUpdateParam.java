package org.example.note.application.dto;

import java.util.Map;

import org.example.common.exception.ErrorCode;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.exception.BlockExeption;
import org.example.note.domain.property.BlockProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public record BlockUpdateParam(
        String title,
        BlockType type,
        Map<String, Object> properties
        ) {

    public BlockProperties parseProperties(ObjectMapper objectMapper) {
        Class<? extends BlockProperties> propertyClass = type.getPropertyClass();

        ObjectNode node = objectMapper.convertValue(properties, ObjectNode.class);
        node.put("type", type.name());

        try {
            return objectMapper.convertValue(node, propertyClass);
        } catch (IllegalArgumentException e) {
            throw new BlockExeption(ErrorCode.INVALID_PROPERTIES, type);
        }
    }
}
