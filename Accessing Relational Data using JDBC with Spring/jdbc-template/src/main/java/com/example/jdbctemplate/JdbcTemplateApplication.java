package com.example.jdbctemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@ToString
@AllArgsConstructor
class Customer {
    private long id;
    private String firstName, lastName;
}

@SpringBootApplication
public class JdbcTemplateApplication {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateApplication.class);

    @Bean
    ApplicationRunner run(JdbcTemplate jdbcTemplate) {
        return args -> {
            logger.info("Creating tables");

            jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
            jdbcTemplate.execute("CREATE TABLE customers(" +
                    "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

            // Split up the array of whole names into an array of first/last names
            List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                    .map(name -> name.split(" "))
                    .collect(Collectors.toList());

            // Use a Java 8 stream to print out each tuple of the list
            splitUpNames.forEach(name -> logger.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

            // Uses JdbcTemplate's batchUpdate operation to bulk load data
            jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

            logger.info("Querying for customer records where first_name = 'Josh':");
            jdbcTemplate.query(
                    "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[]{"Josh"},
                    (rs, rowNum) -> new Customer(rs.getLong("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"))
            ).forEach(customer -> logger.info(customer.toString()));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(JdbcTemplateApplication.class, args);
    }

}
