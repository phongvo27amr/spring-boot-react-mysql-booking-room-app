package com.booking.app.utils;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import com.booking.app.dto.BookingDto;
import com.booking.app.dto.RoomDto;
import com.booking.app.dto.UserDto;
import com.booking.app.entity.Booking;
import com.booking.app.entity.Room;
import com.booking.app.entity.User;

public class Utils {
  private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final SecureRandom secureRandom = new SecureRandom();

  public static String generateRandomConfirmationCode(int length) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
      char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
      stringBuilder.append(randomChar);
    }
    return stringBuilder.toString();
  }

  public static UserDto mapUserEntityToUserDto(User user) {
    UserDto userDto = new UserDto();

    userDto.setId(user.getId());
    userDto.setName(user.getName());
    userDto.setEmail(user.getEmail());
    userDto.setPhoneNumber(user.getPhoneNumber());
    userDto.setRole(user.getRole());
    return userDto;
  }

  public static RoomDto mapRoomEntityToRoomDto(Room room) {
    RoomDto roomDto = new RoomDto();

    roomDto.setId(room.getId());
    roomDto.setRoomType(room.getRoomType());
    roomDto.setRoomPrice(room.getRoomPrice());
    roomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());
    return roomDto;
  }

  public static BookingDto mapBookingEntityToBookingDto(Booking booking) {
    BookingDto bookingDto = new BookingDto();

    bookingDto.setId(booking.getId());
    bookingDto.setCheckInDate(booking.getCheckInDate());
    bookingDto.setCheckOutDate(booking.getCheckOutDate());
    bookingDto.setNumOfAdults(booking.getNumOfAdults());
    bookingDto.setNumOfChildren(booking.getNumOfChildren());
    bookingDto.setTotalNumOfGuest(booking.getTotalNumOfGuest());
    bookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());
    return bookingDto;
  }

  public static RoomDto mapRoomEntityToRoomDtoPlusBookings(Room room) {
    RoomDto roomDto = new RoomDto();

    roomDto.setId(room.getId());
    roomDto.setRoomType(room.getRoomType());
    roomDto.setRoomPrice(room.getRoomPrice());
    roomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());
    roomDto.setRoomDescription(room.getRoomDescription());

    if (room.getBookings() != null) {
      roomDto.setBookings(room.getBookings().stream().map(Utils::mapBookingEntityToBookingDto).collect(Collectors.toList()));
    }
    return roomDto;
  }

  public static UserDto mapUserEntityToUserDtoPlusUserBookingsAndRoom(User user) {
    UserDto userDto = new UserDto();

    userDto.setId(user.getId());
    userDto.setName(user.getName());
    userDto.setEmail(user.getEmail());
    userDto.setPhoneNumber(user.getPhoneNumber());
    userDto.setRole(user.getRole());

    if (!user.getBookings().isEmpty()) {
      userDto.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDtoPlusBookedRooms(booking, false)).collect(Collectors.toList()));
    }
    return userDto;
  }

  public static BookingDto mapBookingEntityToBookingDtoPlusBookedRooms(Booking booking, boolean mapUser) {
    BookingDto bookingDto = new BookingDto();

    bookingDto.setId(booking.getId());
    bookingDto.setCheckInDate(booking.getCheckInDate());
    bookingDto.setCheckOutDate(booking.getCheckOutDate());
    bookingDto.setNumOfAdults(booking.getNumOfAdults());
    bookingDto.setNumOfChildren(booking.getNumOfChildren());
    bookingDto.setTotalNumOfGuest(booking.getTotalNumOfGuest());
    bookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());

    if (mapUser) {
      bookingDto.setUser(Utils.mapUserEntityToUserDto(booking.getUser()));
    }

    if (booking.getRoom() != null) {
      RoomDto roomDto = new RoomDto();

      roomDto.setId(booking.getRoom().getId());
      roomDto.setRoomType(booking.getRoom().getRoomType());
      roomDto.setRoomPrice(booking.getRoom().getRoomPrice());
      roomDto.setRoomPhotoUrl(booking.getRoom().getRoomPhotoUrl());
      roomDto.setRoomDescription(booking.getRoom().getRoomDescription());
      bookingDto.setRoom(roomDto);
    }

    return bookingDto;
  }

  public static List<UserDto> mapUserListEntityToUserListDto(List<User> userList) {
    return userList.stream().map(Utils::mapUserEntityToUserDto).collect(Collectors.toList());
  }

  public static List<RoomDto> mapRoomListEntityToRoomListDto(List<Room> roomList) {
    return roomList.stream().map(Utils::mapRoomEntityToRoomDto).collect(Collectors.toList());
  }

  public static List<BookingDto> mapBookingListEntityToBookingListDto(List<Booking> bookingList) {
    return bookingList.stream().map(Utils::mapBookingEntityToBookingDto).collect(Collectors.toList());
  }
}