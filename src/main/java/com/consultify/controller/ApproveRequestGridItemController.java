package com.consultify.controller;

import com.consultify.service.AppointmentService;
import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.utils.TimeUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
  private String appointmentId;

  public void setAppointmentDetails(String appointmentId) {
    this.appointmentId = appointmentId;
    String[] appointmentRecord = this.appointmentService.getAppointmentById(appointmentId);
    String[] slotRecord = this.slotService.getSlotFromId(appointmentRecord[2]);
    String[] studentRecord = this.userService.getStudentById(appointmentRecord[1]);

    studentName.setText(studentRecord[6]);
    purposeText.setText("Purpose: " + appointmentRecord[3]);
    timeText.setText("Time: " + TimeUtils.getTime(slotRecord[2]) + " to " + TimeUtils.getTime(slotRecord[3]));
    statusText.setText("Status: " + appointmentRecord[4]);

    approveBtn.setOnMouseClicked(this::approveClicked);
    rejectBtn.setOnMouseClicked(this::rejectClicked);
  }

  public void approveClicked(MouseEvent e) {
    System.out.println("approved clicked");
  }

  public void rejectClicked(MouseEvent e) {
    System.out.println("rejected cllicked");
  }
}
