package org.example.note.domain.property;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagBlockProperties implements BlockProperties {

    private final String type = "tags";
    private List<String> tags;
}
