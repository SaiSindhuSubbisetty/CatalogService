package org.example.catalogservice;

import org.example.catalogservice.dto.ApiResponse;
import org.example.catalogservice.dto.ItemRequest;
import org.example.catalogservice.exceptions.ItemAlreadyExistsException;
import org.example.catalogservice.exceptions.ItemNotFoundException;
import org.example.catalogservice.exceptions.RestaurantNotFoundException;
import org.example.catalogservice.models.Item;
import org.example.catalogservice.models.Restaurant;
import org.example.catalogservice.repositories.ItemsRepository;
import org.example.catalogservice.repositories.RestaurantsRepository;
import org.example.catalogservice.services.ItemsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.example.catalogservice.constants.Constants.FETCHED;
import static org.example.catalogservice.constants.Constants.ITEM_ADDED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ItemsServiceTest {
    @Mock
    private ItemsRepository itemsRepository;

    @Mock
    private RestaurantsRepository restaurantsRepository;

    @InjectMocks
    private ItemsService itemsService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    public void testAddItemsToRestaurantSuccessfully() {
        ItemRequest request = ItemRequest.builder()
                .name("item")
                .price(200.0)
                .build();
        String restaurantId = "abc";
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantsRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(itemsRepository.existsByNameAndRestaurant("item", restaurant)).thenReturn(false);
        ResponseEntity<ApiResponse> response = itemsService.add(restaurantId, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ITEM_ADDED, Objects.requireNonNull(response.getBody()).getMessage());

        verify(restaurantsRepository, times(1)).findById(restaurantId);
        verify(itemsRepository, times(1)).existsByNameAndRestaurant("item", restaurant);
        verify(itemsRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testExceptionRestaurantNotFoundWhileAddingTheItem() {
        ItemRequest request = ItemRequest.builder()
                .name("item")
                .price(200.0)
                .build();
        String restaurantId = "abc";
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantsRepository.findById(restaurantId)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        assertThrows(RestaurantNotFoundException.class, () -> itemsService.add(restaurantId, request));

        verify(restaurantsRepository, times(1)).findById(restaurantId);
        verify(itemsRepository, never()).existsByNameAndRestaurant("item", restaurant);
        verify(itemsRepository, never()).save(any(Item.class));
    }

    @Test
    public void testExceptionitemAlreadyPresentInRestaurant() {
        ItemRequest request = ItemRequest.builder()
                .name("item")
                .price(200.0)
                .build();
        String restaurantId = "abc";
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantsRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(itemsRepository.existsByNameAndRestaurant("item", restaurant)).thenReturn(true);

        assertThrows(ItemAlreadyExistsException.class, () -> itemsService.add(restaurantId, request));

        verify(restaurantsRepository, times(1)).findById(restaurantId);
        verify(itemsRepository, times(1)).existsByNameAndRestaurant("item", restaurant);
        verify(itemsRepository, never()).save(any(Item.class));
    }

    @Test
    public void testFetchAllItemsByRestaurantSuccessfully() {
        String restaurantId = "abc";
        Restaurant restaurant = mock(Restaurant.class);
        Item firstItem = mock(Item.class);
        Item secondItem = mock(Item.class);
        List<Item> items = List.of(firstItem, secondItem);

        when(restaurantsRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(itemsRepository.findAllByRestaurant(restaurant)).thenReturn(items);
        when(firstItem.getRestaurant()).thenReturn(restaurant);
        when(secondItem.getRestaurant()).thenReturn(restaurant);
        ResponseEntity<ApiResponse> response = itemsService.fetchAll(restaurantId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FETCHED, Objects.requireNonNull(response.getBody()).getMessage());

        verify(restaurantsRepository, times(1)).findById(restaurantId);
        verify(itemsRepository, times(1)).findAllByRestaurant(restaurant);
    }

    @Test
    public void testRestaurantNotFoundWhileFetchingAllItems_throwsException() {
        String restaurantId = "abc";

        when(restaurantsRepository.findById(restaurantId)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        assertThrows(RestaurantNotFoundException.class, () -> itemsService.fetchAll(restaurantId));

        verify(restaurantsRepository, times(1)).findById(restaurantId);
        verify(itemsRepository, never()).findAllByRestaurant(any(Restaurant.class));
    }

    @Test
    public void test_cannotFindRestaurantWhileFetchingItem_badRequest() {
        String itemId = "item123";

        when(itemsRepository.findById(itemId)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        assertThrows(RestaurantNotFoundException.class, () -> itemsService.fetchById(itemId));

        verify(itemsRepository, times(1)).findById(itemId);
    }

    @Test
    public void testCannotFindItemInRestaurant_badRequest() {
        String itemId = "item123";

        when(itemsRepository.findById(itemId)).thenThrow(new ItemNotFoundException("Item not found"));

        assertThrows(ItemNotFoundException.class, () -> itemsService.fetchById(itemId));

        verify(itemsRepository, times(1)).findById(itemId);
    }

    @Test
    public void test_restaurantNotFoundWhileFetchingAllItems_badRequest() {
        String restaurantId = "abc";

        when(restaurantsRepository.findById(restaurantId)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        assertThrows(RestaurantNotFoundException.class, () -> itemsService.fetchAll(restaurantId));

        verify(restaurantsRepository, times(1)).findById(restaurantId);
        verify(itemsRepository, never()).findAllByRestaurant(any(Restaurant.class));
    }

}