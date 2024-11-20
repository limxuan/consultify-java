package com.consultify.model;

import java.time.LocalDateTime;

public class LecturerModel extends UserModel {
  private String lecturerId;
  private String officeLocation;

  public LecturerModel(String lecturerId, String username, String password, String email, String officeLocation,
      LocalDateTime createdAt, String fullName) {
    super(username, password, email, fullName, createdAt);
    this.lecturerId = lecturerId;
    this.officeLocation = officeLocation;
  }

  // Getters and setters
  public String getLecturerId() {
    return lecturerId;
  }

  public void setLecturerId(String lecturerId) {
    this.lecturerId = lecturerId;
  }

  public String getOfficeLocation() {
    return officeLocation;
  }

  public void setOfficeLocation(String officeLocation) {
    this.officeLocation = officeLocation;
  }

  @Override
  public String toString() {
    return "LecturerModel{" +
        "lecturerId='" + lecturerId + '\'' +
        ", officeLocation='" + officeLocation + '\'' +
        "} " + super.toString();
  }
}
