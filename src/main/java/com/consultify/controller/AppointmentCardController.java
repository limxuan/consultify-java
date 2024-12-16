package com.consultify.controller;

import java.io.IOException;
import java.util.Optional;

import com.consultify.service.AppointmentService;
import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.utils.SceneSwitcher;
import com.consultify.utils.TimeUtils;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AppointmentCardController {
  @FXML
  private Text statusValue;

  @FXML
  private Text timeValue;

  @FXML
  private Text dateValue;

  @FXML
  private Text venueValue;

  @FXML
  private Text purposeValue;

  @FXML
  private Button cancelBtn;

  @FXML
  private Button rescheduleBtn;

  private String appointmentId;
  private AppointmentService appointmentService = new AppointmentService();
  private SlotService slotService = new SlotService();
  private UserService userService = new UserService();

  public void init(String appointmentId) {
    // HACK:
    System.out.println("Debug: init called with appointmentId = " + appointmentId);

    if (appointmentId == null || appointmentId.isEmpty()) {
      throw new IllegalArgumentException("appointmentId cannot be null or empty");
    }

    this.appointmentId = appointmentId;

    String[] appointmentRecord = appointmentService.getAppointmentById(appointmentId);
    if (appointmentRecord == null || appointmentRecord.length <= 4) {
      throw new IllegalArgumentException("Invalid appointment record for appointmentId: " + appointmentId);
    }

    String[] slotRecord = slotService.getSlotFromId(appointmentRecord[2]);
    if (slotRecord == null || slotRecord.length <= 4) {
      throw new IllegalArgumentException("Invalid slot record for slotId: " + appointmentRecord[2]);
    }

    String[] lecturerRecord = userService.getLecturerById(slotRecord[1]);

    String time = String.format("%s to %s",
        TimeUtils.getTime(slotRecord[2]),
        TimeUtils.getTime(slotRecord[3]));

    Long daysFromNow = TimeUtils.daysFromNow(slotRecord[2]);
    if (daysFromNow == null) {
      throw new IllegalArgumentException("daysFromNow is null for slot time: " + slotRecord[2]);
    }

    String daysFromNowFormatted;
    if (daysFromNow < 0) {
      daysFromNowFormatted = String.format("%s days ago", -daysFromNow);
    } else if (daysFromNow > 0) {
      daysFromNowFormatted = String.format("in %s days", daysFromNow);
    } else {
      daysFromNowFormatted = "Today";
    }

    String status = appointmentRecord[4];
    String date = String.format("%s (%s)", TimeUtils.getDate(slotRecord[2]), daysFromNowFormatted);
    String venue = lecturerRecord[6];
    String purpose = appointmentRecord[3];

    statusValue.setText(status);
    timeValue.setText(time);
    dateValue.setText(date);
    venueValue.setText(venue);
    purposeValue.setText(purpose);

    if (cancelBtn == null || rescheduleBtn == null) {
      throw new IllegalStateException("UI components not initialized");
    }

    cancelBtn.setOnMouseClicked(this::handleCancel);
    rescheduleBtn.setOnMouseClicked(this::handleReschedule);
  }

  public void handleCancel(MouseEvent e) {
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Cancel Appointment");
    confirmationAlert.setHeaderText("Confirm Cancellation");
    confirmationAlert.setContentText("Are you sure you want to cancel this appointment?");

    Optional<ButtonType> result = confirmationAlert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      appointmentService.cancelAppointment(this.appointmentId);
      try {
        Stage popupStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        popupStage.close();
        SceneSwitcher.switchTo("StudentHomePage.fxml", "Home");
      } catch (IOException e1) {
        System.out.println("Error loading StudentHomePage.fxml in AppointmentCard: " + e1.getMessage());
        e1.printStackTrace();
      }
    } else {
      System.out.println("Cancellation aborted.");
    }
  }

  public void handleReschedule(MouseEvent e) {
    System.out.println(this.appointmentId);
    System.out.println("hello world");
  }

}
