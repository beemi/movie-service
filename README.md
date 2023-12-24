# Spring boot REST API with MongoDB

This is an example of a REST API using Spring Boot and MongoDB.

## Requirements

- Java 17
- Docker
- Docker Compose
- Maven
- MongoDB
- MongoDB Compass
- Postman

### Start the MongoDB database

```bash
docker-compose up -d
```

### Build the application

`jar` file will be generated in `target` folder.

```bash
./mvnw clean package -DskipTests
```

## Metrics

Access Prometheus at: http://localhost:8090/actuator/prometheus

## Swagger

Access Open API documentation at: http://localhost:8090/swagger-ui/index.html

API documentation is also available in JSON format at: http://localhost:8090/v3/api-docs
