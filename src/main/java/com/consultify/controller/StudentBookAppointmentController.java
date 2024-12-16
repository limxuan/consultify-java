package com.consultify.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.utils.TimeUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StudentBookAppointmentController extends StudentSidebarBaseController {
  @FXML
  private StudentSidebarComponentController studentSidebarComponentController;

  @FXML
  private GridPane entryGridPane;

  @FXML
  private ScrollPane scrollPane;

  @FXML
  private Text availableSlotsText;

  @FXML
  private Text centerText;

  @FXML
  private Button filterButton;

  private SlotService slotService = new SlotService();
  private UserService userService = new UserService();

  private String[] uniqueLecturers;
  private String[] uniqueDates;

  private Map<String, String> currentFilters = new HashMap<>();

  public void initialize() {
    initBase();
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(false);
    populateGrid();

    filterButton.setOnMouseClicked(e -> openFilterStage());
  }

  public void populateGrid() {

    ArrayList<String[]> slots = this.slotService.getAvailableSlots();
    List<Set<String>> unique = this.slotService.getUniqueLecturersAndDates(slots);
    uniqueLecturers = unique.get(0).toArray(new String[0]);
    uniqueDates = unique.get(1).toArray(new String[0]);
    System.out.println("Size of slots" + slots.size());

    System.out.println("before stream" + slots.size());
    slots = new ArrayList<String[]>(slots.stream().filter(slot -> {
      System.out.println("inside stream");

      String currentSlotLecturerFullName = this.userService.getLecturerById(slot[1])[5];
      Boolean lecturerMatch = currentFilters.get("lecturerName") != null
          ? currentFilters.get("lecturerName").equals(currentSlotLecturerFullName)
          : true;

      Boolean validDate = false;
      String filterStartDate = currentFilters.get("startDate");
      String filterEndDate = currentFilters.get("endDate");
      if (filterStartDate == null && filterEndDate == null) {
        validDate = true;
      } else if (filterStartDate != null && filterEndDate == null) {
        validDate = filterStartDate.equals(TimeUtils.getDate(slot[2]));
      } else {
        validDate = TimeUtils.inBetween(TimeUtils.getDate(slot[2]), filterStartDate, filterEndDate);
      }

      return lecturerMatch && validDate;
    }).collect(Collectors.toList()));

    System.out.println();

    centerText.setText(slots.size() == 0 ? "No results found" : "Please select a slot");
    availableSlotsText.setText("Available slot(s): " + slots.size());
    System.out.println("after filterd size" + slots.size());

    int numColumns = 2;
    int x = 0;
    int y = 0;

    this.entryGridPane.getChildren().clear();
    for (int i = 0; i < slots.size(); i++) {
      x = i % numColumns;
      y = i / numColumns;

      String[] slot = slots.get(i);
      String slotId = slot[0];
      String[] lecturerInfo = new UserService().getLecturerById(slot[1]);
      String lecturerFullName = lecturerInfo[5];
      String lecturerVenue = lecturerInfo[6];
      String formattedTime = TimeUtils.getTime(slot[2]) + " to " + TimeUtils.getTime(slot[3]);
      String formattedDate = TimeUtils.getDate(slot[2]);
      addAppointmentItem(slotId, lecturerFullName, formattedTime, formattedDate, lecturerVenue, x, y);
    }
  }

  private void addAppointmentItem(String slotId, String fullName, String time, String date, String venue, int x,
      int y) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/consultify/BookAppointmentItem.fxml"));
      Parent appointmentItem = loader.load();
      BookAppointmentItemController itemController = loader.getController();
      itemController.setAppointmentDetails(slotId, fullName, time, date, venue);
      this.entryGridPane.add(appointmentItem, x, y);
    } catch (IOException e) {
      System.out.println("Error loading UpcomingAppointmentItem.fxml: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void openFilterStage() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/consultify/StudentBookAppointmentFilter.fxml"));
      Parent root = loader.load();

      StudentBookAppointmentFilterController filterController = loader.getController();

      filterController.init();
      filterController.setOnConfirmCallback(filterData -> {
        this.currentFilters = filterData;
        populateGrid();
      });
      filterController.setUniqueLecturers(uniqueLecturers);
      filterController.setPreviousData(currentFilters);

      Stage stage = new Stage();
      stage.setWidth(400);
      stage.setHeight(400);
      stage.setResizable(false);
      stage.setScene(new Scene(root));
      stage.initModality(Modality.APPLICATION_MODAL); // Block parent window until dialog is closed
      stage.showAndWait();
    } catch (IOException e) {
      System.out.println("Error loading StudentFilter.fxml: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
