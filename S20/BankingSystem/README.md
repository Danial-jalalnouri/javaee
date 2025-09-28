# Banking System

A microservices-based banking system with Customer Relationship Management (CRM) and Accounting services.

## System Architecture

The system consists of two microservices:
1. CRM Service (Port 8081) - Manages customer information
2. Accounting Service (Port 8082) - Handles account management and transactions

## User Guide

### Features

#### CRM Service
- Manage both individual and corporate customers
- Create, read, update, and delete customer records
- Validate customer information
- Interactive API documentation

#### Accounting Service
- Create and manage customer accounts
- Support for multiple currencies
- Deposit and withdrawal operations
- Transfer between accounts (same currency)
- View account details by customer

### API Documentation
Once the services are running, you can access the Swagger UI documentation at:
- CRM Service: http://localhost:8081/swagger-ui.html
- Accounting Service: http://localhost:8082/swagger-ui.html

## Developer Guide

### Technology Stack
- Java 21
- Spring Boot 3.1.4
- Spring Data JPA
- H2 Database (File-based)
- MapStruct for object mapping
- Lombok for reducing boilerplate code
- SpringDoc OpenAPI for API documentation
- Maven for dependency management and build automation

### Project Structure
```
aiBankingSystem/
├── crm-service/              # Customer Management Service
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/danialechoes/
│   │       │       ├── dto/
│   │       │       ├── entity/
│   │       │       ├── mapper/
│   │       │       ├── repository/
│   │       │       ├── service/
│   │       │       └── web/
│   │       └── resources/
│   └── pom.xml
├── accounting-service/       # Account Management Service
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/danialechoes/
│   │       │       ├── dto/
│   │       │       ├── entity/
│   │       │       ├── repository/
│   │       │       ├── service/
│   │       │       └── web/
│   │       └── resources/
│   └── pom.xml
└── pom.xml                  # Parent POM
```

### Prerequisites
- JDK 21
- Maven 3.x
- Your favorite IDE (IntelliJ IDEA recommended)

### Environment Setup
1. Install JDK 21
2. Set JAVA_HOME environment variable:
   ```bash
   export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
   ```
   Add this to your ~/.zshrc file for persistence to avoid setting it each time.

### Building the Project
```bash
mvn clean install
```

### Running the Services
1. Start CRM Service:
```bash
cd crm-service
mvn spring-boot:run
```

2. Start Accounting Service:
```bash
cd accounting-service
mvn spring-boot:run
```

### Database Configuration
Both services use file-based H2 databases:
- CRM Service: `crm-db.mv.db` in the root directory
- Accounting Service: `accounting-db.mv.db` in the root directory

Access H2 Console:
- CRM Service: http://localhost:8081/h2-console
- Accounting Service: http://localhost:8082/h2-console

### Development Guidelines

#### Adding New Features
1. Create entity classes in the `entity` package
2. Create corresponding DTOs in the `dto` package
3. Define mapper interfaces in the `mapper` package using MapStruct (if needed)
4. Implement repository interfaces extending JpaRepository
5. Create service classes for business logic
6. Implement REST controllers in the `web` package
7. Add validation annotations as needed
8. Update API documentation using OpenAPI annotations

#### Code Style
- Use Lombok annotations to reduce boilerplate
- Follow Spring best practices for dependency injection
- Write unit tests for new features
- Use meaningful names for classes, methods, and variables
- Document public APIs using Javadoc

### Testing
- Unit tests should be in `src/test/java`
- Use `@SpringBootTest` for integration tests
- Mock dependencies using Mockito

### Common Issues and Solutions
1. JAVA_HOME not set:
   - Add the export command to your ~/.zshrc file
   - Restart your terminal or source the file

2. Port conflicts:
   - CRM Service uses port 8081
   - Accounting Service uses port 8082
   - Change ports in application.properties if needed

3. Database files:
   - Ensure write permissions in the root directory
   - Check file paths in application.properties

### Future Improvements
- Add service discovery (Eureka)
- Implement API Gateway
- Add Circuit Breaker patterns
- Implement distributed tracing
- Add message queues for async operations
- Containerize services with Docker
- Add Kubernetes deployment configurations

### Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

### Maintainers
- Initial development by danialechoes

For any questions or issues, please open a GitHub issue.
