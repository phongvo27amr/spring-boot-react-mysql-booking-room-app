package com.booking.app.service.interfac;

import com.booking.app.dto.Response;
import com.booking.app.entity.Booking;

public interface IBookingService {
  Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
  Response findBookingByConfirmationCode(String confirmationCode);
  Response getAllBookings();
  Response cancelBooking(Long bookingId);
}