package com.consultify.controller;

import java.util.Optional;

import com.consultify.service.AppointmentService;
import com.consultify.service.UserSession;
import com.consultify.utils.SceneSwitcher;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BookAppointmentItemController {
  @FXML
  private Text lecturerName;

  @FXML
  private Text timeText;

  @FXML
  private Text dateText;

  @FXML
  private Text venueText;

  @FXML
  private VBox bookAppointmentItem;

  @FXML
  private Text bookText;

  private String fullName;
  private String time;
  private String date;
  private String venue;
  private String slotId;

  private AppointmentService appointmentService = new AppointmentService();

  /**
   * 
   */
  public void initialize() {
    DropShadow hoverShadow = new DropShadow();
    hoverShadow.setColor(Color.GRAY);
    hoverShadow.setRadius(10);
    hoverShadow.setOffsetX(5);
    hoverShadow.setOffsetY(5);
    bookText.setVisible(false);

    // Add event handlers to each card
    this.bookAppointmentItem.setOnMouseEntered(event -> {
      this.bookAppointmentItem.setEffect(hoverShadow);
      bookText.setVisible(true);
    });
    this.bookAppointmentItem.setOnMouseExited(event -> {
      this.bookAppointmentItem.setEffect(null);
      bookText.setVisible(false);
    });

    this.bookAppointmentItem.setOnMouseClicked(this::onClick);
  }

  public void setAppointmentDetails(String slotId, String fullName, String time, String date, String venue) {
    this.fullName = fullName;
    this.time = time;
    this.date = date;
    this.venue = venue;
    this.slotId = slotId;
    this.lecturerName.setText(fullName);
    this.timeText.setText("Time: " + time);
    this.dateText.setText("Date: " + date);
    this.venueText.setText("Venue: " + venue);
  }

  public void onMouseEntered(MouseEvent event) {
    System.out.println("Entered");
    bookAppointmentItem.setStyle("-fx-background-color: #394368; -fx-background-radius: 10;");
    this.lecturerName.setStyle("-fx-text-fill: white;");
    this.timeText.setStyle("-fx-text-fill: white;");
    this.dateText.setStyle("-fx-text-fill: white;");
    this.venueText.setStyle("-fx-text-fill: white;");

  }

  public void onMouseExited() {
    System.out.println("Exited");
    bookAppointmentItem.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
    this.lecturerName.setStyle("-fx-text-fill: black;");
    this.timeText.setStyle("-fx-text-fill: black;");
    this.dateText.setStyle("-fx-text-fill: black;");
    this.venueText.setStyle("-fx-text-fill: black;");
  }

  public void onClick(MouseEvent e) {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("Appointment Booking Confirmation");

    ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
    ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);

    String bookingDetails = String.join("\n",
        "Lecturer Name: " + this.fullName,
        "Time: " + this.time,
        "Date: " + this.date,
        "Venue: " + this.venue);

    Text detailsText = new Text("Please reconfirm your booking:\n\n" + bookingDetails);
    TextArea reasonField = new TextArea();
    reasonField.setPromptText("Enter the reason for booking...");
    reasonField.setWrapText(true);

    VBox content = new VBox(10, detailsText, reasonField);
    dialog.getDialogPane().setContent(content);

    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == confirmButtonType) {
        return reasonField.getText();
      }
      return null;
    });

    // Show dialog and handle responsestudent
    Optional<String> result = dialog.showAndWait();
    result.ifPresent(reason -> {
      appointmentService.createAppointment(slotId, reason, UserSession.getUserId());
      new Alert(Alert.AlertType.INFORMATION, "Appointment booked successfully!").show();
      try {
        SceneSwitcher.switchTo("StudentHomePage.fxml", "Home");
      } catch (Exception er) {
        System.out.println("Error loading studenthomepage, bookapppointmentitemcontroller:" + er.getMessage());
      }
    });
  }
}
