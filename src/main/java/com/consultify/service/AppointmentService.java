package com.consultify.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.consultify.utils.TimeUtils;

public class AppointmentService {
  private DatabaseService appointmentDatabaseService = new DatabaseService("appointments.txt");
  private SlotService slotService = new SlotService();

  public ArrayList<String[]> getUpcomingAppointments() {
    ArrayList<String[]> appointments = this.getCurrentSessionAppointments();
    for (String[] appointment : appointments) {
      System.out.println(Arrays.toString(appointment));
    }
    List<String> validStatuses = List.of("Pending Approval", "Approved");
    return appointments.stream().filter(
        record -> validStatuses.contains(record[4]) && TimeUtils.isBefore(slotService.getSlotFromId(record[2])[2]))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public ArrayList<String[]> getCurrentSessionAppointments() {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();

    System.out.println("userid" + UserSession.getUserId());
    return records.stream().filter(record -> record[1].equals(UserSession.getUserId()))
        .collect(Collectors.toCollection(ArrayList::new));
  }

}
