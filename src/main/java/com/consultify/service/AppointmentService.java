package com.consultify.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.consultify.constants.AppointmentStatus;
import com.consultify.utils.TimeUtils;

public class AppointmentService {
  private DatabaseService appointmentDatabaseService = new DatabaseService("appointments.txt");
  private SlotService slotService = new SlotService();

  public ArrayList<String[]> getUpcomingAppointments() {
    ArrayList<String[]> appointments = this.getCurrentSessionAppointmentsForSession();
    List<String> validStatuses = List.of("Pending Approval", "Approved");
    return appointments.stream().filter(
        record -> validStatuses.contains(record[4]) && TimeUtils.isBefore(slotService.getSlotFromId(record[2])[2]))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public ArrayList<String[]> getCurrentSessionAppointmentsForSession() {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();

    return records.stream().filter(record -> record[1].equals(UserSession.getUserId()))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public void createAppointment(String slotId, String reason) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();

    String appointmentId = UUID.randomUUID().toString();
    String studentId = UserSession.getUserId();
    if (reason.trim().length() == 0) {
      reason = null;
    }

    String[] newRecord = new String[] { appointmentId, studentId, slotId, reason, AppointmentStatus.PENDING_APPROVAL,
        TimeUtils.getCurrentTimeInISO() };
    records.add(newRecord);
    appointmentDatabaseService.saveData(records);
    slotService.setAvailable(slotId, "false");
  }

}
