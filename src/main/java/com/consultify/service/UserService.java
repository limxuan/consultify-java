package com.consultify.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import com.consultify.utils.TimeUtils;

public class UserService {
  private DatabaseService studentDatabaseService = new DatabaseService("students.txt");
  private DatabaseService lecturerDatabaseService = new DatabaseService("lecturers.txt");

  public String registerStudent(String username, String password, String fullName, String email, String phone) {
    ArrayList<String[]> loginCredentials = studentDatabaseService.parseContent();
    if (loginCredentials.stream().anyMatch(credential -> credential[1].equals(username))) {
      return "Username already exists";
    }
    String studentId = UUID.randomUUID().toString();
    String createdAt = TimeUtils.getCurrentTimeInISO();
    // format: studentId|username|password|email|phone|createdAt|fullName
    loginCredentials.add(new String[] { studentId, username, password, email, phone, createdAt, fullName });
    studentDatabaseService.saveData(loginCredentials);
    return "Registered successfully!";
  }

  // lecturerId|username|password|email|fullName|officeLocation|createdAt
  public String registerLecturer(String username, String password, String email, String phone, String fullName,
      String officeLocation) {
    ArrayList<String[]> loginCredentials = lecturerDatabaseService.parseContent();
    if (loginCredentials.stream().anyMatch(credential -> credential[1].equals(username))) {
      return "Username already exists";
    }
    String lecturerId = UUID.randomUUID().toString();
    String createdAt = TimeUtils.getCurrentTimeInISO();
    // format: lecturerId|username|password|email|fullName|officeLocation|createdAt
    loginCredentials
        .add(new String[] { lecturerId, username, password, email, phone, fullName, officeLocation, createdAt });

    // TODO: remove later
    for (String[] x : loginCredentials) {
      System.out.println(Arrays.toString(x));
    }
    //

    lecturerDatabaseService.saveData(loginCredentials);
    return "Registered successfully!";

  }

  public String[] loginStudent(String username, String password) {
    System.out.println("Username: " + username);
    System.out.println("Password: " + password);
    ArrayList<String[]> loginCredentials = studentDatabaseService.parseContent();

    for (String[] loginCredential : loginCredentials) {
      if (loginCredential[1].equals(username) && loginCredential[2].equals(password)) {
        // index 6 is fullname
        UserSession.setSession(loginCredential[0], username, "student", loginCredential[6]);
        return loginCredential;
      }
    }
    return null;
  }

  public String[] loginLecturer(String username, String password) {
    ArrayList<String[]> loginCredentials = lecturerDatabaseService.parseContent();
    for (String[] loginCredential : loginCredentials) {
      if (loginCredential[1].equals(username) && loginCredential[2].equals(password)) {
        UserSession.setSession(loginCredential[0], username, "lecturer", loginCredential[5]);
        return loginCredential;
      }
    }
    return null;
  }

  public String[] getLecturerById(String lecturerId) {
    ArrayList<String[]> records = lecturerDatabaseService.parseContent();
    for (String[] record : records) {
      if (record[0].equals(lecturerId)) {
        return record;
      }
    }
    return null;
  }

  public String[] getStudentById(String studentId) {
    ArrayList<String[]> records = studentDatabaseService.parseContent();
    for (String[] record : records) {
      if (record[0].equals(studentId)) {
        return record;
      }
    }
    return null;
  }
}
