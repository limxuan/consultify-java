package com.consultify.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AppointmentItemController {

  @FXML
  private Text fullNameText;
  @FXML
  private Text purposeText;
  @FXML
  private Text timeText;
  @FXML
  private Text venueText;
  @FXML
  private Text statusText;

  // Set the details for the appointment
  public void setAppointmentDetails(String fullName, String purpose, String time, String venue, String status) {
    fullNameText.setText(fullName);
    purposeText.setText("Purpose: " + purpose);
    timeText.setText("Time: " + time);
    venueText.setText("Venue: " + venue);
    statusText.setText("Status: " + status);
    String color = "#35a2fe";
    if ("Approved".equals(status)) {
      color = "#1ebc4b";
    }
    statusText.setStyle("-fx-fill: " + color);
  }
}
