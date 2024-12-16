package com.consultify.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import com.consultify.service.AppointmentService;
import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.utils.SceneSwitcher;
import com.consultify.utils.TimeUtils;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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
  private String[] slotRecord;
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

    this.slotRecord = slotService.getSlotFromId(appointmentRecord[2]);
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
    Stage curStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
    curStage.close();
    ArrayList<String[]> availableSlots = slotService.getAvailableSlotsForLecturer(this.slotRecord[1]);
    System.out.println("Printing available slots, len: " + availableSlots.size());

    Stage stage = new Stage();
    VBox vbox = new VBox(15);
    vbox.setPadding(new Insets(20, 20, 20, 20));

    Label instructionLabel = new Label("Select a time slot and provide a reason:");
    instructionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

    ComboBox<String> timeSlotComboBox = new ComboBox<>();
    for (String[] slot : availableSlots) {
      String slotInfo = slot[0] + " (" + TimeUtils.getTime(slot[2]) + " - " + TimeUtils.getTime(slot[3]) + " @ "
          + TimeUtils.getDate(slot[3]) + ")";

      timeSlotComboBox.getItems().add(slotInfo);
    }
    timeSlotComboBox.setStyle(
        "-fx-padding: 10; -fx-font-size: 12px; -fx-background-color: #f9f9f9; -fx-border-color: #333; -fx-border-width: 1px; -fx-background-insets: 0, 1;");

    TextArea reasonTextArea = new TextArea();
    reasonTextArea.setPromptText("Enter your reason for rescheduling...");
    reasonTextArea.setStyle("-fx-padding: 10; -fx-font-size: 12px; -fx-background-color: #f2f2f2;");

    Button confirmButton = new Button("Confirm");
    confirmButton.setStyle(
        "-fx-background-color: #5fb8b8; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-font-weight: bold;");

    confirmButton.setOnAction(event -> {
      String selectedSlot = timeSlotComboBox.getValue();
      String reason = reasonTextArea.getText();
      if (selectedSlot != null && !reason.isEmpty()) {
        String selectedSlotId = selectedSlot.split(" ")[0];
        System.out.println("Selected Slot: " + selectedSlotId);
        System.out.println("Reason: " + reason);
        stage.close();
        handleRescheduleSelection(selectedSlotId, reason);
      } else {
        showAlert("Error", "Please select a slot and provide a reason.");
      }
    });
    vbox.getChildren().addAll(instructionLabel, timeSlotComboBox, reasonTextArea, confirmButton);

    Scene scene = new Scene(vbox, 400, 300);
    scene.setFill(Color.TRANSPARENT);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setScene(scene);
    stage.setResizable(false);
    stage.setTitle("Reschedule Appointment");
    stage.show();
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.showAndWait();
  }

  private void handleRescheduleSelection(String slotId, String reason) {
    System.out.println("Rescheduled Slot ID: " + slotId);
    System.out.println("Reason for rescheduling: " + reason);
  }
}
