package com.example.consumerestful;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

record Quote(String type, Value value) {

}

record Value(long id, String quote) {

}


@SpringBootApplication
public class ConsumeRestfulApplication {
    private static final Logger logger = LoggerFactory.getLogger(ConsumeRestfulApplication.class);

    @Bean
    ApplicationRunner run(RestTemplate restTemplate) {
        return args -> {
            Quote quote = restTemplate.getForObject("http://localhost:8080/api/random", Quote.class);
            logger.info("quote: {}", quote);
        };
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumeRestfulApplication.class, args);
    }

}
