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

  public ArrayList<String[]> getUpcomingAppointments(String id, boolean isStudent) {
    ArrayList<String[]> appointments = this.getAppointmentsForId(id, isStudent);
    List<String> validStatuses = new ArrayList<String>(List.of(
        AppointmentStatus.APPROVED,
        AppointmentStatus.RESCHEDULED_APPROVED));

    if (isStudent) {
      validStatuses.add(AppointmentStatus.PENDING_APPROVAL);
      validStatuses.add(AppointmentStatus.RESCHEDULED_PENDING_APPROVAL);
      validStatuses.add(AppointmentStatus.APPROVED_RESCHEDULE_REJECTED);
    }

    for (String[] record : appointments) {
      String status = record[4];
      String slotId = record[2];
      String slotTime = slotService.getSlotFromId(slotId)[2];

      System.out.println("Record ID: " + record[0]); // Print record ID for debugging
      System.out.println("Status: " + status); // Print the status
      System.out.println("Slot ID: " + slotId); // Print the slot ID
      System.out.println("Slot Time: " + slotTime); // Print the slot time

    }

    return appointments.stream().filter(
        record -> validStatuses.contains(record[4]) && TimeUtils.isBefore(slotService.getSlotFromId(record[2])[2]))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public ArrayList<String[]> getApprovalAppointments(String lecturerId) {
    ArrayList<String[]> appointments = this.getAppointmentsForId(lecturerId, false);
    List<String> validStatuses = new ArrayList<String>(List.of(
        AppointmentStatus.PENDING_APPROVAL,
        AppointmentStatus.RESCHEDULED_PENDING_APPROVAL));
    return appointments.stream().filter(
        record -> validStatuses.contains(record[4]) && TimeUtils.isBefore(slotService.getSlotFromId(record[2])[2]))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public ArrayList<String[]> getAppointmentsForId(String id, boolean isStudent) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();
    return records.stream().filter(record -> {
      if (isStudent) {
        return record[1].equals(id);
      } else {
        String[] slot = slotService.getSlotFromId(record[2]);
        return slot[1].equals(UserSession.getUserId());
      }
    }).collect(Collectors.toCollection(ArrayList::new));
  }

  public String createAppointment(String slotId, String reason, String studentId) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();

    String appointmentId = UUID.randomUUID().toString();
    if (reason.trim().length() == 0) {
      reason = null;
    }

    String[] newRecord = new String[] { appointmentId, studentId, slotId, reason, AppointmentStatus.PENDING_APPROVAL,
        TimeUtils.getCurrentTimeInISO(), "null", "null" };
    records.add(newRecord);
    appointmentDatabaseService.saveData(records);
    slotService.setAvailable(slotId, "false");

    return appointmentId;
  }

  public void cancelAppointment(String appointmentId) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();

    for (String[] record : records) {
      if (record[0].equals(appointmentId)) {
        String slotId = record[2];
        if (record[4].equals(AppointmentStatus.RESCHEDULED_PENDING_APPROVAL)) {
          RescheduleService rescheduleService = new RescheduleService();
          rescheduleService.cancelReschedule(appointmentId);
        }
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

    public PastAppointmentsResult(ArrayList<String[]> pastAppointments, Set<String> uniqueLecturers,
        Set<String> uniqueStatuses) {
      this.appointments = pastAppointments;
      this.uniqueLecturers = uniqueLecturers;
      this.uniqueStatuses = uniqueStatuses;
    }
  }

  public PastAppointmentsResult getPastAppointmentsWithUniqueDates(String studentId) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();
    Set<String> uniqueLecturers = new HashSet<>();
    Set<String> uniqueStatuses = new HashSet<>();

    String[] validStatuses = { AppointmentStatus.CANCELLED,
        AppointmentStatus.REJECTED, AppointmentStatus.APPROVED_RESCHEDULE_REJECTED };
    ArrayList<String[]> pastAppointments = new ArrayList<>();

    for (String[] record : records) {
      String slotId = record[2];
      String[] slot = slotService.getSlotFromId(slotId);

      if (slot != null && record[1].equals(studentId)) {
        if (TimeUtils.isPast(slot[3]) || Arrays.asList(validStatuses).contains(record[4])) {
          uniqueStatuses.add(record[4]);
          String[] lecturer = userService.getLecturerById(slot[1]);
          uniqueLecturers.add(lecturer[5]);
          pastAppointments.add(record);
        }

      }
    }
    return new PastAppointmentsResult(pastAppointments, uniqueLecturers, uniqueStatuses);
  }

  public ArrayList<String[]> getLecturerPastAppointments(String lecturerId) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();

    String[] validStatuses = { AppointmentStatus.RESCHEDULED_APPROVED, AppointmentStatus.CANCELLED,
        AppointmentStatus.REJECTED, AppointmentStatus.RESCHEDULED_PENDING_APPROVAL };
    ArrayList<String[]> pastAppointments = new ArrayList<>();

    for (String[] record : records) {
      String slotId = record[2];
      String[] slotRecord = slotService.getSlotFromId(slotId);

      if (slotRecord != null && slotRecord[1].equals(lecturerId)) {
        if (TimeUtils.isPast(slotRecord[3]) || Arrays.asList(validStatuses).contains(record[4])) {
          pastAppointments.add(record);
        }
      }
    }
    return pastAppointments;
  }

  public void addFeedback(String appointmentId, String feedback, boolean isStudentFeedback) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();
    for (String[] record : records) {
      if (record[0].equals(appointmentId)) {
        if (isStudentFeedback) {
          record[6] = feedback;
        } else {
          record[7] = feedback;
        }
        break;
      }
    }
    appointmentDatabaseService.saveData(records);
  }

  public String getFeedback(String appointmentId, boolean isStudentFeedback) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();
    for (String[] record : records) {
      if (record[0].equals(appointmentId)) {
        int recordIndex = isStudentFeedback ? 6 : 7;
        String feedback = record[recordIndex];
        if (feedback.equals("null")) {
          return null;
        } else {
          return feedback;
        }
      }
    }
    return null;
  }

  public void updateStatus(String appointmentId, String status) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();
    for (String[] record : records) {
      if (record[0].equals(appointmentId)) {
        record[4] = status;
        break;
      }
    }
    appointmentDatabaseService.saveData(records);
  }

  public void approveAppointment(String appointmentId, String status) {
    String[] appointmentRecord = getAppointmentById(appointmentId);
    boolean isReschedule = appointmentRecord[4].equals(AppointmentStatus.RESCHEDULED_PENDING_APPROVAL);
    if (isReschedule) {
      RescheduleService rescheduleService = new RescheduleService();
      rescheduleService.acceptReschedule(appointmentId);
    } else {
      updateStatus(appointmentId, AppointmentStatus.APPROVED);
    }
  }

  public void rejectAppointment(String appointmentId, String status) {
    String[] appointmentRecord = getAppointmentById(appointmentId);
    boolean isReschedule = appointmentRecord[4].equals(AppointmentStatus.RESCHEDULED_PENDING_APPROVAL);
    if (isReschedule) {
      RescheduleService rescheduleService = new RescheduleService();
      rescheduleService.rejectReschedule(appointmentId);
    } else {
      updateStatus(appointmentId, AppointmentStatus.REJECTED);
      slotService.setAvailable(appointmentRecord[2], "true");
    }
  }

  public void updateSlotId(String appointmentId, String slotId) {
    ArrayList<String[]> records = appointmentDatabaseService.parseContent();
    for (String[] record : records) {
      if (record[0].equals(appointmentId)) {
        record[2] = slotId;
        break;
      }
    }
    appointmentDatabaseService.saveData(records);
  }
}
