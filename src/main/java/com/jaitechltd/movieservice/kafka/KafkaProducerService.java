package com.jaitechltd.movieservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaitechltd.movieservice.model.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(final String topic, final Movie message) {
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(message));
        } catch (Exception e) {
           log.error("Error while sending message to kafka topic: {}", topic, e);
        }
    }
}
