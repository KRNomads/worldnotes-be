package org.example.note.domain.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RichTextElement {

    private String text;

    private boolean bold;
    private boolean italic;
    private boolean underline;

    private String color;         // 텍스트 색상 (예: "#FF0000")
    private String backgroundColor; // 배경 색상 (예: "#FFFF00")
}
