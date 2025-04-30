package org.example.note.domain.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextBlockProperties implements BlockProperties {

    private final String type = "text";
    private String value;
}
