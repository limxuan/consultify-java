package com.consultify.controller;

import com.consultify.constants.AppointmentStatus;
import com.consultify.constants.Role;
import com.consultify.service.AppointmentService;
import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.service.UserSession;
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
  private Text studentFeedbackValue;

  @FXML
  private Text lecturerFeedbackValue;

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

    String studentFeedback = appointmentService.getFeedback(appointmentId, true);
    boolean studentGaveFeedback = studentFeedback != null;
    studentFeedbackValue.setText(studentGaveFeedback ? studentFeedback : "None provided");

    String lecturerFeedback = appointmentService.getFeedback(appointmentId, false);
    boolean lecturerGaveFeedback = lecturerFeedback != null;
    lecturerFeedbackValue.setText(lecturerGaveFeedback ? lecturerFeedback : "None provided");

    boolean isStudent = UserSession.getRole() == Role.STUDENT;
    boolean feedbackCondition = isStudent ? !studentGaveFeedback : !lecturerGaveFeedback;

    feedbackBtn.setDisable(!(appointmentRecord[4].equals(AppointmentStatus.APPROVED) && feedbackCondition));
    feedbackBtn.setOnMouseClicked(this::handleFeedback);
  }

  public void handleFeedback(MouseEvent e) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Feedback");
    dialog.setHeaderText("Please enter your feedback:");
    dialog.setContentText("Feedback:");
    dialog.showAndWait().ifPresent(feedback -> {
      if (UserSession.getRole() == Role.LECTURER) {
        appointmentService.addFeedback(appointmentId, feedback, false);
        lecturerFeedbackValue.setText(feedback);
      } else {
        appointmentService.addFeedback(appointmentId, feedback, true);
        studentFeedbackValue.setText(feedback);
      }
      feedbackBtn.setDisable(true);
    });
  }
}
