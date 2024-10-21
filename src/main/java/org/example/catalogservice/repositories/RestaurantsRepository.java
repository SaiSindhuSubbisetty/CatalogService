package org.example.catalogservice.repositories;

import org.example.catalogservice.dto.Address;
import org.example.catalogservice.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantsRepository extends JpaRepository<Restaurant, String> {
    boolean existsByNameAndAddress(String name, Address address);
}