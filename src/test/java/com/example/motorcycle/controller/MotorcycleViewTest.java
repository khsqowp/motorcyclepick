package com.example.motorcycle.controller;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MotorcycleViewTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MotorcycleViewTest.class);

    @Test
    public void testViewMotorcycles() {
        String url = "http://localhost:" + port + "/motorcycle/list";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Log the HTML response body
        if (response.getBody() != null) {
            logger.info("HTML Response: \n{}", response.getBody());
        } else {
            logger.warn("Received null response body from the server.");
        }

        // Assert that the response status code is 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Assert that the response body contains expected content (e.g., the entire list of motorcycles)
        assertThat(response.getBody()).isNotNull().contains("<html>", "<body>", "Motorcycle List", "motorcycle-item");
    }
}