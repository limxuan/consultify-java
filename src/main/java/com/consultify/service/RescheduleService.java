package com.consultify.service;

import java.util.ArrayList;
import java.util.UUID;

import com.consultify.constants.AppointmentStatus;
import com.consultify.utils.TimeUtils;

public class RescheduleService {
  private DatabaseService rescheduleDatabaseService = new DatabaseService("reschedule.txt");
  private SlotService slotService = new SlotService();
  private AppointmentService appointmentService = new AppointmentService();
  private UserService userService = new UserService();

  // rescheduleId|appointmentId|reason|status|createdAt
  public void createReschedule(String appointmentId, String slotId, String reason) {
    String[] appointment = appointmentService.getAppointmentById(appointmentId);
    String rescheduleId = UUID.randomUUID().toString();
    ArrayList<String[]> records = rescheduleDatabaseService.parseContent();
    String studentId = UserSession.getUserId();
    String[] newRecord = new String[] { rescheduleId, appointmentId, AppointmentStatus.RESCHEDULED_PENDING_APPROVAL,
        reason,
        TimeUtils.getCurrentTimeInISO() };
    records.add(newRecord);

    rescheduleDatabaseService.saveData(records);
    appointmentService.updateStatus(appointmentId, AppointmentStatus.RESCHEDULED_PENDING_APPROVAL);
    slotService.setAvailable(slotId, "false");
    slotService.setAvailable(appointment[2], "false");
  }
}
