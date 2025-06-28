package com.certificator.patron_ms.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User registerUser(String username, String password, String email, String role) {
    if (userRepository.existsByUsername(username)) {
      throw new RuntimeException("Username already exists");
    }

    if (userRepository.existsByEmail(email)) {
      throw new RuntimeException("Email already registered");
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setEmail(email);
    user.setRole(role != null && !role.isBlank() ? role : "ROLE_USER");

    return userRepository.save(user);
  }
}

