package com.consultify.model;

import java.time.LocalDateTime;

public abstract class UserModel {
  private String username;
  private String password;
  private String email;
  private String fullName;
  private LocalDateTime createdAt;

  public UserModel(String username, String password, String email, String fullName, LocalDateTime createdAt) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.fullName = fullName;
    this.createdAt = createdAt;
  }

  // Getters and setters
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "UserModel{" +
        "username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", fullName='" + fullName + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}
