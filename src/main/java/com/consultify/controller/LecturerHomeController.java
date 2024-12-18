package com.consultify.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.ArrayList;
import com.consultify.service.AppointmentService;
import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.service.UserSession;
import com.consultify.utils.SceneSwitcher;
import com.consultify.utils.TimeUtils;

public class LecturerHomeController extends LecturerSidebarBaseController {

  @FXML
  private VBox appointmentsVBox; // Reference to the VBox in the main FXML

  @FXML
  private Text upcomingAppointmentsText;

  private AppointmentService appointmentService = new AppointmentService();
  private SlotService slotService = new SlotService();
  private UserService userService = new UserService();

  public void initialize() {
    initBase();
    ArrayList<String[]> appointments = appointmentService.getUpcomingAppointments(UserSession.getUserId(), false);
    upcomingAppointmentsText.setText("Upcoming Appointments: " + appointments.size());

    for (String[] appointment : appointments) {
      String[] slot = slotService.getSlotFromId(appointment[2]);
      String[] student = userService.getStudentById(appointment[1]);
      String appointmentId = appointment[0];
      String studentFullName = student[6];
      String slotStart = slot[2];
      String slotEnd = slot[3];
      String formatTime = TimeUtils.getTime(slotStart) + " to " + TimeUtils.getTime(slotEnd) + " ("
          + TimeUtils.getDate(slotStart) + ")";
      ;
      String officeLocation = student[6];
      addAppointmentItem(appointmentId, studentFullName, appointment[3], formatTime, officeLocation, appointment[4]);
    }

  }

  private void addAppointmentItem(String appointmentId, String fullName, String purpose, String time, String venue,
      String status) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/consultify/UpcomingAppointmentItem.fxml"));
      Parent appointmentItem = loader.load();
      AppointmentItemController itemController = loader.getController();
      itemController.setAppointmentDetails(appointmentId, fullName, purpose, time, venue, status);
      appointmentsVBox.getChildren().add(appointmentItem);
    } catch (IOException e) {
      System.out.println("Error loading UpcomingAppointmentItem.fxml: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void redirectBookAppointment(MouseEvent e) {
    System.out.println("redirect book appointment clicked");
    try {
      SceneSwitcher.switchTo("StudentBookAppointmentPage.fxml", "Book Appointment");
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

}
