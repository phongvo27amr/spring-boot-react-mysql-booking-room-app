package com.booking.app.service.impl;

import com.booking.app.dto.LoginRequest;
import com.booking.app.dto.Response;
import com.booking.app.entity.User;
import com.booking.app.repository.UserRepository;
import com.booking.app.utils.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {
  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private JwtUtils jwtUtils;

  @Mock
  private AuthenticationManager authenticationManager;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSuccessfulLogin() {
    // Arrange
    LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

    User user = new User();
    user.setEmail("test@example.com");
    user.setRole("USER");
    String token = "dummy.jwt.token";

    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
    when(jwtUtils.generateToken(user)).thenReturn(token);

    // Act
    Response response = userService.login(loginRequest);

    // Assert
    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    assertEquals(200, response.getStatusCode());
    assertEquals("Successful", response.getMessage());
    assertEquals(token, response.getToken());
    assertEquals("7 Days", response.getExpirationTime());
    assertEquals("USER", response.getRole());
  }

  @Test
  void testUserNotFound() {
    // Arrange
    LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

    when(authenticationManager.authenticate(any()))
        .thenReturn(mock(org.springframework.security.core.Authentication.class));

    when(userRepository.findByEmail("test@example.com"))
        .thenReturn(Optional.empty());

    // Act
    Response response = userService.login(loginRequest);

    // Assert
    assertEquals(404, response.getStatusCode());
    assertEquals("User not found", response.getMessage());
  }

  @Test
  void testLoginFailureWithException() {
    // Arrange
    LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongpassword");

    when(authenticationManager.authenticate(any()))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    // Act
    Response response = userService.login(loginRequest);

    // Assert
    assertEquals(500, response.getStatusCode());
    assertTrue(response.getMessage().contains("Error occurred during user login"));
  }
}