package com.example.demo.business.roomNumber;

import com.example.demo.MessageResponse;
import com.example.demo.business.hotel.Hotel;
import com.example.demo.business.hotel.HotelRepository;
import com.example.demo.business.roomType.RoomType;
import com.example.demo.business.roomType.RoomTypeRepository;
import com.example.demo.business.roomType.RoomTypeRequest;;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomNumberService {
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomNumberRepository roomNumberRepository;

    public RoomNumberService(HotelRepository hotelRepository,
                             RoomTypeRepository roomTypeRepository,
                             RoomNumberRepository roomNumberRepository) {
        this.hotelRepository = hotelRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.roomNumberRepository = roomNumberRepository;
    }

    public RoomNumberResponse getOneRoomNumber(Integer hotelId, Integer roomTypeId, Integer roomNumberId) {
        if(validRoomNumber(hotelId, roomTypeId, roomNumberId) == true) {
            RoomNumber existingRoomNumber = roomNumberRepository.findById(roomNumberId).get();
            return new RoomNumberResponse.Builder()
                    .id(existingRoomNumber.getId())
                    .number(existingRoomNumber.getNumber())
                    .build();
        }

        else throw new RuntimeException("Error while fetching the room number.");
    }

    public List<RoomNumberResponse> getAllRoomsNumber (Integer hotelId, Integer roomTypeId) {
        if(validRoomTypeOfHotel(hotelId, roomTypeId) == true) {
            List<RoomNumberResponse> roomsNumberResponseList = new ArrayList<>();
            RoomType existingRoomType = roomTypeRepository.findById(roomTypeId).get();
            List<RoomNumber> existingListRoomsNumber = roomNumberRepository.findByRoomType(existingRoomType).get();
            existingListRoomsNumber.forEach(
                    existingRoomNumber -> {
                        roomsNumberResponseList.add(
                                new RoomNumberResponse.Builder()
                                        .id(existingRoomNumber.getId())
                                        .number(existingRoomNumber.getNumber())
                                        .build()
                        );
                    }
            );
            return roomsNumberResponseList;
        }

        else throw new RuntimeException("Error while fetching all the rooms' numbers.");
    }

    public MessageResponse createRoomNumber(Integer hotelId, Integer roomTypeId, RoomNumberRequest request) {
//        check if exists hotelId and roomTypeId
        if(validRoomTypeOfHotel(hotelId, roomTypeId) == true) {
            RoomNumber roomNumber = new RoomNumber.Builder()
                    .number(request.getNumber())
                    .roomType(roomTypeRepository.findById(roomTypeId).get())
                    .build();
            roomNumberRepository.save(roomNumber);
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Room number successfully created.")
                    .build();
        }
        else throw new RuntimeException("Wrong hotelId and/or roomTypeId");
    }

    public MessageResponse updateRoomNumber(Integer hotelId, Integer roomTypeId, Integer roomNumberId,
                                            RoomNumberRequest request) {
        if(validRoomNumber(hotelId, roomTypeId, roomNumberId) == true) {
            RoomNumber existingRoomNumber = roomNumberRepository.findById(roomNumberId).get();
            if(request.getNumber() != null)
                existingRoomNumber.setNumber(request.getNumber());
            roomNumberRepository.save(existingRoomNumber);
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Room number successfully updated.")
                    .build();
        }

        else throw new RuntimeException("Error while updating the room number.");
    }

    public MessageResponse deleteRoomNumber(Integer hotelId, Integer roomTypeId, Integer roomNumberId) {
        if(validRoomNumber(hotelId, roomTypeId, roomNumberId) == true) {
            roomNumberRepository.deleteById(roomNumberId);
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Room number successfully deleted.")
                    .build();
        }
        else
            throw new RuntimeException("Error while deleting the room number.");
    }

    private boolean validRoomTypeOfHotel(Integer hotelId, Integer roomTypeId) {
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
                else if(optionalHotel.get().getId() == optionalRoomType.get().getHotel().getId())
                    return true;
            }

        }
        return false;
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
}
