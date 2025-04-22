# Catalog Service

The **Catalog Service** is part of a Smart Order Management System for cloud kitchens. It manages the information about restaurants and their food items. The service allows for the creation and retrieval of restaurants and menu items. It integrates with other microservices like the **Order Service**, **Payment Service**, and **Inventory Service**.

---

## ✨ Features

- 🏪 **Restaurant Management**: Create and retrieve restaurant details.
- 🍽️ **Menu Item Management**: Add and retrieve food items under specific restaurants.
- 📓 **Database**: PostgreSQL for persistent storage.
- ⚙️ **Service Discovery**: Registers with Eureka Server.
- 🔄 **Database Migrations**: Managed with Liquibase.
- 📆 **API Layer**: Built using Spring Boot REST controllers.

---

## 📁 Models

### 🏪 Restaurant
- `id` (UUID): Unique identifier
- `name`: Restaurant name
- `address`: Restaurant address
- `items`: List of associated food items

### 🍽️ Item
- `id` (UUID): Unique identifier
- `name`: Item name
- `description`: Item description
- `price`: Item price
- `restaurant`: Associated restaurant

---

## ⚙️ Configuration

### application.properties
```properties
# Service & Eureka
spring.application.name=catalog-service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
server.port=8081

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/catalogdb
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=1234
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Liquibase
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.yaml
```

---

## 🌐 API Endpoints

### 🔹 Restaurant API
| Method | Endpoint             | Description                |
|--------|----------------------|----------------------------|
| POST   | `/restaurants`       | Create a new restaurant    |
| GET    | `/restaurants`       | Get all restaurants        |
| GET    | `/restaurants/{id}`  | Get restaurant by ID       |

### 🔺 Item API
| Method | Endpoint                                             | Description                        |
|--------|------------------------------------------------------|------------------------------------|
| POST   | `/restaurants/{restaurantId}/items`                 | Add new item to a restaurant       |
| GET    | `/restaurants/{restaurantId}/items`                 | Get all items for a restaurant     |
| GET    | `/restaurants/{restaurantId}/items/{itemId}`        | Get item by ID                     |

---

## 🧰 Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- Jakarta Validation
- PostgreSQL
- Liquibase
- Eureka Discovery Client

---

## 🛠️ Running Locally

1. **Start Eureka Server** on `localhost:8761`
2. **Ensure PostgreSQL is running** and accessible
3. **Run the service**:
```bash
./mvnw spring-boot:run
```
4. Service will be available at: `http://localhost:8081`

---

## 📄 License
This project is licensed under the MIT License.

