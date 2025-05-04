package com.booking.app.service.impl;

import com.booking.app.dto.Response;
import com.booking.app.dto.RoomDto;
import com.booking.app.entity.Room;
import com.booking.app.exception.MyException;
import com.booking.app.repository.RoomRepository;
import com.booking.app.service.AwsS3Service;
import com.booking.app.service.interfac.IRoomService;
import com.booking.app.utils.Utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RoomService implements IRoomService {

  @Autowired
  private RoomRepository roomRepository;
  @Autowired
  private AwsS3Service awsS3Service;

  @Override
  public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
    Response response = new Response();

    try {
      String imageUrl = awsS3Service.saveImageToS3(photo);
      Room room = new Room();
      room.setRoomPhotoUrl(imageUrl);
      room.setRoomType(roomType);
      room.setRoomPrice(roomPrice);
      room.setRoomDescription(description);
      Room savedRoom = roomRepository.save(room);
      RoomDto roomDto = Utils.mapRoomEntityToRoomDto(savedRoom);

      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setRoom(roomDto);
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error saving a room: " + e.getMessage());
    }
    return response;
  }

  @Override
  public List<String> getAllRoomTypes() {
    return roomRepository.findDistinctRoomTypes();
  }

  public Response getAllRooms() {
    Response response = new Response();

    try {
      List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
      List<RoomDto> roomDtoList = Utils.mapRoomListEntityToRoomListDto(roomList);
      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setRoomList(roomDtoList);
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error saving a room: " + e.getMessage());
    }
    return response;
  }

  @Override
  public Response deleteRoom(Long roomId) {
    Response response = new Response();

    try {
      roomRepository.findById(roomId).orElseThrow(() -> new MyException("Room not found"));
      roomRepository.deleteById(roomId);
      response.setStatusCode(200);
      response.setMessage("Successful");
    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error deleting a room: " + e.getMessage());
    }
    return response;
  }

  @Override
  public Response updateRoom(Long roomId, MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
    Response response = new Response();

    try {
      String imageUrl = null;
      if (photo != null && !photo.isEmpty()) {
        imageUrl = awsS3Service.saveImageToS3(photo);
      }

      Room room = roomRepository.findById(roomId).orElseThrow(() -> new MyException("Room not found"));
      if (roomType != null) room.setRoomType(roomType);
      if (roomPrice != null) room.setRoomPrice(roomPrice);
      if (description != null) room.setRoomDescription(description);
      if (imageUrl != null) room.setRoomPhotoUrl(imageUrl);

      Room updatedRoom = roomRepository.save(room);
      RoomDto roomDto = Utils.mapRoomEntityToRoomDto(updatedRoom);

      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setRoom(roomDto);
    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error updating a room: " + e.getMessage());
    }
    return response;
  }

  @Override
  public Response getRoomById(Long roomId) {
    Response response = new Response();

    try {
      Room room = roomRepository.findById(roomId).orElseThrow(() -> new MyException("Room not found"));
      RoomDto roomDto = Utils.mapRoomEntityToRoomDtoPlusBookings(room);

      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setRoom(roomDto);
    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error getting a room: " + e.getMessage());
    }
    return response;
  }

  @Override
  public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
    Response response = new Response();

    try {
      List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);
      List<RoomDto> roomDtoList = Utils.mapRoomListEntityToRoomListDto(availableRooms);

      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setRoomList(roomDtoList);
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error getting rooms: " + e.getMessage());
    }
    return response;
  }

  @Override
  public Response getAllAvailableRooms() {
    Response response = new Response();

    try {
      List<Room> roomList = roomRepository.getAllAvailableRooms();
      List<RoomDto> roomDtoList = Utils.mapRoomListEntityToRoomListDto(roomList);
      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setRoomList(roomDtoList);
    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error getting a room: " + e.getMessage());
    }
    return response;
  }
}