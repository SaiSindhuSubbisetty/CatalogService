package org.example.catalogservice;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.catalogservice.dto.Address;
import org.example.catalogservice.dto.RestaurantRequest;
import org.example.catalogservice.exceptions.RestaurantAlreadyExistsException;
import org.example.catalogservice.exceptions.RestaurantNotFoundException;
import org.example.catalogservice.services.RestaurantsService;
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
class RestaurantsControllerTest {
    @MockBean
    private RestaurantsService restaurantsService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testRestaurantIsCreated() throws Exception {
        Address address = Address.builder()
                .buildingNumber(2)
                .city("abc")
                .state("def")
                .country("ssw")
                .locality("sdw")
                .street("we")
                .zipcode("600001")
                .build();
        RestaurantRequest request = RestaurantRequest.builder()
                .name("name")
                .address(address)
                .build();
        String req = objectMapper.writeValueAsString(request);

        when(restaurantsService.create(request)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        mvc.perform(post("/restaurants")
                .content(req)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
        verify(restaurantsService, times(1)).create(request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testRestaurantWithSameNameAlreadyExistsInSameAddress_badRequest() throws Exception {
        Address address = Address.builder()
                .buildingNumber(2)
                .city("abc")
                .state("def")
                .country("ssw")
                .locality("sdw")
                .street("we")
                .zipcode("600001")
                .build();
        RestaurantRequest request = RestaurantRequest.builder()
                .name("name")
                .address(address)
                .build();
        String req = objectMapper.writeValueAsString(request);

        when(restaurantsService.create(request)).thenThrow(new RestaurantAlreadyExistsException("Restaurant already exists"));

        mvc.perform(post("/restaurants")
                .content(req)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(restaurantsService, times(1)).create(request);
    }

    @Test
    public void test_fetchAllRestaurants() throws Exception {
        when(restaurantsService.fetchAll()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mvc.perform(get("/restaurants")).andExpect(status().isOk());
        verify(restaurantsService, times(1)).fetchAll();
    }

    @Test
    public void test_fetchRestaurantById_ok() throws Exception {
        String restaurantId = "abc";

        when(restaurantsService.fetchById(restaurantId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mvc.perform(get("/restaurants/" + restaurantId)).andExpect(status().isOk());
        verify(restaurantsService, times(1)).fetchById(restaurantId);
    }

    @Test
    public void test_restaurantNotFoundWhileFetchingById_badRequest() throws Exception {
        String restaurantId = "abc";

        when(restaurantsService.fetchById(restaurantId)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        mvc.perform(get("/restaurants/" + restaurantId)).andExpect(status().isBadRequest());
        verify(restaurantsService, times(1)).fetchById(restaurantId);
    }

    @Test
    public void testFetchRestaurantThatDoesNotExist() throws Exception {
        String restaurantId = "non-existent-id";

        when(restaurantsService.fetchById(restaurantId)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        mvc.perform(get("/restaurants/" + restaurantId)).andExpect(status().isBadRequest());
        verify(restaurantsService, times(1)).fetchById(restaurantId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testMethodArgumentNotValid() throws Exception {

        Address address = Address.builder()
                .buildingNumber(0)
                .city("abc")
                .state("def")
                .country("ssw")
                .locality("sdw")
                .street("we")
                .zipcode("600001")
                .build();
        RestaurantRequest request = RestaurantRequest.builder()
                .name("")
                .address(address)
                .build();
        String req = objectMapper.writeValueAsString(request);

        mvc.perform(post("/restaurants")
                .content(req)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
    @Test
    public void testFetchAllRestaurantsEmpty() throws Exception {
        when(restaurantsService.fetchAll()).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        mvc.perform(get("/restaurants"))
                .andExpect(status().isNoContent());
        verify(restaurantsService, times(1)).fetchAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateRestaurantWithComplexAddress() throws Exception {
        Address address = new Address(2, "abc", "def", "ssw", "sdw", "complex street", "600001");
        RestaurantRequest request = new RestaurantRequest("complex name", address);
        String req = objectMapper.writeValueAsString(request);

        when(restaurantsService.create(request)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        mvc.perform(post("/restaurants")
                .content(req)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
        verify(restaurantsService, times(1)).create(request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testHttpMessageNotReadableException() throws Exception {
        String invalidJson = "{ \"name\": \"name\", \"address\": { \"buildingNumber\": 2, \"city\": \"abc\" }";

        mvc.perform(post("/restaurants")
                .content(invalidJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(restaurantsService, never()).create(any());
    }

}
