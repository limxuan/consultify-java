package com.consultify.constants;

public final class AppointmentStatus {
  public static final String PENDING_APPROVAL = "Pending Approval";
  public static final String APPROVED = "Approved";
  public static final String RESCHEDULED = "Rescheduled";
  public static final String CANCELLED = "Cancelled";
  public static final String REJECTED = "Rejected";

  private AppointmentStatus() {
    // Prevent instantiation
  }
}
