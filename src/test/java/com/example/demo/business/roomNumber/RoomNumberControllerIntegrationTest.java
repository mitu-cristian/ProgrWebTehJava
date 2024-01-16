package com.example.demo.business.roomNumber;

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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomNumberControllerIntegrationTest {
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private RoomNumberRepository roomNumberRepository;
    @Value("${application.security.jwt.token.admin}")
    private String adminToken;

    @Test
    @Order(1)
    void getOneRoomNumber() throws Exception {
        HttpEntity<String> httpEntity = new HttpEntity<>(null, null);
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/public/1/room-type/1/room-number/1",
                HttpMethod.GET, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    @Order(1)
    void getAllRoomsNumber() throws Exception {
        HttpEntity<String> httpEntity = new HttpEntity<>(null, null);
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/public/1/room-type/1/room-number",
                HttpMethod.GET, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    @Order(2)
    void createRoomNumber() throws Exception {
        String requestBody = """
                {
                    "number": 0
                }
                """;
        HttpHeaders headers = createHeadersForAdmin();
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/admin/1/room-type/1/room-number",
                HttpMethod.POST, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String expectedResponse = """
                { "success": true,
                "message": "Room number successfully created."}
                """;
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    @Order(3)
    void updateRoomNumber() throws Exception {
        String requestBody = """
                {"number": 0}
                """;
        HttpHeaders headers = createHeadersForAdmin();
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        Integer id = roomNumberRepository.findByNumber(0).get().getId();
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/admin/1/room-type/1/room-number/" + id,
                HttpMethod.PUT, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String expectedResponse = """
                { "success": true,
                "message": "Room number successfully updated."}
                """;
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    @Order(4)
    void deleteRoomNumber() throws Exception {
        HttpHeaders headers = createHeadersForAdmin();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        Integer id = roomNumberRepository.findByNumber(0).get().getId();
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/admin/1/room-type/1/room-number/" + id,
                HttpMethod.DELETE, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String expectedResponse = """
                { "success": true,
                "message": "Room number successfully deleted."}
                """;
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    private HttpHeaders createHeadersForAdmin() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Authorization", "Bearer " + adminToken);
            return headers;
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating admin headers", e);
        }
    }
}
