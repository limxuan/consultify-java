package com.consultify.controller;

import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.service.UserSession;
import com.consultify.utils.SceneSwitcher;
import com.consultify.utils.TimeUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;

public class ManageSlotsItemController {
  @FXML
  private Text timeText;

  @FXML
  private Text dateText;

  @FXML
  private Text venueText;

  @FXML
  private Button cancelSlotButton;

  @FXML
  private VBox manageSlotItem;

  private String slotId;

  private UserService userService = new UserService();
  private SlotService slotService = new SlotService();

  /**
   * Initialize method for setting up event handlers and effects.
   */
  public void initialize() {

    // Add event handlers for the VBox hover effect
    this.cancelSlotButton.setOnMouseEntered(this::onMouseEntered);
    this.cancelSlotButton.setOnMouseExited(this::onMouseExited);

    // Button click handler
    this.cancelSlotButton.setOnMouseClicked(this::onClick);
  }

  /**
   * Sets the slot details for displaying in the VBox.
   */
  public void setSlotDetails(String slotId) {
    String[] slotRecord = this.slotService.getSlotFromId(slotId);
    String[] lecturerRecord = this.userService.getLecturerById(UserSession.getUserId());
    this.slotId = slotId;
    this.timeText.setText("Time: " + TimeUtils.getTime(slotRecord[2]) + " to " + TimeUtils.getTime(slotRecord[3]));
    this.dateText.setText("Date: " + TimeUtils.getDate(slotRecord[2]));
    this.venueText.setText("Venue: " + lecturerRecord[6]);
  }

  public void onMouseEntered(MouseEvent event) {
    cancelSlotButton.setStyle(
        "-fx-background-color: #f38579; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-padding: 5px 15px; " +
            "-fx-background-radius: 10; " +
            "-fx-border-radius: 10; " +
            "-fx-border-width: 2px;");

    // Apply the DropShadow effect
    DropShadow dropShadow = new DropShadow();
    dropShadow.setOffsetX(5);
    dropShadow.setOffsetY(5);
    dropShadow.setColor(Color.GRAY); // You can customize the color and intensity
    cancelSlotButton.setEffect(dropShadow);
  }

  public void onMouseExited(MouseEvent event) {
    cancelSlotButton.setStyle(
        "-fx-background-color: #e14f3e; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-padding: 5px 15px; " +
            "-fx-background-radius: 10; " +
            "-fx-border-width: 2px;");
    this.cancelSlotButton.setEffect(null);
  }

  /**
   * Button click event to handle slot cancellation.
   */
  public void onClick(MouseEvent e) {

    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Cancel Slot");
    alert.setHeaderText("Are you sure you want to cancel this slot?");
    alert.setContentText("This action cannot be undone.");
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) {
        slotService.removeSlot(this.slotId);
        SceneSwitcher.refreshPage();
      }
    });
  }
}
