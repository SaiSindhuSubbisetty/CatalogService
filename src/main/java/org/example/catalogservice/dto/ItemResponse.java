package org.example.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.catalogservice.models.Item;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private String id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    private String restaurantId;
    private double price;
    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.restaurantId = item.getRestaurant().getId();
    }
}