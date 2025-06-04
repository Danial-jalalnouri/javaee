package com.danialechoes.customerSystem.dto;

import com.danialechoes.customerSystem.model.CustomerType;
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
        @JsonSubTypes.Type(value = RealCustomerDto.class, name = "REAL"),
        @JsonSubTypes.Type(value = LegalCustomerDto.class, name = "LEGAL")
})
@Schema(description = "Customer entity representing a customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class CustomerDto {
    @Schema(description = "Unique identifier for the customer", example = "1")
    private Long id;
    @Schema(description = "Name of the customer", example = "John Doe")
    private String name;
    @Schema(description = "Phone number of the customer", example = "+1234567890")
    private String phoneNumber;
    private CustomerType type;
}
