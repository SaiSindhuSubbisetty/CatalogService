package org.example.catalogservice.services;


import lombok.RequiredArgsConstructor;
import org.example.catalogservice.dto.ApiResponse;
import org.example.catalogservice.dto.ItemRequest;
import org.example.catalogservice.dto.ItemResponse;
import org.example.catalogservice.exceptions.ItemAlreadyExistsException;
import org.example.catalogservice.exceptions.ItemNotFoundException;
import org.example.catalogservice.exceptions.RestaurantNotFoundException;
import org.example.catalogservice.models.Item;
import org.example.catalogservice.models.Restaurant;
import org.example.catalogservice.repositories.ItemsRepository;
import org.example.catalogservice.repositories.RestaurantsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.catalogservice.constants.Constants.FETCHED;
import static org.example.catalogservice.constants.Constants.ITEM_ADDED;

@Service
@RequiredArgsConstructor
public class ItemsService {
    private final ItemsRepository itemsRepository;
    private final RestaurantsRepository restaurantsRepository;

    public ResponseEntity<ApiResponse> add(String restaurantId, ItemRequest request) {
        Restaurant restaurant = restaurantsRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        if (itemsRepository.existsByNameAndRestaurant(request.getName(), restaurant)) {
            throw new ItemAlreadyExistsException("Item already exists in the given restaurant");
        }

        Item item = Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .restaurant(restaurant)
                .build();

        itemsRepository.save(item);

        ApiResponse response = ApiResponse.builder()
                .message(ITEM_ADDED)
                .status(HttpStatus.CREATED)
                .data(Map.of("item", new ItemResponse(item)))
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public ResponseEntity<ApiResponse> fetchAll(String restaurantId) {
        Restaurant restaurant = restaurantsRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        List<Item> items = itemsRepository.findAllByRestaurant(restaurant);
        List<ItemResponse> responses = new ArrayList<>();

        for (Item item : items) {
            responses.add(new ItemResponse(item));
        }

        ApiResponse response = ApiResponse.builder()
                .message(FETCHED)
                .status(HttpStatus.OK)
                .data(Map.of("items", responses))
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public ResponseEntity<ApiResponse> fetchById(String itemId) {
        Item item = itemsRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found"));

        ApiResponse response = ApiResponse.builder()
                .message(FETCHED)
                .status(HttpStatus.OK)
                .data(Map.of("item", new ItemResponse(item)))
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

}