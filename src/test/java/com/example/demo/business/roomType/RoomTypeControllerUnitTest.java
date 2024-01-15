package com.example.demo.business.roomType;

import com.example.demo.MessageResponse;
import com.example.demo.exception.ProblemDetailTemplate;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = RoomTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RoomTypeControllerUnitTest {
    @MockBean
    private RoomTypeService roomTypeService;
    @MockBean
    private ProblemDetailTemplate problemDetailTemplate;
    @Autowired
    private MockMvc mockMvc;
    private static String GENERIC_URL = "http://localhost:8080/api/v1";

    @Test
    void getOneRoomType() throws Exception {
        RoomTypeResponse roomTypeResponse = new RoomTypeResponse.Builder()
                .name("Camera single")
                .price(new BigDecimal(String.valueOf(150)))
                .maxPeople(1)
                .build();
        when(roomTypeService.getOneRoomType(1,1)).thenReturn(roomTypeResponse);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GENERIC_URL + "/public/1/room-type/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String expectedResponse = """
                { "name": "Camera single",
                "price": 150.00,
                "maxPeople": 1}
                """;
        JSONAssert.assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString(), false);
    }
    @Test
    void getAllRoomsType() throws Exception {
        List<RoomTypeResponse> roomTypeResponses = new ArrayList<>(List.of(
                new RoomTypeResponse.Builder()
                        .name("Camera single")
                        .price(new BigDecimal(String.valueOf(150)))
                        .maxPeople(1)
                        .build()
        ));
        when(roomTypeService.getAllRoomsType(1)).thenReturn(roomTypeResponses);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GENERIC_URL + "/public/1/room-type")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String expectedResponse = """
                [ { "name": "Camera single",
                 "price": 150.00,
                 "maxPeople": 1} ]
                """;
        JSONAssert.assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    void createRoomType() throws Exception {
        String requestBody = """
                {
                    "name": "Camera single",
                    "price": "150",
                    "maxPeople": 1
                }
                """;
        MessageResponse messageResponse =new MessageResponse.Builder()
                .success(true)
                .message("Room Type successfully created.")
                .build();
        when(roomTypeService.createRoomType(any(), any())).thenReturn(messageResponse);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("http://localhost:8080/api/v1/admin/1/room-type")
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String expectedResponse = """
                {"success":true,
                "message":"Room Type successfully created."}
                """;
        JSONAssert.assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString(), true);
    }

    @Test
    void updateRoomType() throws Exception {
        String requestBody = """
                {
                    "name": "Camera double",
                    "price": "200",
                    "maxPeople": 2
                }
                """;
        MessageResponse messageResponse =new MessageResponse.Builder()
                .success(true)
                .message("Room Type successfully updated.")
                .build();
        when(roomTypeService.updateRoomType(any(), any(), any())).thenReturn(messageResponse);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("http://localhost:8080/api/v1/admin/1/room-type/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String expectedResponse = """
                {"success":true,
                "message":"Room Type successfully updated."}
                """;
        JSONAssert.assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString(), true);
    }

    @Test
    void deleteRoomType() throws Exception {
        MessageResponse messageResponse =new MessageResponse.Builder()
                .success(true)
                .message("Room Type successfully deleted.")
                .build();
        when(roomTypeService.deleteRoomType(any(), any())).thenReturn(messageResponse);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("http://localhost:8080/api/v1/admin/1/room-type/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String expectedResponse = """
                {"success": true,
                "message": "Room Type successfully deleted."}
                """;
        JSONAssert.assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString(), true);
    }
}
