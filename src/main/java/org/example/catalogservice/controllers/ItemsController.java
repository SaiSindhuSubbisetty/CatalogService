package org.example.catalogservice.controllers;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;


import org.example.catalogservice.dto.ApiResponse;
import org.example.catalogservice.dto.ItemRequest;
import org.example.catalogservice.dto.ItemResponse;
import org.example.catalogservice.services.ItemsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants/{restaurantId}/items")
public class ItemsController {
    private final ItemsService itemsService;

    @PostMapping
    public ResponseEntity<ApiResponse> add(@PathVariable(name = "restaurantId") String restaurantId, @Valid @RequestBody ItemRequest request) {
        return this.itemsService.add(restaurantId, request);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> fetchAll(@PathVariable(name = "restaurantId") String restaurantId) {
        return this.itemsService.fetchAll(restaurantId);
    }

    @GetMapping("/{itemName}")
    public ResponseEntity<ApiResponse> fetchByItemName(@PathVariable(name = "restaurantId") String restaurantId, @PathVariable(name = "itemName") String itemName) {
        return this.itemsService.fetchByName(restaurantId, itemName);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponse> fetchById(@PathVariable(name = "itemId") String itemId) {
        return this.itemsService.fetchById(itemId);
    }

}