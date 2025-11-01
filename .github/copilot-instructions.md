# AI Agent Instructions for Cafe Management System

## Project Overview
This is a Spring Boot-based cafe management system with features for menu management, order processing, and real-time suggestions using WebSocket. The application follows a multi-tier architecture with clear separation of concerns.

## Key Technologies & Dependencies
- Java 17+
- Spring Boot (Web, Security, WebSocket, JPA)
- MySQL 8.0+
- Maven 3.8+
- WebSocket for real-time suggestions
- Google API integration for analytics

## Architecture Patterns
### Authentication & Authorization
- Custom user authentication implemented in `config/SecurityConfig.java`
- Role-based access control (Admin vs Customer)
- Success handler in `config/CustomAuthenticationSuccessHandler.java` for role-specific redirects

### Real-time Communication
- WebSocket configuration in `config/WebSocketConfig.java`
- Topic-based messaging using `/topic/suggestions` channel
- SockJS fallback support for older browsers

### Data Flow
1. DTOs (`dto/` package) for data transfer between layers
2. Controllers (`controller/` package) handle HTTP requests
3. Services (`service/` package) implement business logic
4. Repositories (`repository/` package) manage data persistence

## Development Workflow
### Local Setup
1. Configure MySQL in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/menu_db
   spring.datasource.username=root
   spring.datasource.password=root
   ```

2. Run the application:
   ```powershell
   cd cafe-app/demo
   ./mvnw spring-boot:run
   ```

### Key Integration Points
1. WebSocket for real-time suggestions:
   - Endpoint: `/ws-suggest`
   - Topic: `/topic/suggestions`
   - Client implementation in `static/suggestion_display.html`

2. Google API integration:
   - Configure key in `application.properties`
   - Used for analytics and face detection features

## Common Patterns
1. DTO pattern for data transfer:
   - Request DTOs: `*RequestDto.java`
   - Response DTOs: `*ResponseDto.java`
   - Example: `OrderRequestDto.java` → `OrderResponseDto.java`

2. Controller conventions:
   - REST controllers use `*Controller` naming
   - API controllers use `*ApiController` suffix
   - Example: `AdminController.java` vs `AdminDashboardApiController.java`

## Testing
- Application tests in `src/test/java`
- Test properties in `application-test.properties`
- Run tests: `./mvnw test`

## External Components
- Python suggestion client in `python-clients/suggestion_client.py`
- Integration with external Google APIs for analytics