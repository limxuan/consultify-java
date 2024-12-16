
package com.consultify.controller;

import com.consultify.constants.AppointmentStatus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StudentHistoryItemController {

  @FXML
  private VBox appointmentItem;
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
  @FXML
  private Text viewMoreText;

  private String appointmentId;

  public void initialize() {
    DropShadow hoverShadow = new DropShadow();
    hoverShadow.setColor(Color.GRAY);
    hoverShadow.setRadius(10);
    hoverShadow.setOffsetX(5);
    hoverShadow.setOffsetY(5);

    viewMoreText.setVisible(false);
    appointmentItem.setOnMouseEntered(e -> {
      this.appointmentItem.setEffect(hoverShadow);
      onMouseEntered(e);
    });

    appointmentItem.setOnMouseExited(e -> {
      this.appointmentItem.setEffect(null);
      onMouseExited(e);
    });

    appointmentItem.setOnMouseClicked(this::onClick);

  }

  // Set the details for the appointment
  public void setAppointmentDetails(String appointmentId, String fullName, String purpose, String time, String venue,
      String status) {
    this.appointmentId = appointmentId;
    fullNameText.setText(fullName);
    purposeText.setText("Purpose: " + purpose);
    timeText.setText("Time: " + time);
    venueText.setText("Venue: " + venue);
    statusText.setText("Status: " + status);
    String color = AppointmentStatus.PENDING_APPROVAL_COLOR;
    if ("Approved".equals(status)) {
      color = AppointmentStatus.APPROVED_COLOR;
    }
    statusText.setStyle("-fx-fill: " + color);
  }

  public void onMouseEntered(MouseEvent event) {
    System.out.println("Entered");
    appointmentItem.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
    this.fullNameText.setStyle("-fx-text-fill: white;");
    this.timeText.setStyle("-fx-text-fill: white;");
    this.venueText.setStyle("-fx-text-fill: white;");
    this.viewMoreText.setVisible(true);

  }

  public void onMouseExited(MouseEvent event) {
    System.out.println("Exited");
    appointmentItem.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
    this.timeText.setStyle("-fx-text-fill: black;");
    this.venueText.setStyle("-fx-text-fill: black;");
    this.viewMoreText.setVisible(false);
  }

  public void onClick(MouseEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/consultify/HistoryAppointmentCard.fxml"));
      Parent root = loader.load();
      HistoryAppointmentCardController controller = loader.getController();
      controller.init(appointmentId);

      Stage stage = new Stage();
      stage.setTitle("Appointment Details");
      stage.setScene(new Scene(root));
      stage.setWidth(433);
      stage.setHeight(470);
      stage.setResizable(false);
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();
    } catch (Exception e) {
      System.out.println("error changing appointment card");
      e.printStackTrace();
    }
  }
}
