package org.example.note.domain.property;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphBlockProperties implements BlockProperties {

    private final String type = "PARAGRAPH";
    private Map<String, Object> content;

}
