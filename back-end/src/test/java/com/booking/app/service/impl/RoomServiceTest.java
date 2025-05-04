package com.booking.app.service.impl;

import com.booking.app.dto.Response;
import com.booking.app.dto.RoomDto;
import com.booking.app.entity.Room;
import com.booking.app.repository.RoomRepository;
import com.booking.app.service.AwsS3Service;
import com.booking.app.utils.Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceTest {
  @InjectMocks
  private RoomService roomService;

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private AwsS3Service awsS3Service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAvailableRooms_Success() {
    // Arrange
    LocalDate checkIn = LocalDate.of(2025, 5, 10);
    LocalDate checkOut = LocalDate.of(2025, 5, 15);
    String roomType = "DELUXE";

    Room room = new Room();
    room.setId(1L);
    room.setRoomType("DELUXE");
    room.setRoomPrice(BigDecimal.valueOf(120));
    List<Room> roomList = Collections.singletonList(room);

    RoomDto roomDto = new RoomDto();
    roomDto.setId(1L);
    roomDto.setRoomType("DELUXE");
    roomDto.setRoomPrice(BigDecimal.valueOf(120));

    // Mock repository and util
    when(roomRepository.findAvailableRoomsByDatesAndTypes(checkIn, checkOut, roomType)).thenReturn(roomList);
    mockStatic(Utils.class).when(() -> Utils.mapRoomListEntityToRoomListDto(roomList)).thenReturn(List.of(roomDto));

    // Act
    Response response = roomService.getAvailableRoomsByDateAndType(checkIn, checkOut, roomType);

    // Assert
    assertEquals(200, response.getStatusCode());
    assertEquals("Successful", response.getMessage());
    assertNotNull(response.getRoomList());
    assertEquals(1, response.getRoomList().size());
    assertEquals("DELUXE", response.getRoomList().get(0).getRoomType());
  }

  @Test
  void testGetAvailableRooms_InternalServerError() {
    // Arrange
    LocalDate checkIn = LocalDate.of(2025, 5, 10);
    LocalDate checkOut = LocalDate.of(2025, 5, 15);
    String roomType = "STANDARD";

    when(roomRepository.findAvailableRoomsByDatesAndTypes(checkIn, checkOut, roomType))
        .thenThrow(new RuntimeException("Database error"));

    // Act
    Response response = roomService.getAvailableRoomsByDateAndType(checkIn, checkOut, roomType);

    // Assert
    assertEquals(500, response.getStatusCode());
    assertTrue(response.getMessage().contains("Error getting rooms"));
    assertNull(response.getRoomList());
  }
}