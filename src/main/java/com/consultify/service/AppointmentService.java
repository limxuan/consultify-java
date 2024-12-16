package com.consultify.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.consultify.constants.AppointmentStatus;
import com.consultify.utils.TimeUtils;

public class AppointmentService {
  private DatabaseService appointmentDatabaseService = new DatabaseService("appointments.txt");
  private SlotService slotService = new SlotService();
  private UserService userService = new UserService();

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
      TimeUtils.getCurrentTimeInISO(), ""};
    records.add(newRecord);
    appointmentDatabaseService.saveData(records);
    slotService.setAvailable(slotId, "false");
  }

  public void cancelAppointment(String appointmentId) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();

    for (String[] record : records) {
      if (record[0].equals(appointmentId)) {
        String slotId = record[2];
        record[4] = AppointmentStatus.CANCELLED;

        slotService.setAvailable(slotId, "true");
        break;
      }
    }
    appointmentDatabaseService.saveData(records);
  }

  public String[] getAppointmentById(String id) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();
    for (String[] record : records) {
      if (record[0].equals(id)) {
        return record;
      }
    }
    return null;
  }

  public class PastAppointmentsResult {
    public ArrayList<String[]> appointments;
    public Set<String> uniqueStatuses;
    public Set<String> uniqueLecturers;

    public PastAppointmentsResult(ArrayList<String[]> pastAppointments, Set<String> uniqueLecturers, Set<String> uniqueStatuses) {
      this.appointments = pastAppointments;
      this.uniqueLecturers = uniqueLecturers;
      this.uniqueStatuses = uniqueStatuses; 
    }
  }

  public PastAppointmentsResult getPastAppointmentsWithUniqueDates(String studentId) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();
    Set<String> uniqueLecturers = new HashSet<>();
    Set<String> uniqueStatuses = new HashSet<>();

    String[] validStatuses = {AppointmentStatus.RESCHEDULED, AppointmentStatus.CANCELLED, AppointmentStatus.REJECTED};
    ArrayList<String[]> pastAppointments = new ArrayList<>();

    for (String[] record : records) {
      String slotId = record[2];
      String[] slot = slotService.getSlotFromId(slotId);
      
      if (slot != null && record[1].equals(studentId)){
        if(TimeUtils.isPast(slot[3]) || Arrays.asList(validStatuses).contains(record[4])) {
          System.out.println(slot[3]);
          System.out.println("past ady " + TimeUtils.isPast(slot[3]));
          uniqueStatuses.add(record[4]);
          String [] lecturer = userService.getLecturerById(slot[1]);
          uniqueLecturers.add(lecturer[5]);
          pastAppointments.add(record);
        }


      }
    }
    return new PastAppointmentsResult(pastAppointments, uniqueLecturers, uniqueStatuses);
  } 

  public void addFeedback(String appointmentId, String feedback) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();
    for (int i = 0; i < records.size(); i++) {
      String[] record = records.get(i);
      System.out.println(Arrays.toString(
        record
      ));
      if (record[0].equals(appointmentId)) {
        if (record.length == 6) {
          String[] newRecord = Arrays.copyOf(record, record.length + 1);
          newRecord[newRecord.length - 1] = feedback;
          records.set(i, newRecord);
        } else {
          record[6] = feedback;
        }
        break;
      }
    }
    appointmentDatabaseService.saveData(records);
  }
}
