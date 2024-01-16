package com.example.demo.business.review;

import com.example.demo.token.TokenRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReviewControllerIntegrationTest {
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Value("${application.security.jwt.token.admin}")
    private String adminToken;
    @Value("${application.security.jwt.token.user}")
    private String userToken;

    @Test
    @Order(1)
    void getReview() throws Exception {
        HttpHeaders headers = createHeadersForUser();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/user/review",
                HttpMethod.GET, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    @Order(1)
    void getAllReviews() throws Exception {
        HttpEntity<String> httpEntity = new HttpEntity<>(null, null);
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/public/review",
                HttpMethod.GET, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    @Order(1)
    void createReview() throws Exception {
        String requestBody = """
                {
                    "rating": 4,
                    "description": "create review"
                }
                """;
        HttpHeaders headers = createHeadersForUser();
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/user/review",
                HttpMethod.POST, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String expectedResponse = """
                {"success": true,
                "message": "Review successfully created."}
                """;
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    @Order(2)
    void updateReview() throws Exception {
        String requestBody = """
                {
                    "rating": 5
                }
                """;
        HttpHeaders headers = createHeadersForUser();
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/user/review",
                HttpMethod.PUT, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String expectedResponse = """
                { "success": true,
                "message": "Review successfully updated."
                }
                """;
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);

    }

    @Test
    @Order(3)
    void deleteReview() throws Exception {
        HttpHeaders headers = createHeadersForUser();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/user/review",
                HttpMethod.DELETE, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String expectedResponse = """
                { "success": true,
                "message": "Review successfully deleted."
                }
                """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);

    }

    private HttpHeaders createHeadersForAdmin() {
        return createHeaders(adminToken);
    }
    private HttpHeaders createHeadersForUser() {
        return createHeaders(userToken);
    }

    private HttpHeaders createHeaders(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Authorization", "Bearer " + token);
            return headers;
        }
        catch(Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Error creating headers", exception);
        }
    }
}
