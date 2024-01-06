package com.example.demo.business.unavailableDate;

import com.example.demo.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UnavailableDateController {
    private final UnavailableDateService unavailableDateService;

    public UnavailableDateController(UnavailableDateService unavailableDateService) {
        this.unavailableDateService = unavailableDateService;
    }

//  @METHOD     GET
//  @DESC       get one unavailableDate
//  @ACCESS     PUBLIC
    @RequestMapping(value = "public/hotel/{hotelId}/room-type/{roomTypeId}/room-number/{roomNumberId}/unavailable-date/{unavailableDateId}")
    public ResponseEntity<UnavailableDateResponse> getOneUnavailableDate(@PathVariable Integer hotelId,
                                                                 @PathVariable Integer roomTypeId,
                                                                 @PathVariable Integer roomNumberId,
                                                                 @PathVariable Integer unavailableDateId) {
        return ResponseEntity.ok(unavailableDateService.getOneUnavailableDate(hotelId, roomTypeId, roomNumberId, unavailableDateId));
    }

//  @METHOD     GET
//  @DESC       get all unavailable dates
//  @ACCESS     PUBLIC
@RequestMapping(value = "public/hotel/{hotelId}/room-type/{roomTypeId}/room-number/{roomNumberId}/unavailable-date")
public ResponseEntity<List<UnavailableDateResponse>> getAllUnavailableDates(@PathVariable Integer hotelId,
                                                                            @PathVariable Integer roomTypeId,
                                                                            @PathVariable Integer roomNumberId) {
        return ResponseEntity.ok(unavailableDateService.getAllUnavailableDates(hotelId, roomTypeId, roomNumberId));
}

//  @METHOD     POST
//  @DESC       create unavailableDate
//  @ACCESS     ADMIN / USER
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
    @RequestMapping(value = "/{hotelId}/room-type/{roomTypeId}/room-number/{roomNumberId}/unavailable-date",
    method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> createUnavailableDate(@PathVariable Integer hotelId,
                                                                 @PathVariable Integer roomTypeId,
                                                                 @PathVariable Integer roomNumberId,
                                                                 @RequestBody UnavailableDateRequest request) {
        return ResponseEntity.ok(unavailableDateService.createUnavailableDate(hotelId, roomTypeId, roomNumberId, request));
    }

//  @METHOD     PUT
//  @DESC       update unavailableDate
//  @ACCESS     ADMIN / USER
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
    @RequestMapping(value = "/hotel/{hotelId}/room-type/{roomTypeId}/room-number/{roomNumberId}/unavailable-date/{unavailableDateId}",
    method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse> updateUnavailableDate(@PathVariable Integer hotelId,
                                                                 @PathVariable Integer roomTypeId,
                                                                 @PathVariable Integer roomNumberId,
                                                                 @PathVariable Integer unavailableDateId,
                                                                 @RequestBody UnavailableDateRequest request) {
        return ResponseEntity.status(201).body(unavailableDateService.updateUnavailableDate(hotelId, roomTypeId, roomNumberId,
                unavailableDateId, request));
    }

//  @METHOD     DELETE
//  @DESC       delete unavailable date
//  @ACCESS     ADMIN / USER
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
    @RequestMapping(value = "/hotel/{hotelId}/room-type/{roomTypeId}/room-number/{roomNumberId}/unavailable-date/{unavailableDateId}",
    method = RequestMethod.DELETE)
    public ResponseEntity<MessageResponse> deleteUnavailableDate(@PathVariable Integer hotelId,
                                                                 @PathVariable Integer roomTypeId,
                                                                 @PathVariable Integer roomNumberId,
                                                                 @PathVariable Integer unavailableDateId) {
        return ResponseEntity.ok(unavailableDateService.deleteUnavailableDate(hotelId, roomTypeId, roomNumberId, unavailableDateId));
    }
}
