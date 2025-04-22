# Catalog Service

The **Catalog Service** is part of a food ordering system. It manages the information about restaurants and their food items. The service allows for the creation, updating, and retrieval of restaurants and food items, and integrates with other services like the **Order Service** for a seamless experience. 

## Features
- **Restaurant Management**: Create, update, and manage restaurant details.
- **Food Item Management**: Manage food items associated with restaurants, including their name, description, and price.
- **Database**: Uses **PostgreSQL** for persistent storage of restaurant and food item data.
- **API Documentation**: Automatically generates API documentation using **Swagger** for easy testing and usage.
- **Database Migrations**: Uses **Liquibase** for version-controlled database schema updates.

## Models

### Item
- Represents a food item in the catalog.
- Fields:
  - `id` (UUID): Unique identifier for the item.
  - `name`: Name of the food item.
  - `description`: Description of the food item.
  - `price`: Price of the food item.
  - `restaurant`: The restaurant to which the item belongs.

### Restaurant
- Represents a restaurant in the catalog.
- Fields:
  - `id` (UUID): Unique identifier for the restaurant.
  - `name`: Name of the restaurant.
  - `address`: Address of the restaurant.
  - `items`: List of food items associated with the restaurant.

## Setup

### Prerequisites
- Java 17 or later
- Spring Boot 2.x
- PostgreSQL database
- Maven or Gradle

### Steps to Run

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/SaiSindhuSubbisetty/catalog-service.git
   cd catalog-service
   ```

2. **Configure Database**:
   - Create a PostgreSQL database (e.g., `catalog_db`).
   - Update the `application.properties` or `application.yml` with your database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/catalog_db
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Run Liquibase**:
   Liquibase will automatically apply database migrations on startup to ensure the database schema is up to date.

4. **Build the Application**:
   ```bash
   mvn clean install
   ```

5. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

6. **Access the Swagger UI**:
   After the application is running, access the API documentation at:
   ```bash
   http://localhost:8080/swagger-ui.html
   ```

## API Endpoints

### Restaurant API
- **GET** `/restaurants`: Get a list of all restaurants.
- **POST** `/restaurants`: Create a new restaurant.
- **GET** `/restaurants/{id}`: Get details of a specific restaurant by ID.
- **PUT** `/restaurants/{id}`: Update a restaurant.
- **DELETE** `/restaurants/{id}`: Delete a restaurant.

### Item API
- **GET** `/items`: Get a list of all food items.
- **POST** `/items`: Create a new food item.
- **GET** `/items/{id}`: Get details of a specific food item by ID.
- **PUT** `/items/{id}`: Update a food item.
- **DELETE** `/items/{id}`: Delete a food item.

## Tech Stack
- **Java 17**: Programming language.
- **Spring Boot**: Framework for building the service.
- **PostgreSQL**: Database for storing restaurant and food item data.
- **Liquibase**: Database version control for schema management.
- **Swagger**: For API documentation and testing.

## License
This project is licensed under the MIT License.
