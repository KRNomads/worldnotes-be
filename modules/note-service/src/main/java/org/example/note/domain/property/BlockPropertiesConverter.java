package org.example.note.domain.property;

import org.example.note.domain.enums.BlockType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BlockPropertiesConverter implements AttributeConverter<BlockProperties, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(BlockProperties attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting BlockProperties to JSON", e);
        }
    }

    @Override
    public BlockProperties convertToEntityAttribute(String dbData) {
        try {
            JsonNode node = objectMapper.readTree(dbData);
            String typeName = node.get("type").asText(); // JSON 내부에 "type" 포함되도록 함

            BlockType blockType = BlockType.valueOf(typeName.toUpperCase());
            Class<? extends BlockProperties> clazz = blockType.getPropertyClass();

            return objectMapper.treeToValue(node, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to BlockProperties", e);
        }
    }

}
