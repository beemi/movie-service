package com.jaitechltd.movieservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.KafkaProducerService;
import kafka.KafkaPublisherCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaProducerService kafkaProducerService(final KafkaTemplate<String, String> eventKafkaTemplate,
                                                     final ObjectMapper objectMapper) {

        return new KafkaProducerService(eventKafkaTemplate, objectMapper);
    }

    @Bean
    public KafkaPublisherCallback<String> kafkaPublisherCallback() {
        return new KafkaPublisherCallback<>();
    }
}
