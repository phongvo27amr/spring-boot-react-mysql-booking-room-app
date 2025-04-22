package com.booking.app.service.interfac;

import com.booking.app.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IRoomService {
  Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description);

  List<String> getAllRoomTypes();

  Response getAllRooms();

  Response deleteRoom(Long roomId);

  Response updateRoom(Long roomId, MultipartFile photo, String roomType, BigDecimal roomPrice, String description);

  Response getRoomById(Long roomId);

  Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

  Response getAllAvailableRooms();
}