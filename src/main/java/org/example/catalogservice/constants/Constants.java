package org.example.catalogservice.constants;

public final class Constants {
    private Constants() {}
    // Restaurants
    public static final String RESTAURANT_CREATED = "Restaurant created successfully";
    public static final String RESTAURANT_ALREADY_EXISTS = "Restaurant already exists with same name and address";
    public static final String RESTAURANT_NOT_FOUND = "Restaurant not found with the given id";
    // Items
    public static final String ITEM_ADDED = "Item added to the menu successfully";
    public static final String ITEM_ALREADY_EXISTS = "Item already exists in the given restaurant";
    public static final String ITEM_NOT_FOUND = "Item not found in the restaurant";
    // Common
    public static final String FETCHED = "Fetched";
}