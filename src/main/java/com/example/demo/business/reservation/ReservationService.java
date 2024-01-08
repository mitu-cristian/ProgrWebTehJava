package com.example.demo.business.reservation;

import com.example.demo.MessageResponse;
import com.example.demo.business.hotel.Hotel;
import com.example.demo.business.hotel.HotelRepository;
import com.example.demo.business.roomNumber.RoomNumber;
import com.example.demo.business.roomNumber.RoomNumberRepository;
import com.example.demo.business.roomType.RoomType;
import com.example.demo.business.roomType.RoomTypeRepository;
import com.example.demo.business.unavailableDate.UnavailableDateRepository;
import com.example.demo.business.unavailableDate.UnavailableDateRequest;
import com.example.demo.business.unavailableDate.UnavailableDateService;
import com.example.demo.config.JwtService;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReservationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomNumberRepository roomNumberRepository;
    private final UnavailableDateService unavailableDateService;
    private final ReservationRepository reservationRepository;


    public ReservationService(UserRepository userRepository, JwtService jwtService, HotelRepository hotelRepository, RoomTypeRepository roomTypeRepository,
                              RoomNumberRepository roomNumberRepository, UnavailableDateService unavailableDateService,
                              ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.hotelRepository = hotelRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.roomNumberRepository = roomNumberRepository;
        this.unavailableDateService = unavailableDateService;
        this.reservationRepository = reservationRepository;
    }

    public MessageResponse userCreatesReservation (Integer hotelId, Integer roomTypeId, Integer roomNumberId,
                                                   ReservationUserRequest reservationUserRequest,
                                                   HttpServletRequest httpRequest) {
        if(validRoomNumber(hotelId, roomTypeId, roomNumberId) == true) {
            final String jwtToken = httpRequest.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
            final String email = jwtService.extractEmail(jwtToken);
            final UserEntity userEntity = userRepository.findByEmail(email).get();
            BigDecimal nrNights = BigDecimal.valueOf(ChronoUnit.DAYS.between(reservationUserRequest.getCheckIn(),
                    reservationUserRequest.getCheckOut()));
            RoomType existingRoomType = roomTypeRepository.findById(roomTypeId).get();
            Reservation reservation = new Reservation.Builder()
                    .user(userEntity)
                    .price(existingRoomType.getPrice().multiply(nrNights))
                    .nrPeople(reservationUserRequest.getNrPeople())
                    .roomNumber(roomNumberRepository.findById(roomNumberId).get())
                    .checkIn(reservationUserRequest.getCheckIn())
                    .checkOut(reservationUserRequest.getCheckOut())
                    .status(Status.NEW)
                    .builder();
            reservationRepository.save(reservation);
            List<LocalDate> dateRange = getDateRange(reservationUserRequest.getCheckIn(),
                    reservationUserRequest.getCheckOut());
            dateRange.forEach(
                    date -> {
                        unavailableDateService.createUnavailableDate(hotelId, roomTypeId,
                                roomNumberId, new UnavailableDateRequest.Builder()
                                        .unavailableDate(date)
                                        .build());

                    }
            );
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Reservation successfully added.")
                    .build();
        }
        else throw new RuntimeException("Error while user creates the reservation.");
    }

    public MessageResponse changeReservationStatusCompleted(Integer userId, Integer reservationId) {
        if(validReservation(userId, reservationId)) {
            Reservation existingReservation = reservationRepository.findById(reservationId).get();
            existingReservation.setStatus(Status.COMPLETED);
            reservationRepository.save(existingReservation);
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Successfully changed the status of reservation to completed")
                    .build();
        }
        else throw new RuntimeException("Error while changing the status of reservation to completed.");
    }

    public MessageResponse changeReservationStatusCancelled(Integer userId, Integer reservationId) {
        if(validReservation(userId, reservationId)) {
            Reservation existingReservation = reservationRepository.findById(reservationId).get();
            existingReservation.setStatus(Status.CANCELLED);
            reservationRepository.save(existingReservation);
            List<LocalDate> dateRange = getDateRange(existingReservation.getCheckIn(),
                    existingReservation.getCheckOut());
            dateRange.forEach(
                    date -> {
                        unavailableDateService.deleteUnavailableDateByDateAndRoomNumberId(
                                existingReservation.getRoomNumber(),
                                date
                        );
                    }

            );
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Successfully changed the status of reservation to cancelled.")
                    .build();
        }
        else throw new RuntimeException("Error while changing the status of reservation to cancelled.");
    }

    private boolean validRoomNumber(Integer hotelId, Integer roomTypeId, Integer roomNumberId) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
        if(optionalHotel.isEmpty())
            throw new RuntimeException("Hotel not found with id of " + hotelId);
        else if(optionalHotel.isPresent()) {
            Optional<RoomType> optionalRoomType = roomTypeRepository.findById(roomTypeId);
            if(optionalRoomType.isEmpty())
                throw new RuntimeException("Room type not found with id of " + roomTypeId);
            else if(optionalRoomType.isPresent()) {
                if(optionalHotel.get().getId() != optionalRoomType.get().getHotel().getId())
                    throw new RuntimeException("Room type with id of " + roomTypeId + " doesn't belong to the hotel with id of "
                            + hotelId + ".");
                else if(optionalHotel.get().getId() == optionalRoomType.get().getHotel().getId()) {
                    Optional<RoomNumber> optionalRoomNumber = roomNumberRepository.findById(roomNumberId);
                    if(optionalRoomNumber.isEmpty())
                        throw new RuntimeException("Room number not found with id of " + roomNumberId + ".");
                    else if(optionalRoomNumber.isPresent()) {
                        if(optionalRoomType.get().getId() != optionalRoomNumber.get().getRoomType().getId())
                            throw new RuntimeException("Room number with id of " + roomNumberId + " doesn't belong to the room type with id of "
                                    + roomTypeId + ".");
                        else return true;
                    }
                }
            }

        }
        return false;
    }

    private static List<LocalDate> getDateRange(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate))
                .collect(Collectors.toList());
    }

    private boolean validReservation(Integer userId, Integer reservationId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty())
            throw new RuntimeException("User with id of " + userId + " not found.");
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if(optionalReservation.isEmpty())
            throw new RuntimeException("Reservation with id of " + reservationId + " not found.");
        if(optionalUser.isPresent() && optionalReservation.isPresent()) {
            if(optionalUser.get().getId() != optionalReservation.get().getUser().getId())
                throw new RuntimeException("The reservation with id of " + reservationId + " doesn't belong to the " +
                        "user with id of " + userId +".");
            if(optionalUser.get().getId() == optionalReservation.get().getUser().getId())
                return true;
        }
        return false;
    }

}
