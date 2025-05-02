package com.booking.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.booking.app.dto.LoginRequest;
import com.booking.app.dto.Response;
import com.booking.app.dto.UserDto;
import com.booking.app.entity.User;
import com.booking.app.exception.MyException;
import com.booking.app.repository.UserRepository;
import com.booking.app.service.interfac.IUserService;
import com.booking.app.utils.JwtUtils;
import com.booking.app.utils.Utils;

@Service
public class UserService implements IUserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private AuthenticationManager authenticationManager;


  @Override
  public Response register(User user) {
    Response response = new Response();
    try {
      if (user.getRole() == null || user.getRole().isBlank()) {
        user.setRole("USER");
      }
      if (userRepository.existsByEmail(user.getEmail())) {
        throw new MyException(user.getEmail() + " already exists.");
      }
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      User savedUser = userRepository.save(user);

      UserDto userDto = Utils.mapUserEntityToUserDto(savedUser);
      response.setStatusCode(200);
      response.setUser(userDto);
    } catch (MyException e) {
      response.setStatusCode(400);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error occurred during user registration: " + e.getMessage());
    }
    return response;
  }

  @Override
  public Response login(LoginRequest loginRequest) {
    Response response = new Response();
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

      var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new MyException("User not found"));

      var token = jwtUtils.generateToken(user);
      response.setStatusCode(200);
      response.setToken(token);
      response.setRole(user.getRole());
      response.setExpirationTime("7 Days");
      response.setMessage("Successful");
    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error occurred during user login: " + e.getMessage());
    }
    return response;
  }

  @Override
  public Response getAllUsers() {
    Response response = new Response();
    try {
      List<User> userList = userRepository.findAll();
      List<UserDto> userDtoList = Utils.mapUserListEntityToUserListDto(userList);
      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setUserList(userDtoList);
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error getting all users: " + e.getMessage());
    }
    return response;
  }

  @Override
  public Response getUserBookingHistory(String userId) {
    Response response = new Response();
    try {
      User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new MyException("User not found"));
      UserDto userDto = Utils.mapUserEntityToUserDtoPlusUserBookingsAndRoom(user);
      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setUser(userDto);
    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error getting user booking history: " + e.getMessage());
    }
    return response;
  }

  @Override
  public Response deleteUser(String userId) {
    Response response = new Response();
    try {
      userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new MyException("User not found"));
      userRepository.deleteById(Long.valueOf(userId));
      response.setStatusCode(200);
      response.setMessage("Successful");
    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error occurred during user login: " + e.getMessage());
    }
    return response;
  }

  @Override
  public Response getUserById(String userId) {
    Response response = new Response();
    try {
      User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new MyException("User not found"));
      UserDto userDto = Utils.mapUserEntityToUserDto(user);

      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setUser(userDto);
    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error getting user: " + e.getMessage());
    }
    return response;
  }

  @Override
  public Response getMyInfo(String email) {
    Response response = new Response();
    try {
      User user = userRepository.findByEmail(email).orElseThrow(() -> new MyException("User not found"));
      UserDto userDto = Utils.mapUserEntityToUserDto(user);

      response.setStatusCode(200);
      response.setMessage("Successful");
      response.setUser(userDto);
    } catch (MyException e) {
      response.setStatusCode(404);
      response.setMessage(e.getMessage());
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setMessage("Error getting user info: " + e.getMessage());
    }
    return response;
  }
}