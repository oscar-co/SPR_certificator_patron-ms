package com.certificator.patron_ms.user;

public class UserDTO {
  private String username;
  private String password;
  private String email;
  private String role;

  public UserDTO() {}

  public UserDTO(String username, String password, String email, String role) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
  }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }
}
