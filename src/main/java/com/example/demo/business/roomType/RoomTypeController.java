package com.example.demo.business.roomType;

import com.example.demo.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

//  @METHOD     GET
//  @DESC       get one room type
//  @ACCESS     PUBLIC
    @RequestMapping(value = "/public/{hotelId}/room-type/{roomTypeId}")
    public ResponseEntity<RoomTypeResponse> getOneRoomType(@PathVariable Integer hotelId, @PathVariable Integer roomTypeId) {
        return ResponseEntity.ok(roomTypeService.getOneRoomType(hotelId, roomTypeId));
    }

//  @METHOD     GET
//  @DESC       get all rooms type
//  @ACCESS     PUBLIC
    @RequestMapping(value = "/public/{hotelId}/room-type")
    public ResponseEntity<List<RoomTypeResponse>> getAllRoomsType(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(roomTypeService.getAllRoomsType(hotelId));
    }

//  @METHOD     POST
//  @DESC       create a room type
//  @ACCESS     ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/admin/{hotelId}/room-type", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> addRoomType(@PathVariable Integer hotelId, @RequestBody RoomTypeRequest request) {
        return ResponseEntity.status(201).body(roomTypeService.addRoomType(hotelId, request));
    }

//  @METHOD     PUT
//  @DESC       update a room type
//  @ACCESS     ADMIN
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping(value = "/admin/{hotelId}/room-type/{roomTypeId}", method = RequestMethod.PUT)
public ResponseEntity<MessageResponse> updateRoomType(@PathVariable Integer hotelId, @PathVariable Integer roomTypeId,
                                                      @RequestBody RoomTypeRequest request) {
    return ResponseEntity.ok(roomTypeService.updateRoomType(hotelId, roomTypeId, request));
}

//  @METHOD     DELETE
//  @DESC       delete a room type
//  @ACCESS     ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/admin/{hotelId}/room-type/{roomTypeId}", method = RequestMethod.DELETE)
    public ResponseEntity<MessageResponse> deleteRoomType(@PathVariable Integer hotelId, @PathVariable Integer roomTypeId) {
        return ResponseEntity.ok(roomTypeService.deleteRoomType(hotelId, roomTypeId));
    }
}
