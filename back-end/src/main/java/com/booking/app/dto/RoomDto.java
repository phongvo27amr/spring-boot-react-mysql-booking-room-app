package com.booking.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class RoomDto {
  private Long id;
  private String roomType;
  private BigDecimal roomPrice;
  private String roomPhotoUrl;
  private String roomDescription;
  private List<BookingDto> bookings;
}