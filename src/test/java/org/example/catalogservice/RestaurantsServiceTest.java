package org.example.catalogservice;

import org.example.catalogservice.dto.Address;
import org.example.catalogservice.dto.ApiResponse;
import org.example.catalogservice.dto.RestaurantRequest;
import org.example.catalogservice.exceptions.RestaurantAlreadyExistsException;
import org.example.catalogservice.exceptions.RestaurantNotFoundException;
import org.example.catalogservice.models.Restaurant;
import org.example.catalogservice.repositories.RestaurantsRepository;
import org.example.catalogservice.services.RestaurantsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


import static org.example.catalogservice.constants.Constants.FETCHED;
import static org.example.catalogservice.constants.Constants.RESTAURANT_CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RestaurantsServiceTest {
    @Mock
    private RestaurantsRepository restaurantsRepository;

    @InjectMocks
    private RestaurantsService restaurantsService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    public void testRestaurantIsCreatedSuccessfully() {
        RestaurantRequest request = RestaurantRequest.builder()
                .name("restaurant")
                .address(mock(Address.class))
                .build();
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantsRepository.existsByNameAndAddress(request.getName(), request.getAddress())).thenReturn(false);
        when(restaurantsRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        ResponseEntity<ApiResponse> response = restaurantsService.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(RESTAURANT_CREATED, Objects.requireNonNull(response.getBody()).getMessage());

        verify(restaurantsRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    public void testRestaurantWithSameNameAlreadyExistsInSameAddress_throwsException() {
        RestaurantRequest request = RestaurantRequest.builder()
                .name("restaurant")
                .address(mock(Address.class))
                .build();

        when(restaurantsRepository.existsByNameAndAddress(request.getName(), request.getAddress())).thenReturn(true);

        assertThrows(RestaurantAlreadyExistsException.class, () -> restaurantsService.create(request));

        verify(restaurantsRepository, never()).save(any(Restaurant.class));
    }

    @Test
    public void testFetchAllRestaurants() {
        Restaurant firstRestaurant = mock(Restaurant.class);
        Restaurant secondRestaurant = mock(Restaurant.class);
        Restaurant thirdRestaurant = mock(Restaurant.class);
        List<Restaurant> restaurants = List.of(firstRestaurant, secondRestaurant, thirdRestaurant);

        when(restaurantsRepository.findAll()).thenReturn(restaurants);
        ResponseEntity<ApiResponse> response = restaurantsService.fetchAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FETCHED, Objects.requireNonNull(response.getBody()).getMessage());

        verify(restaurantsRepository, times(1)).findAll();
    }

    @Test
    public void testFetchRestaurantByIdSuccessfully() {
        Restaurant restaurant = mock(Restaurant.class);
        String restaurantId = "id";

        when(restaurantsRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        ResponseEntity<ApiResponse> response = restaurantsService.fetchById(restaurantId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FETCHED, Objects.requireNonNull(response.getBody()).getMessage());
        verify(restaurantsRepository, times(1)).findById(restaurantId);
    }

    @Test
    public void testRestaurantNotFoundWhileFetchingById_throwsException() {
        String restaurantId = "id";

        when(restaurantsRepository.findById(restaurantId)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        assertThrows(RestaurantNotFoundException.class, () -> restaurantsService.fetchById(restaurantId));
        verify(restaurantsRepository, times(1)).findById(restaurantId);
    }
}
