package com.danialechoes.phonebook.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PersonalContact.class, name = "PERSONAL"),
        @JsonSubTypes.Type(value = BusinessContact.class, name = "BUSINESS")
})
@Schema(description = "Contact entity representing a phonebook contact")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Contact {
    @Schema(description = "Unique identifier for the contact", example = "1")
    private Long id;
    @Schema(description = "Name of the contact", example = "John Doe")
    private String name;
    @Schema(description = "Phone number of the contact", example = "+1234567890")
    private String phoneNumber;
    private ContactType type;
}
