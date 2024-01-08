package com.example.demo.business.reservation;

import com.example.demo.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController (ReservationService reservationService) {
        this.reservationService = reservationService;
    }

//  @METHOD     POST
//  @DESC       user creates reservation
//  @ACCESS     USER
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/hotel/{hotelId}/room-type/{roomTypeId}/room-number/{roomNumberId}/reservation",
            method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> userCreatesReservation(@PathVariable Integer hotelId,
                                                                  @PathVariable Integer roomTypeId,
                                                                  @PathVariable Integer roomNumberId,
                                                                  @RequestBody ReservationUserRequest reservationUserRequest,
                                                                  HttpServletRequest httpRequest) {
        return ResponseEntity.ok(reservationService.userCreatesReservation(hotelId, roomTypeId, roomNumberId,
                reservationUserRequest, httpRequest));

    }

//  @METHOD     PUT
//  @DESC       admin changes the status of reservation to completed
//  @ACCESS     ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/user/{userId}/reservation/{reservationId}/completed", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse> changeReservationStatusCompleted (@PathVariable Integer userId,
                                                                                  @PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.changeReservationStatusCompleted(userId, reservationId));
    }

//  @METHOD     PUT
//  @DESC       admin changes the status of reservation to cancelled
//  @ACCESS     USER or ADMIN
    @RequestMapping(value = "/user/{userId}/reservation/{reservationId}/cancelled", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse> changeReservationStatusCancelled (@PathVariable Integer userId,
                                                                             @PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.changeReservationStatusCancelled(userId, reservationId));
    }
}
