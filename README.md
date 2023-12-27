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

## kafka

### Run Just Kafka services
```bash
docker-compose -f docker-compose.kafka.yml up -d
```

Kafka can be accessed at localhost:9092, also available via the Kafka UI at http://localhost:8080/.

![Kafka UI](docs/images/kafka-ui.png)

## Swagger

Access Open API documentation at: http://localhost:8090/swagger-ui/index.html

API documentation is also available in JSON format at: http://localhost:8090/v3/api-docs

![Swagger UI](docs/images/swagger-api.png)
