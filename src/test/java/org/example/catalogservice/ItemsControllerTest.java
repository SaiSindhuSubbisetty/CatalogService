package org.example.catalogservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.catalogservice.controllers.ItemsController;
import org.example.catalogservice.dto.ItemRequest;
import org.example.catalogservice.exceptions.GlobalExceptionHandler;
import org.example.catalogservice.exceptions.ItemAlreadyExistsException;
import org.example.catalogservice.exceptions.ItemNotFoundException;
import org.example.catalogservice.exceptions.RestaurantNotFoundException;
import org.example.catalogservice.services.ItemsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemsController.class)
@Import(SecurityConfigTest.class)
class ItemsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemsService itemsService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        reset(itemsService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddItemsToRestaurantCreated() throws Exception {
        ItemRequest request = ItemRequest.builder()
                .name("name")
                .price(200.00)
                .build();
        String restaurantId = "abc";
        String req = objectMapper.writeValueAsString(request);

        when(itemsService.add(restaurantId, request)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        mvc.perform(post("/restaurants/" + restaurantId + "/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req)
        ).andExpect(status().isCreated());
        verify(itemsService, times(1)).add(restaurantId, request);
    }

    @Test
    void testRestaurantNotFoundWhileAddingTheItem() throws Exception {
        ItemRequest request = ItemRequest.builder()
                .name("name")
                .price(200.00)
                .build();
        String restaurantId = "abc";
        String req = objectMapper.writeValueAsString(request);

        doThrow(new RestaurantNotFoundException("Restaurant not found")).when(itemsService).add(restaurantId, request);

        mvc.perform(post("/restaurants/" + restaurantId + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isBadRequest());
        verify(itemsService, times(1)).add(restaurantId, request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test_itemAlreadyPresentInRestaurant_badRequest() throws Exception {
        ItemRequest request = ItemRequest.builder()
                .name("name")
                .price(200.00)
                .build();
        String restaurantId = "abc";
        String req = objectMapper.writeValueAsString(request);

        when(itemsService.add(restaurantId, request)).thenThrow(new ItemAlreadyExistsException("Item already exists"));

        mvc.perform(post("/restaurants/" + restaurantId + "/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req)
        ).andExpect(status().isBadRequest());
        verify(itemsService, times(1)).add(restaurantId, request);
    }

    @Test
    public void test_fetchAllItemsByRestaurant_ok() throws Exception {
        String restaurantId = "abc";

        when(itemsService.fetchAll(restaurantId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mvc.perform(get("/restaurants/" + restaurantId + "/items")).andExpect(status().isOk());
        verify(itemsService, times(1)).fetchAll(restaurantId);
    }

    @Test
    void testRestaurantNotFoundWhileFetchingAllItems() throws Exception {
        String restaurantId = "abc";

        doThrow(new RestaurantNotFoundException("Restaurant not found")).when(itemsService).fetchAll(restaurantId);

        mvc.perform(get("/restaurants/" + restaurantId + "/items"))
                .andExpect(status().isBadRequest());
        verify(itemsService, times(1)).fetchAll(restaurantId);
    }

    @Test
    void testFetchItemById() throws Exception {
        String itemId = "item123";

        mvc.perform(get("/restaurants/abc/items/" + itemId))
                .andExpect(status().isOk());
        verify(itemsService, times(1)).fetchById(itemId);
    }

    @Test
    void testItemNotFoundWhileFetchingById() throws Exception {
        String itemId = "item123";

        doThrow(new ItemNotFoundException("Item not found")).when(itemsService).fetchById(itemId);

        mvc.perform(get("/restaurants/abc/items/" + itemId))
                .andExpect(status().isBadRequest());
        verify(itemsService, times(1)).fetchById(itemId);
    }

    @Test
    void test_cannotFindRestaurantWhileFetchingItem_badRequest() throws Exception {
        String restaurantId = "abc";
        String itemId = "item123";

        doThrow(new RestaurantNotFoundException("Restaurant not found")).when(itemsService).fetchById(itemId);

        mvc.perform(get("/restaurants/" + restaurantId + "/items/" + itemId))
                .andExpect(status().isBadRequest());
        verify(itemsService, times(1)).fetchById(itemId);
    }

    @Test
    void testCannotFindItemInRestaurant_badRequest() throws Exception {
        String itemId = "item123";

        doThrow(new ItemNotFoundException("Item not found")).when(itemsService).fetchById(itemId);

        mvc.perform(get("/restaurants/abc/items/" + itemId))
                .andExpect(status().isBadRequest());
        verify(itemsService, times(1)).fetchById(itemId);
    }

    @Test
    void test_restaurantNotFoundWhileFetchingAllItems_badRequest() throws Exception {
        String restaurantId = "abc";

        doThrow(new RestaurantNotFoundException("Restaurant not found")).when(itemsService).fetchAll(restaurantId);

        mvc.perform(get("/restaurants/" + restaurantId + "/items"))
                .andExpect(status().isBadRequest());
        verify(itemsService, times(1)).fetchAll(restaurantId);
    }
}