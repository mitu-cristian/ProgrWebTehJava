package com.example.demo.business.roomType;

import com.example.demo.MessageResponse;
import com.example.demo.business.hotel.Hotel;
import com.example.demo.business.hotel.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeService {
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeService(HotelRepository hotelRepository, RoomTypeRepository roomTypeRepository) {
        this.hotelRepository = hotelRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    public RoomTypeResponse getOneRoomType(Integer hotelId, Integer roomTypeId) {
        if(validRoomTypeOfHotel(hotelId, roomTypeId) == true) {
            RoomType existingRoomType = roomTypeRepository.findById(roomTypeId).get();
            return new RoomTypeResponse.Builder()
                    .id(existingRoomType.getId())
                    .name(existingRoomType.getName())
                    .price(existingRoomType.getPrice())
                    .maxPeople(existingRoomType.getMaxPeople())
                    .roomNumbers(existingRoomType.getRoomNumbers())
                    .build();
        }
        else
            throw new RuntimeException("Error while fetching the room type with id of " + roomTypeId + ".");
    }

    public List<RoomTypeResponse> getAllRoomsType(Integer hotelId) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
        if(optionalHotel.isPresent()) {
            List<RoomTypeResponse> roomsTypeResponseList = new ArrayList<>();
            List<RoomType> existingListRoomsType = roomTypeRepository.findByHotel(optionalHotel.get()).get();
            existingListRoomsType.forEach(
                    existingRoomType ->
                    {
                        roomsTypeResponseList.add(
                                new RoomTypeResponse.Builder()
                                        .id(existingRoomType.getId())
                                        .name(existingRoomType.getName())
                                        .price(existingRoomType.getPrice())
                                        .maxPeople(existingRoomType.getMaxPeople())
                                        .roomNumbers(existingRoomType.getRoomNumbers())
                                        .build()
                        );
                    }
            );
            return roomsTypeResponseList;
        }
        else
            throw new RuntimeException("Error while fetching all the room types");
    }

    public MessageResponse createRoomType(Integer hotelId, RoomTypeRequest request) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
        if(hotelOptional.isPresent()) {
            RoomType roomType = new RoomType.Builder()
                    .name(request.getName())
                    .price(request.getPrice())
                    .maxPeople(request.getMaxPeople())
                    .hotel(hotelOptional.get())
                    .build();
            roomTypeRepository.save(roomType);
            return new MessageResponse.Builder()
                    .message("Room type successfully added.")
                    .success(true)
                    .build();
        }
        else throw new RuntimeException("Hotel not found with id of " + hotelId);
    }

    public MessageResponse updateRoomType(Integer hotelId, Integer roomTypeId, RoomTypeRequest request) {
        if(validRoomTypeOfHotel(hotelId, roomTypeId)) {
            RoomType existingRoomType = roomTypeRepository.findById(roomTypeId).get();
            if(request.getName() != null)
                existingRoomType.setName(request.getName());
            if(request.getPrice() != null)
                existingRoomType.setPrice(request.getPrice());
            if(request.getMaxPeople() != null)
                existingRoomType.setMaxPeople(request.getMaxPeople());
            roomTypeRepository.save(existingRoomType);
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Room type successfully updated.")
                    .build();
        }
        else
            return new MessageResponse.Builder()
                    .success(false)
                    .message("Error while updating the room type.")
                    .build();
    }

    public MessageResponse deleteRoomType(Integer hotelId, Integer roomTypeId) {
//        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
//        if(hotelOptional.isPresent()) {
//            Optional<RoomType> optionalRoomType = roomTypeRepository.findById(roomTypeId);
//            if(optionalRoomType.isPresent()) {
//                if(hotelOptional.get().getId() == optionalRoomType.get().getHotel().getId()) {
//                    roomTypeRepository.deleteById(optionalRoomType.get().getId());
//                    return new MessageResponse.Builder()
//                            .message("Room type successfully deleted.")
//                            .success(true)
//                            .build();
//                }
//                else if(hotelOptional.get().getId() != optionalRoomType.get().getHotel().getId())
//                    throw new RuntimeException("Room type with id of " + roomTypeId + " doesn't belong to the"
//                            + "hotel with id of " + hotelId);
//            }
//            else if(optionalRoomType.isEmpty())
//                throw new RuntimeException("Room type not found with id of " + roomTypeId);
//        }
//        else if(hotelOptional.isEmpty())
//            throw new RuntimeException("Hotel not found with id of " + hotelId);
//        return new MessageResponse.Builder()
//                .message("Error while deleting the room type.")
//                .success(false)
//                .build();
        if (validRoomTypeOfHotel(hotelId, roomTypeId)==true) {
            roomTypeRepository.delete(roomTypeRepository.findById(roomTypeId).get());
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Room type successfully deleted")
                    .build();
        }
        else throw new RuntimeException("Error while fetching the room type.");
//        else return new MessageResponse.Builder()
//                .success(false)
//                .message("Error while deleting the room.")
//                .build();
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

    }

