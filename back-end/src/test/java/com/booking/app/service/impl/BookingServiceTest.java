package com.booking.app.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.booking.app.dto.Response;
import com.booking.app.entity.Booking;
import com.booking.app.entity.Room;
import com.booking.app.entity.User;
import com.booking.app.repository.BookingRepository;
import com.booking.app.repository.RoomRepository;
import com.booking.app.repository.UserRepository;
import com.booking.app.utils.Utils;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  @InjectMocks
  private BookingService bookingService;

  @Mock
  private BookingRepository bookingRepository;

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private UserRepository userRepository;

  @Test
  void testSaveBooking_Success() {
    Booking booking = new Booking();
    booking.setCheckInDate(LocalDate.now().plusDays(1));
    booking.setCheckOutDate(LocalDate.now().plusDays(3));

    Room room = new Room();
    room.setBookings(List.of()); // No existing bookings

    User user = new User();

    when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
    when(userRepository.findById(2L)).thenReturn(Optional.of(user));
    try (MockedStatic<Utils> mocked = mockStatic(Utils.class)) {
      mocked.when(() -> Utils.generateRandomConfirmationCode(10)).thenReturn("27ARV8G777");
      Response response = bookingService.saveBooking(1L, 2L, booking);

      assertEquals(200, response.getStatusCode());
      assertEquals("Successful", response.getMessage());
      assertEquals("27ARV8G777", response.getBookingConfirmationCode());

      verify(bookingRepository).save(booking);
    }
  }

  @Test
  void testSaveBooking_RoomNotFound() {
    Booking booking = new Booking();
    booking.setCheckInDate(LocalDate.now().plusDays(1));
    booking.setCheckOutDate(LocalDate.now().plusDays(3));

    when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

    Response response = bookingService.saveBooking(1L, 2L, booking);

    assertEquals(404, response.getStatusCode());
    assertEquals("Room not found.", response.getMessage());
  }

  @Test
  void testSaveBooking_UserNotFound() {
    Booking booking = new Booking();
    booking.setCheckInDate(LocalDate.now().plusDays(1));
    booking.setCheckOutDate(LocalDate.now().plusDays(3));

    Room room = new Room();
    room.setBookings(List.of());

    when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
    when(userRepository.findById(2L)).thenReturn(Optional.empty());

    Response response = bookingService.saveBooking(1L, 2L, booking);

    assertEquals(404, response.getStatusCode());
    assertEquals("Room not found.", response.getMessage());
  }

  @Test
  void testSaveBooking_InvalidDateRange() {
    Booking booking = new Booking();
    booking.setCheckInDate(LocalDate.now().plusDays(5));
    booking.setCheckOutDate(LocalDate.now().plusDays(2)); // Invalid

    Response response = bookingService.saveBooking(1L, 2L, booking);

    assertEquals(500, response.getStatusCode());
    assertTrue(response.getMessage().contains("Error saving a booking"));
  }

  @Test
  void testSaveBooking_RoomUnavailable() {
    Booking booking = new Booking();
    booking.setCheckInDate(LocalDate.now().plusDays(1));
    booking.setCheckOutDate(LocalDate.now().plusDays(3));

    Booking conflicting = new Booking();
    conflicting.setCheckInDate(LocalDate.now());
    conflicting.setCheckOutDate(LocalDate.now().plusDays(2));

    Room room = new Room();
    room.setBookings(List.of(conflicting));

    User user = new User();

    when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
    when(userRepository.findById(2L)).thenReturn(Optional.of(user));

    // Mock roomIsAvailable to false
    BookingService spyService = Mockito.spy(bookingService);
    doReturn(false).when(spyService).roomIsAvailable(eq(booking), anyList());

    Response response = spyService.saveBooking(1L, 2L, booking);

    assertEquals(404, response.getStatusCode());
    assertEquals("Room not available for selected date range.", response.getMessage());
  }
}