package com.consultify.model;

import java.time.LocalDateTime;

public class StudentModel extends UserModel {
  private String studentId;
  private String phone;

  public StudentModel(String studentId, String username, String password, String email, String phone,
      LocalDateTime createdAt, String fullName) {
    super(username, password, email, fullName, createdAt);
    this.studentId = studentId;
    this.phone = phone;
  }

  // Getters and setters
  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Override
  public String toString() {
    return "StudentModel{" +
        "studentId='" + studentId + '\'' +
        ", phone='" + phone + '\'' +
        "} " + super.toString();
  }
}
