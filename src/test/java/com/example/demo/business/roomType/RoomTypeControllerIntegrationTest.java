package com.example.demo.business.roomType;

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
public class RoomTypeControllerIntegrationTest {
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Value("${application.security.jwt.token.admin}")
    private String adminToken;

    @Test
    @Order(1)
    void getOneRoomType() throws Exception {
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, null);
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/public/1/room-type/1",
                HttpMethod.GET, httpEntity,String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    @Order(1)
    void getAllRoomsType() throws Exception {
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, null);
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/public/1/room-type",
                HttpMethod.GET, httpEntity,String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    @Order(2)
    void createRoomType() throws Exception {
        String requestBody = """
                {
                    "name": "Camera test",
                    "price": "200",
                    "maxPeople": 2
                }
                """;
        HttpHeaders headers = createHeadersForAdmin();
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/admin/1/room-type",
                HttpMethod.POST, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String expectedResponse = """
                {"success": true,
                "message": "Room type successfully added."}
                """;
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }


    @Test
    @Order(3)
    void updateRoomType() throws Exception {
        String requestBody = """
                {
                    "name": "Camera test update"
                }
                """;
        HttpHeaders headers = createHeadersForAdmin();
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);
        Integer id = roomTypeRepository.findByName("Camera test").get().getId();
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/admin/1/room-type/" + id,
                HttpMethod.PUT, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String expectedResponse = """
                {"success": true,
                "message": "Room type successfully updated."}
                """;
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }
    @Test
    @Order(4)
    void deleteRoomType() throws Exception {
        HttpHeaders headers = createHeadersForAdmin();
        HttpEntity httpEntity = new HttpEntity<>(null, headers);
        Integer id = roomTypeRepository.findByName("Camera test update").get().getId();
        ResponseEntity<String> responseEntity = template.exchange("/api/v1/admin/1/room-type/" + id,
                HttpMethod.DELETE, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String expectedResponse = """
                {"success": true,
                "message": "Room type successfully deleted."}
                """;
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), true);
    }

    private HttpHeaders createHeadersForAdmin() {
        try{
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
