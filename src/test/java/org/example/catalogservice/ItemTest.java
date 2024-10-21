package org.example.catalogservice;

import org.example.catalogservice.dto.Address;
import org.example.catalogservice.models.Item;
import org.example.catalogservice.models.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

    private Item item;

    @BeforeEach
    void setUp() {
        Restaurant restaurant = Restaurant.builder()
                .id("restaurant-id")
                .name("Test Restaurant")
                .address(new Address())
                .build();

        item = Item.builder()
                .id("item-id")
                .name("Test Item")
                .description("Test Description")
                .price(9.99)
                .restaurant(restaurant)
                .build();
    }

    @Test
    void testItemCreation() {
        assertThat(item.getId()).isEqualTo("item-id");
        assertThat(item.getName()).isEqualTo("Test Item");
        assertThat(item.getDescription()).isEqualTo("Test Description");
        assertThat(item.getPrice()).isEqualTo(9.99);
        assertThat(item.getRestaurant()).isNotNull();
    }

    @Test
    void testItemBuilder() {
        Item builtItem = Item.builder()
                .name("Built Item")
                .price(19.99)
                .build();

        assertThat(builtItem.getName()).isEqualTo("Built Item");
        assertThat(builtItem.getPrice()).isEqualTo(19.99);
    }

}