package org.example.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import lombok.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @NonNull
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @Min(value = 100, message = "Price must be at least 100 rupees")
    private double price;
}
