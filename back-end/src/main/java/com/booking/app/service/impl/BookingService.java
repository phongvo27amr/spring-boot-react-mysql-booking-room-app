package com.booking.app.service.impl;

import com.booking.app.dto.BookingDto;
import com.booking.app.dto.Response;
import com.booking.app.entity.Booking;
import com.booking.app.entity.Room;
import com.booking.app.entity.User;
import com.booking.app.exception.MyException;
import com.booking.app.repository.BookingRepository;
import com.booking.app.repository.RoomRepository;
import com.booking.app.repository.UserRepository;
import com.booking.app.service.interfac.IBookingService;
import com.booking.app.utils.Utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookingService implements IBookingService {
  @Autowired
  private BookingRepository bookingRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoomRepository roomRepository;

  @Override
  public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
    Response response = new Response();

    try {
      if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
        throw new IllegalArgumentException("Check in date must come before check out date.");
      }
      Room room = roomRepository.findById(roomId).orElseThrow(() -> new MyException("Room not found."));
      User user = userRepository.findById(userId).orElseThrow(() -> new MyException("User not found."));

      List<Booking> existingBookings = room.getBookings();

      if (!roomIsAvailable(bookingRequest, existingBookings)) {
        throw new MyException("Room not available for selected date range.");
      }

      bookingRequest.setRoom(room);
      bookingRequest.setUser(user);
      String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
      bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
      bookingRepository.save(bookingRequest);
      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setBookingConfirmationCode(bookingConfirmationCode);

    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error saving a booking:" + e.getMessage());
    }
    return response;
  }

  @Override
  public Response findBookingByConfirmationCode(String confirmationCode) {
    Response response = new Response();

    try {
      Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new MyException("Booking not found."));
      BookingDto bookingDto = Utils.mapBookingEntityToBookingDtoPlusBookedRooms(booking, true);
      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setBooking(bookingDto);

    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error finding a booking:" + e.getMessage());
    }
    return response;
  }

  @Override
  public Response getAllBookings() {
    Response response = new Response();

    try {
      List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
      List<BookingDto> bookingDtoList = Utils.mapBookingListEntityToBookingListDto(bookingList);
      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setBookingList(bookingDtoList);

    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error getting all bookings:" + e.getMessage());
    }
    return response;
  }

  @Override
  public Response cancelBooking(Long bookingId) {
    Response response = new Response();

    try {
      bookingRepository.findById(bookingId).orElseThrow(() -> new MyException("Booking does not exist."));
      bookingRepository.deleteById(bookingId);
      response.setStatusCode(200);
      response.setMessage("Successful");

    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error cancelling a booking:" + e.getMessage());
    }
    return response;
  }

  boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
    return existingBookings.stream().noneMatch(existingBooking ->
      bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
    );
  }
}