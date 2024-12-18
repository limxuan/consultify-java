package com.consultify.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.service.UserSession;
import com.consultify.utils.SceneSwitcher;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LecturerManageSlotsController extends LecturerSidebarBaseController {
  @FXML
  private LecturerSidebarComponentController lecturerSidebarComponentController;

  @FXML
  private GridPane entryGridPane;

  @FXML
  private ScrollPane scrollPane;

  @FXML
  private Text availableSlotsText;

  @FXML
  private Text centerText;

  @FXML
  private Button addSlotsButton;

  private SlotService slotService = new SlotService();

  public void initialize() {
    initBase();
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(false);
    populateGrid();

    addSlotsButton.setOnMouseClicked(e -> openAddSlotsStage());
  }

  public void populateGrid() {

    ArrayList<String[]> slots = this.slotService.getAvailableSlotsForLecturer(UserSession.getUserId());
    System.out.println("_____> current lecturer has " + slots.size() + " slots");

    centerText.setText(slots.size() == 0 ? "No results found" : "Please select a slot");
    availableSlotsText.setText("Available slot(s): " + slots.size());

    int numColumns = 2;
    int x = 0;
    int y = 0;

    this.entryGridPane.getChildren().clear();
    for (int i = 0; i < slots.size(); i++) {
      x = i % numColumns;
      y = i / numColumns;

      String[] slot = slots.get(i);
      String slotId = slot[0];
      String[] lecturerInfo = new UserService().getLecturerById(UserSession.getUserId());
      addAppointmentItem(slotId, x, y);
    }
  }

  private void addAppointmentItem(String slotId, int x, int y) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/consultify/ManageSlotsItem.fxml"));
      Parent appointmentItem = loader.load();
      ManageSlotsItemController itemController = loader.getController();
      itemController.setSlotDetails(slotId);
      this.entryGridPane.add(appointmentItem, x, y);
    } catch (IOException e) {
      System.out.println("Error loading ManageSlotsItem.fxml: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void openAddSlotsStage() {
    Stage addSlotsStage = new Stage();
    addSlotsStage.initModality(Modality.APPLICATION_MODAL);
    addSlotsStage.setResizable(false);
    addSlotsStage.setTitle("Add Consultation Slots");
    addSlotsStage.setHeight(300);

    DatePicker datePicker = new DatePicker();
    datePicker.setValue(LocalDate.now().plusDays(1));

    datePicker.setDayCellFactory(picker -> new DateCell() {
      @Override
      public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        setDisable(empty || item.isBefore(LocalDate.now().plusDays(1)));
      }
    });

    Label dateLabel = new Label("Select Date:");

    ComboBox<Integer> hourComboBox = new ComboBox<>();
    for (int i = 0; i < 24; i++) {
      hourComboBox.getItems().add(i);
    }
    hourComboBox.setValue(LocalTime.now().getHour());

    ComboBox<Integer> minuteComboBox = new ComboBox<>();
    for (int i = 0; i < 60; i += 15) {
      minuteComboBox.getItems().add(i);
    }
    minuteComboBox.setValue(LocalTime.now().getMinute());

    Label timeLabel = new Label("Select Start Time:");

    ComboBox<Integer> durationComboBox = new ComboBox<>();
    durationComboBox.getItems().addAll(1, 2, 3);
    durationComboBox.setValue(1);

    Label durationLabel = new Label("Select Duration (hours):");

    Button confirmButton = new Button("Confirm");
    confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
    confirmButton.setOnAction(e -> {
      LocalDate selectedDate = datePicker.getValue();
      int selectedHour = hourComboBox.getValue();
      int selectedMinute = minuteComboBox.getValue();
      int selectedDuration = durationComboBox.getValue();
      LocalTime selectedTime = LocalTime.of(selectedHour, selectedMinute);

      System.out.println("Selected Date: " + selectedDate);
      System.out.println("Selected Time: " + selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")));
      System.out.println("Selected Duration: " + selectedDuration + " hours");
      slotService.addSlot(UserSession.getUserId(), selectedDate.toString(),
          selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")), selectedDuration);
      SceneSwitcher.refreshPage();
      addSlotsStage.close();
    });

    VBox vbox = new VBox(10, dateLabel, datePicker, timeLabel, new HBox(10, hourComboBox, minuteComboBox),
        durationLabel, durationComboBox, confirmButton);
    vbox.setStyle("-fx-padding: 20; -fx-font-family: 'Arial'; -fx-font-size: 14;");
    vbox.setPrefHeight(250);
    vbox.setPrefWidth(350);
    vbox.setSpacing(15);

    dateLabel.setStyle("-fx-font-weight: bold;");
    timeLabel.setStyle("-fx-font-weight: bold;");
    durationLabel.setStyle("-fx-font-weight: bold;");

    Scene scene = new Scene(vbox, 350, 250);
    addSlotsStage.setScene(scene);
    addSlotsStage.show();
  }
}
