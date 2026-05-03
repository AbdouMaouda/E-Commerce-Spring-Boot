# E-Commerce Backend API
A production-ready RESTful API built with Spring Boot supporting user registration, product catalog management, cart operations, and order lifecycle tracking.
---
## Features
- **RESTful API** — Full e-commerce backend supporting user registration, product catalog management, cart operations, and order lifecycle tracking
- **JWT Authentication** — Secure JWT-based authentication with role-based access control (RBAC) restricting sensitive operations to authorized roles only
- **Spring Security** — All endpoints protected with route-level authorization
- **JPA/Hibernate** — Complex domain relationships modeled with normalized PostgreSQL schemas ensuring ACID compliance
- **Exception Handling** — Global exception handling for clean and consistent API error responses
---
## Tech Stack
| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| Security | Spring Security, JWT |
| Database | PostgreSQL |
| ORM | JPA / Hibernate |
| Build Tool | Maven |
---
## Local Setup
### Prerequisites
- Java 17+
- PostgreSQL running locally
- Maven
### 1. Clone the repository
git clone [https://github.com/AbdouMaouda/ecommerce-backend.git](https://github.com/AbdouMaouda/E-Commerce-Spring-Boot)
cd ecommerce-backend
### 2. Set up environment variables
Create an application.properties file in src/main/resources/:
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=your_pg_username
spring.datasource.password=your_pg_password
jwt.secret=your_jwt_secret
jwt.expiration=86400000
### 3. Run the app
./mvnw spring-boot:run
The API will be available at http://localhost:8080
---
## Environment Variables
| Variable | Description |
|---|---|
| spring.datasource.url | PostgreSQL JDBC connection URL |
| spring.datasource.username | Database username |
| spring.datasource.password | Database password |
| jwt.secret | Secret key used to sign JWT tokens |
| jwt.expiration | Token expiration time in milliseconds |
---
## API Endpoints
### Auth
- POST /api/auth/register — Register a new user
- POST /api/auth/login — Login and receive a JWT token
### Products
- GET /api/products — Get all products
- GET /api/products/{id} — Get product by ID
- POST /api/products — Create a product (Admin only)
- PUT /api/products/{id} — Update a product (Admin only)
- DELETE /api/products/{id} — Delete a product (Admin only)
### Cart
- GET /api/cart — Get current user cart
- POST /api/cart — Add item to cart
- DELETE /api/cart/{itemId} — Remove item from cart
### Orders
- GET /api/orders — Get current user orders
- POST /api/orders — Place an order
- GET /api/orders/{id} — Get order by ID (Admin only)
---
## Project Structure
```
src/
└── main/java/com/ecommerce/
├── configuration/        — Security config, beans
├── controller/           — REST controllers
├── exceptions/           — Global exception handling
├── model/                — JPA entities
├── payload/              — Request and response DTOs
├── repositories/         — Spring Data JPA repositories
├── security/             — JWT filter, auth entry point
├── service/              — Business logic
├── util/                 — Utility classes
└── ECommerceApplication.java — Main entry point
```
---
## License
This project is licensed under the MIT License.
