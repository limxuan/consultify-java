package com.consultify.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.consultify.service.AppointmentService;
import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.service.UserSession;
import com.consultify.utils.TimeUtils;

public class StudentHomeController {

  @FXML
  private VBox appointmentsVBox; // Reference to the VBox in the main FXML

  @FXML
  private Text upcomingAppointmentsText;

  @FXML
  private Text username;

  private AppointmentService appointmentService = new AppointmentService();
  private SlotService slotService = new SlotService();
  private UserService userService = new UserService();

  public void initialize() {
    ArrayList<String[]> appointments = appointmentService.getUpcomingAppointments();
    username.setText(UserSession.getUsername());
    upcomingAppointmentsText.setText("Upcoming Appointments: " + appointments.size());
    for (String[] appointment : appointments) {
      System.out.println("Appointments: " + Arrays.toString(appointment)); // This prints the array in a readable format
    }

    for (String[] appointment : appointments) {
      String[] slot = slotService.getSlotFromId(appointment[2]);
      String[] lecturer = userService.getLecturerById(slot[1]);
      String lecturerFullName = lecturer[5];
      String slotStart = slot[2];
      String slotEnd = slot[3];
      String formatTime = TimeUtils.getTime(slotStart) + " to " + TimeUtils.getTime(slotEnd) + " ("
          + TimeUtils.getDate(slotStart) + ")";
      ;
      String officeLocation = lecturer[6];
      addAppointmentItem(lecturerFullName, appointment[3], formatTime, officeLocation, appointment[4]);
    }

  }

  private void addAppointmentItem(String fullName, String purpose, String time, String venue, String status) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/consultify/UpcomingAppointmentItem.fxml"));
      Parent appointmentItem = loader.load();
      AppointmentItemController itemController = loader.getController();
      itemController.setAppointmentDetails(fullName, purpose, time, venue, status);
      appointmentsVBox.getChildren().add(appointmentItem);
    } catch (IOException e) {
      System.out.println("Error loading UpcomingAppointmentItem.fxml: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void redirectBookAppointment(MouseEvent e) {
    System.out.println("redirect book appointment clicked");
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/com/consultify/StudentBookAppointmentPage.fxml"));
      Scene scene = new Scene(root);
      Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
      stage.setScene(scene);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

}
