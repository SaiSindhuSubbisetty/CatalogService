package org.example.catalogservice;

import org.example.catalogservice.dto.Address;

import org.example.catalogservice.models.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = Restaurant.builder()
                .id("restaurant-id")
                .name("Test Restaurant")
                .address(new Address())
                .items(Collections.emptyList())
                .build();
    }

    @Test
    void testRestaurantCreation() {
        assertThat(restaurant.getId()).isEqualTo("restaurant-id");
        assertThat(restaurant.getName()).isEqualTo("Test Restaurant");
        assertThat(restaurant.getAddress()).isNotNull();
        assertThat(restaurant.getItems()).isNotNull();
    }

    @Test
    void testRestaurantBuilder() {
        Restaurant builtRestaurant = Restaurant.builder()
                .name("Built Restaurant")
                .address(new Address())
                .build();

        assertThat(builtRestaurant.getName()).isEqualTo("Built Restaurant");
        assertThat(builtRestaurant.getAddress()).isNotNull();
    }
}