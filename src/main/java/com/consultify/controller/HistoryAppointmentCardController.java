package com.consultify.controller;

import com.consultify.constants.AppointmentStatus;
import com.consultify.service.AppointmentService;
import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.utils.SceneSwitcher;
import com.consultify.utils.TimeUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class HistoryAppointmentCardController {
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
  private Text feedbackValue;

  @FXML
  private Button feedbackBtn;

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
    feedbackValue.setText(appointmentRecord.length == 7 ? appointmentRecord[6] : "None provided");
    if (appointmentRecord[4].equals(AppointmentStatus.APPROVED) && appointmentRecord.length == 6) {
      feedbackBtn.setDisable(false);
    } else {
      feedbackBtn.setDisable(true);
    }
    feedbackBtn.setOnMouseClicked(this::handleFeedback);
  }

  public void handleFeedback(MouseEvent e) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Feedback");
    dialog.setHeaderText("Please enter your feedback:");
    dialog.setContentText("Feedback:");
    dialog.showAndWait().ifPresent(feedback -> {
      appointmentService.addFeedback(appointmentId, feedback);
      feedbackValue.setText(feedback);
      feedbackBtn.setDisable(true);
    });
  }
}
