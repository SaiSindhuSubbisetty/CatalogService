package org.example.catalogservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.catalogservice.dto.ItemRequest;
import org.example.catalogservice.exceptions.ItemAlreadyExistsException;
import org.example.catalogservice.exceptions.ItemNotFoundException;
import org.example.catalogservice.exceptions.RestaurantNotFoundException;
import org.example.catalogservice.services.ItemsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemsControllerTest {
    @MockBean
    private ItemsService itemsService;

    @Autowired
    private MockMvc mvc;

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
    public void testRandomUserAddItemsToRestaurantUnauthorized() throws Exception {
        ItemRequest request = ItemRequest.builder()
                .name("name")
                .price(200.00)
                .build();
        String restaurantId = "abc";
        String req = objectMapper.writeValueAsString(request);

        mvc.perform(post("/restaurants/" + restaurantId + "/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req)
        ).andExpect(status().isUnauthorized());
        verify(itemsService, never()).add(restaurantId, request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testRestaurantNotFoundWhileAddingTheItem_badRequest() throws Exception {
        ItemRequest request = ItemRequest.builder()
                .name("name")
                .price(200.00)
                .build();
        String restaurantId = "abc";
        String req = objectMapper.writeValueAsString(request);

        when(itemsService.add(restaurantId, request)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        mvc.perform(post("/restaurants/" + restaurantId + "/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req)
        ).andExpect(status().isBadRequest());
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
    public void test_restaurantNotFoundWhileFetchingAllItems_badRequest() throws Exception {
        String restaurantId = "abc";

        when(itemsService.fetchAll(restaurantId)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        mvc.perform(get("/restaurants/" + restaurantId + "/items")).andExpect(status().isBadRequest());
        verify(itemsService, times(1)).fetchAll(restaurantId);
    }

    @Test
    public void test_fetchItemByNameFromARestaurant_ok() throws Exception {
        String restaurantId = "id";
        String itemName = "name";

        when(itemsService.fetchByName(restaurantId, itemName)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mvc.perform(get("/restaurants/" + restaurantId + "/items/" + itemName)).andExpect(status().isOk());
        verify(itemsService, times(1)).fetchByName(restaurantId, itemName);
    }

    @Test
    public void test_cannotFindRestaurantWhileFetchingItem_badRequest() throws Exception {
        String restaurantId = "id";
        String itemName = "name";

        when(itemsService.fetchByName(restaurantId, itemName)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        mvc.perform(get("/restaurants/" + restaurantId + "/items/" + itemName)).andExpect(status().isBadRequest());
        verify(itemsService, times(1)).fetchByName(restaurantId, itemName);
    }

    @Test
    public void testCannotFindItemInRestaurant_badRequest() throws Exception {
        String restaurantId = "id";
        String itemName = "name";

        when(itemsService.fetchByName(restaurantId, itemName)).thenThrow(new ItemNotFoundException("Item not found"));

        mvc.perform(get("/restaurants/" + restaurantId + "/items/" + itemName)).andExpect(status().isBadRequest());
        verify(itemsService, times(1)).fetchByName(restaurantId, itemName);
    }

}