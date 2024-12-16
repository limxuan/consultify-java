package com.consultify.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import com.consultify.constants.AppointmentStatus;
import com.consultify.utils.TimeUtils;

public class RescheduleService {
  private DatabaseService rescheduleDatabaseService = new DatabaseService("reschedule.txt");
  private SlotService slotService = new SlotService();
  private AppointmentService appointmentService = new AppointmentService();

  // rescheduleId|appointmentId|slotId|reason|status|createdAt
  public void createReschedule(String appointmentId, String slotId, String reason) {
    String[] appointment = appointmentService.getAppointmentById(appointmentId);
    String rescheduleId = UUID.randomUUID().toString();
    ArrayList<String[]> records = rescheduleDatabaseService.parseContent();
    String[] newRecord = new String[] { rescheduleId, appointmentId, slotId,
        reason,
        AppointmentStatus.RESCHEDULED_PENDING_APPROVAL,
        TimeUtils.getCurrentTimeInISO() };
    records.add(newRecord);

    rescheduleDatabaseService.saveData(records);
    appointmentService.updateStatus(appointmentId, AppointmentStatus.RESCHEDULED_PENDING_APPROVAL);
    slotService.setAvailable(slotId, "false");
    slotService.setAvailable(appointment[2], "false");
  }

  public void cancelReschedule(String appointmentId) {
    ArrayList<String[]> records = rescheduleDatabaseService.parseContent();
    for (String[] record : records) {
      if (record[1].equals(appointmentId)) {
        String slotId = record[2];
        System.out.println("reschedule did the tings");
        this.slotService.setAvailable(slotId, "true");
        System.out.println("--> made slot available " + slotId);
        record[4] = AppointmentStatus.CANCELLED;
        System.out.println("the new record" + Arrays.toString(record));
        break;
      }
    }
    rescheduleDatabaseService.saveData(records);
  }

  public String[] getRescheduleById(String id) {
    ArrayList<String[]> records = rescheduleDatabaseService.parseContent();
    for (String[] record : records) {
      if (record[0].equals(id)) {
        return record;
      }
    }
    return null;
  }
}
