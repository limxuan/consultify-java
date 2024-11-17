package com.consultify.service;

public class UserService {
  public boolean validateStudent(String username, String password) {
    if (username.equals("student")) {
      return true;
    } else {
      return false;
    }
  }

  public boolean validateLecturer(String username, String password) {
    if (username.equals("lecturer")) {
      return true;
    } else {
      return false;
    }
  }
}
