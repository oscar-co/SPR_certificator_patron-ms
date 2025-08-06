package com.certificator.patron_ms.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.certificator.patron_ms.shared.dto.ApiResponse;

@RestController
@RequestMapping("/certificator/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<User>> register(@RequestBody UserDTO userDto) {
    try {
      User user = userService.registerUser(
          userDto.getUsername(),
          userDto.getPassword(),
          userDto.getEmail(),
          userDto.getRole()
      );

      ApiResponse<User> response = new ApiResponse<>(
          "success",
          "Usuario registrado correctamente",
          user
      );

      return ResponseEntity.ok(response);

    } catch (Exception e) {
      ApiResponse<User> response = new ApiResponse<>(
          "error",
          e.getMessage(),
          null
      );

      return ResponseEntity.badRequest().body(response);
    }
  }
}