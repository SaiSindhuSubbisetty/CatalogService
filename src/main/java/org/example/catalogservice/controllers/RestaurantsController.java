package org.example.catalogservice.controllers;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.example.catalogservice.dto.ApiResponse;
import org.example.catalogservice.dto.RestaurantRequest;
import org.example.catalogservice.services.RestaurantsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantsController {
    private final RestaurantsService restaurantsService;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody RestaurantRequest request) {
        return this.restaurantsService.create(request);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> fetchAll() {
        return this.restaurantsService.fetchAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> fetchById(@PathVariable(value = "id") String id) {
        return this.restaurantsService.fetchById(id);
    }
}
