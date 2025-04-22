package com.booking.app.service.interfac;

import com.booking.app.dto.LoginRequest;
import com.booking.app.dto.Response;
import com.booking.app.entity.User;

public interface IUserService {
  Response register(User user);

  Response login(LoginRequest loginRequest);

  Response getAllUsers();

  Response getUserBookingHistory(String userId);

  Response deleteUser(String userId);

  Response getUserById(String userId);

  Response getMyInfo(String email);
}