package org.example.note.domain.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageBlockProperties implements BlockProperties {

    private final String type = "image";
    private String url;           // 이미지 URL
    private String caption;
}
