package com.example.demo.business.roomNumber;

import com.example.demo.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RoomNumberController {
    private final RoomNumberService roomNumberService;

    public RoomNumberController(RoomNumberService roomNumberService) {
        this.roomNumberService = roomNumberService;
    }

//  @METHOD     GET
//  @DESC       get one room number
//  @ACCESS     PUBLIC
    @RequestMapping(value = "/public/{hotelId}/room-type/{roomTypeId}/room-number/{roomNumberId}")
    public ResponseEntity<RoomNumberResponse> getOneRoomNumber(@PathVariable Integer hotelId,
                                                       @PathVariable Integer roomTypeId,
                                                       @PathVariable Integer roomNumberId) {
        return ResponseEntity.ok(roomNumberService.getOneRoomNumber(hotelId, roomTypeId, roomNumberId));
    }

//  @METHOD     GET
//  @DESC       get all room numbers
//  @ACCESS     PUBLIC
    @RequestMapping(value = "/public/{hotelId}/room-type/{roomTypeId}/room-number")
    public ResponseEntity<List<RoomNumberResponse>> getAllRoomsNumber(@PathVariable Integer hotelId,
                                                                      @PathVariable Integer roomTypeId) {
        return ResponseEntity.ok(roomNumberService.getAllRoomsNumber(hotelId, roomTypeId));
    }

//  @METHOD     POST
//  @DESC       create a room number
//  @ACCESS     ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/admin/{hotelId}/room-type/{roomTypeId}/room-number")
    public ResponseEntity<MessageResponse> createRoomNumber(@PathVariable Integer hotelId,
                                                         @PathVariable Integer roomTypeId,
                                                         @RequestBody RoomNumberRequest request) {
        return ResponseEntity.status(201).body(roomNumberService.createRoomNumber(hotelId, roomTypeId, request));
    }

//  @METHOD     PUT
//  @DESC       update room number
//  @ACCESS     ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/admin/{hotelId}/room-type/{roomTypeId}/room-number/{roomNumberId}", method  = RequestMethod.PUT)
    public ResponseEntity<MessageResponse> updateRoomNumber(@PathVariable Integer hotelId,
                                                            @PathVariable Integer roomTypeId,
                                                            @PathVariable Integer roomNumberId,
                                                            @RequestBody RoomNumberRequest request) {
        return ResponseEntity.ok(roomNumberService.updateRoomNumber(hotelId, roomTypeId, roomNumberId, request));
    }

//  @METHOD     DELETE
//  @DESC       delete a room number
//  @ACCESS     ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/admin/{hotelId}/room-type/{roomTypeId}/room-number/{roomNumberId}", method = RequestMethod.DELETE)
    public ResponseEntity<MessageResponse> deleteRoomNumber(@PathVariable Integer hotelId,
                                                               @PathVariable Integer roomTypeId,
                                                               @PathVariable Integer roomNumberId) {
        return ResponseEntity.ok(roomNumberService.deleteRoomNumber(hotelId, roomTypeId, roomNumberId));
    }
}
