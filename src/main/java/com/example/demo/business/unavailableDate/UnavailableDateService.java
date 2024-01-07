package com.example.demo.business.unavailableDate;

import com.example.demo.MessageResponse;
import com.example.demo.business.hotel.Hotel;
import com.example.demo.business.hotel.HotelRepository;
import com.example.demo.business.roomNumber.RoomNumber;
import com.example.demo.business.roomNumber.RoomNumberRepository;
import com.example.demo.business.roomType.RoomType;
import com.example.demo.business.roomType.RoomTypeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UnavailableDateService {
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomNumberRepository roomNumberRepository;
    private final UnavailableDateRepository unavailableDateRepository;

    public UnavailableDateService(HotelRepository hotelRepository, RoomTypeRepository roomTypeRepository,
                           RoomNumberRepository roomNumberRepository, UnavailableDateRepository unavailableDateRepository) {
        this.hotelRepository = hotelRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.roomNumberRepository = roomNumberRepository;
        this.unavailableDateRepository = unavailableDateRepository;
    }

    public UnavailableDateResponse getOneUnavailableDate(Integer hotelId, Integer roomTypeId,
                                                 Integer roomNumberId, Integer unavailableDateId) {
        if(validUnavailableDate(hotelId, roomTypeId, roomNumberId, unavailableDateId)) {
            UnavailableDate existingUnavailableDate = unavailableDateRepository.findById(unavailableDateId).get();
            return new UnavailableDateResponse.Builder()
                    .id(existingUnavailableDate.getId())
                    .unavailableDate(existingUnavailableDate.getUnavailableDate())
                    .build();
        }
        else throw new RuntimeException("Error while fetching the unavailable date.");
    }

    public List<UnavailableDateResponse> getAllUnavailableDates(Integer hotelId, Integer roomTypeId, Integer roomNumberId) {
        if(validRoomNumber(hotelId, roomTypeId, roomNumberId)) {
            List<UnavailableDateResponse> unavailableDateResponsesList = new ArrayList<>();
            List<UnavailableDate> existingUnavailableDates = unavailableDateRepository.findByRoomNumber(roomNumberRepository.findById(roomNumberId).get()).get();
            existingUnavailableDates.forEach(
                    existingUnavailableDate -> {
                        unavailableDateResponsesList.add(
                                new UnavailableDateResponse.Builder()
                                        .id(existingUnavailableDate.getId())
                                        .unavailableDate(existingUnavailableDate.getUnavailableDate())
                                        .build()
                        );
                    }
            );
            return unavailableDateResponsesList;
        }
        else throw new RuntimeException("Error while fetching all the unavailable dates.");
    }

    public void createUnavailableDate(Integer hotelId, Integer roomTypeId,
                                                 Integer roomNumberId, UnavailableDateRequest request) {
        if(validRoomNumber(hotelId, roomTypeId, roomNumberId) == true) {
            RoomNumber existingRoomNumber = roomNumberRepository.findById(roomNumberId).get();
            UnavailableDate unavailableDate = new UnavailableDate.Builder()
                    .unavailableDate(request.getUnavailableDate())
                    .roomNumber(existingRoomNumber)
                    .builder();
            unavailableDateRepository.save(unavailableDate);
//            return new MessageResponse.Builder()
//                    .success(true)
//                    .message("Unavailable date successfully added.")
//                    .build();
        }
        else throw new RuntimeException("Error while adding unavailable date.");
    }

    public MessageResponse updateUnavailableDate(Integer hotelId, Integer roomTypeId,
                                                 Integer roomNumberId, Integer unavailableDateId,
                                                 UnavailableDateRequest request) {
        if(validUnavailableDate(hotelId, roomTypeId, roomNumberId, unavailableDateId) == true) {
            UnavailableDate existingUnavailableDate = unavailableDateRepository.findById(unavailableDateId).get();
            if(request.getUnavailableDate() != null)
                existingUnavailableDate.setUnavailableDate(request.getUnavailableDate());
            unavailableDateRepository.save(existingUnavailableDate);
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Unavailable date successfully updated")
                    .build();
        }
        else throw new RuntimeException("Error while updating the unavailable date.");
    }

    public MessageResponse deleteUnavailableDate(Integer hotelId, Integer roomTypeId, Integer roomNumberId, Integer unavailableDateId) {
        if(validUnavailableDate(hotelId, roomTypeId, roomNumberId, unavailableDateId) == true) {
            unavailableDateRepository.deleteById(unavailableDateId);
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Unavailable date successfully deleted")
                    .build();
        }
        else throw new RuntimeException("Error while deleting the unavailable date.");
    }

    public void deleteUnavailableDateByDateAndRoomNumberId(RoomNumber roomNumber, LocalDate date) {
        try {
            unavailableDateRepository.deleteUnavailableDateByDateAndRoomNumber(roomNumber, date);
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("Exception when deleting the unavailableDate: " + e.getMessage());
            throw new RuntimeException("Exception when deleting the unavailableDate", e);
        }
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

    private boolean validUnavailableDate(Integer hotelId, Integer roomTypeId, Integer roomNumberId,
                                         Integer unavailableDateId) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
        if(optionalHotel.isEmpty())
            throw new RuntimeException("Hotel not found with id of " + hotelId);
        if(optionalHotel.isPresent()) {
            Optional<RoomType> optionalRoomType = roomTypeRepository.findById(roomTypeId);
            if(optionalRoomType.isEmpty())
                throw new RuntimeException("Room type not found with id of " + roomTypeId);
            if(optionalRoomType.isPresent()) {
                if(optionalHotel.get().getId() != optionalRoomType.get().getHotel().getId())
                    throw new RuntimeException("Room type with id of " + roomTypeId + " doesn't belong to the hotel with id of "
                            + hotelId + ".");
                else if(optionalHotel.get().getId() == optionalRoomType.get().getHotel().getId()) {
                    Optional<RoomNumber> optionalRoomNumber = roomNumberRepository.findById(roomNumberId);
                    if(optionalRoomNumber.isEmpty())
                        throw new RuntimeException("Room number not found with id of " + roomNumberId + ".");
                    if(optionalRoomNumber.isPresent()) {
                        if(optionalRoomType.get().getId() != optionalRoomNumber.get().getRoomType().getId())
                            throw new RuntimeException("Room number with id of " + roomNumberId + " doesn't belong to the room type with id of "
                                    + roomTypeId + ".");
                        Optional<UnavailableDate> optionalUnavailableDate = unavailableDateRepository.findById(unavailableDateId);
                        if(optionalRoomType.isEmpty())
                            throw new RuntimeException("Unavailable date not found with id of " + unavailableDateId + ".");
                        if(optionalUnavailableDate.isPresent()) {
                            if(optionalRoomNumber.get().getId() != optionalUnavailableDate.get().getRoomNumber().getId())
                                throw new RuntimeException("Unavailable date with id of " + unavailableDateId + " doesn't belong to the room type with id of " +
                                roomTypeId + ".");
                            if(optionalRoomNumber.get().getId() == optionalUnavailableDate.get().getRoomNumber().getId())
                                return true;
                        }
                    }
                }
            }

        }
        return false;
    }

}
