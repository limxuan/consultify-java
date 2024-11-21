package com.consultify.service;

public class UserSession {
  private static String userId;
  private static String username;
  private static String role;
  private static String fullName;

  private UserSession() {
  }

  public static void setSession(String userId, String username, String role, String fullName) {
    if (userId == null || userId.isEmpty() || username == null || username.isEmpty() || role == null || role.isEmpty()
        || fullName == null
        || fullName.isEmpty()) {
      throw new IllegalArgumentException("All fields must be provided");
    }
    UserSession.userId = userId;
    UserSession.username = username;
    UserSession.role = role;
    UserSession.fullName = fullName;
  }

  public static String getUserId() {
    return userId;
  }

  public static String getUsername() {
    return username;
  }

  public static String getRole() {
    return role;
  }

  public static String getFullName() {
    return fullName;
  }

  public static Boolean isLecturer() {
    return "lecturer".equals(role);
  }

  public static Boolean isStudent() {
    return "student".equals(role);
  }

  public static void clearSession() {
    username = null;
    role = null;
    fullName = null;
  }

  public static String stringify() {
    return "UserSession [userId=" + userId + ", username=" + username + ", role=" + role + ", fullName=" + fullName
        + "]";
  }

}
