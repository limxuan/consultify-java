package com.consultify.controller;

import javafx.fxml.FXML;
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
  }

  public void setAppointmentDetails(String fullName, String time, String date, String venue) {
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
}
