package org.example.note.domain.property;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// @JsonTypeInfo(
//         use = JsonTypeInfo.Id.NAME,
//         include = JsonTypeInfo.As.PROPERTY,
//         property = "type"
// )
// @JsonSubTypes({
//     @JsonSubTypes.Type(value = TextBlockProperties.class, name = "TEXT"),
//     @JsonSubTypes.Type(value = ImageBlockProperties.class, name = "IMAGE"),
//     @JsonSubTypes.Type(value = ImageBlockProperties.class, name = "TAGS"), // 필요시 추가
// })
public interface BlockProperties {

}
