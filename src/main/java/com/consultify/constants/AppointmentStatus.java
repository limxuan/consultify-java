package com.consultify.constants;

public final class AppointmentStatus {
  public static final String PENDING_APPROVAL = "Pending Approval";
  public static final String APPROVED = "Approved";
  public static final String RESCHEDULED_APPROVED = "Rescheduled";
  public static final String RESCHEDULED_PENDING_APPROVAL = "Pending Approval (Reschedule)";
  public static final String CANCELLED = "Cancelled";
  public static final String REJECTED = "Rejected";
  public static final String ACCEPTED = "Accepted";

  public static final String PENDING_APPROVAL_COLOR = "#35a2fe";
  public static final String APPROVED_COLOR = "#1ebc4b";
  public static final String RESCHEDULED_COLOR = "#ffb84d";
  public static final String CANCELLED_COLOR = "#f29999";
  public static final String REJECTED_COLOR = "#e74c3c";

  private AppointmentStatus() {
    // Prevent instantiation
  }
}
