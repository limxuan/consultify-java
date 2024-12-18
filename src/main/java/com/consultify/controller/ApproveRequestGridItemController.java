package com.consultify.controller;

import com.consultify.constants.AppointmentStatus;
import com.consultify.service.AppointmentService;
import com.consultify.service.RescheduleService;
import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.utils.SceneSwitcher;
import com.consultify.utils.TimeUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ApproveRequestGridItemController {
  @FXML
  private Button approveBtn;

  @FXML
  private Button rejectBtn;

  @FXML
  private Text studentName;

  @FXML
  private Text purposeText;

  @FXML
  private Text timeText;

  @FXML
  private Text statusText;

  private UserService userService = new UserService();
  private SlotService slotService = new SlotService();
  private AppointmentService appointmentService = new AppointmentService();
  private RescheduleService rescheduleService = new RescheduleService();
  private String appointmentId;
  private boolean isReschedule;

  public void setAppointmentDetails(String appointmentId) {
    this.appointmentId = appointmentId;
    String[] appointmentRecord = this.appointmentService.getAppointmentById(appointmentId);
    String[] slotRecord = this.slotService.getSlotFromId(appointmentRecord[2]);
    String[] studentRecord = this.userService.getStudentById(appointmentRecord[1]);

    isReschedule = appointmentRecord[4].equals(AppointmentStatus.RESCHEDULED_PENDING_APPROVAL);

    studentName.setText(studentRecord[6]);
    purposeText.setText("Purpose: " + appointmentRecord[3]);
    timeText.setText("Time: " + TimeUtils.getTime(slotRecord[2]) + " to " + TimeUtils.getTime(slotRecord[3]));
    statusText.setText("Status: " + appointmentRecord[4]);

    approveBtn.setOnMouseClicked(this::approveClicked);
    rejectBtn.setOnMouseClicked(this::rejectClicked);
  }

  public void approveClicked(MouseEvent e) {
    if (isReschedule) {
      this.rescheduleService.acceptReschedule(appointmentId);
    } else {
      this.appointmentService.approveAppointment(appointmentId, AppointmentStatus.APPROVED);
    }
    showAlert("The reschedule request has been accepted successfully!");
    SceneSwitcher.refreshPage();
  }

  public void rejectClicked(MouseEvent e) {
    // TODO: Hehrerere
    rescheduleService.rejectReschedule(appointmentId);
    showAlert("The reschedule request has been rejected successfully!");
    SceneSwitcher.refreshPage();
  }

  public void showAlert(String message) {
    Alert alert = new Alert(AlertType.INFORMATION, message,
        ButtonType.OK);
    alert.setTitle("Success");
    alert.setHeaderText(null);
    alert.showAndWait();
  }
}
