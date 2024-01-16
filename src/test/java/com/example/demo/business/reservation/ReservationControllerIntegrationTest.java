package com.example.demo.business.reservation;

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
public class ReservationControllerIntegrationTest {
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private ReservationRepository reservationRepository;
    @Value("${application.security.jwt.token.admin}")
    private String adminToken;
    @Value("${application.security.jwt.token.user}")
    private String userToken;

    @Test
    @Order(1)
    void userCreatesReservation() throws Exception{
        String requestBody = """
                {
                    "check-in": "2020-01-16",
                    "check-out": "2020-01-18",
                    "number-of-people": 0
                }
                """;
        HttpHeaders headers = createHeadersForUser();
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/hotel/1/room-type/1/room-number/1/reservation",
                HttpMethod.POST, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String expectedResponse = """
                {"success": true,
                "message": "Reservation successfully created."}
                """;
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    @Order(2)
    void changeReservationStatusCancelled() throws Exception {
        HttpHeaders headers = createHeadersForUser();
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
        Integer id = reservationRepository.findByNrPeople(0).get().getId();
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/user/2/reservation/" + id + "/cancelled",
                HttpMethod.PUT, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String expectedResponse = """
                {"success": true,
                "message": "Successfully changed the status of reservation to cancelled."}
                """;
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    @Order(3)
    void changeReservationStatusCompleted() throws Exception {
        HttpHeaders headers = createHeadersForAdmin();
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
        Integer id = reservationRepository.findByNrPeople(0).get().getId();
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/user/2/reservation/" + id + "/completed",
                HttpMethod.PUT, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String expectedResponse = """
                {"success": true,
                "message": "Successfully changed the status of reservation to completed."}
                """;
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
