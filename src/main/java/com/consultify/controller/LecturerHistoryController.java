
package com.consultify.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.consultify.service.AppointmentService;
import com.consultify.service.AppointmentService.PastAppointmentsResult;
import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.service.UserSession;
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

public class LecturerHistoryController extends LecturerSidebarBaseController {
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
  private Button filterButton;

  private SlotService slotService = new SlotService();
  private UserService userService = new UserService();
  private AppointmentService appointmentService = new AppointmentService();

  private String[] uniqueLecturers;
  private String[] uniqueStatuses;

  private Map<String, String> currentFilters = new HashMap<>();

  public void initialize() {
    initBase();
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(false);
    populateGrid();

    filterButton.setOnMouseClicked(e -> openFilterStage());
  }

  public void populateGrid() {
    ArrayList<String[]> pastAppointments = this.appointmentService.getLecturerPastAppointments(UserSession.getUserId());
    centerText.setText(pastAppointments.size() == 0 ? "No results found" : "Appointments");
    availableSlotsText.setText("Appointments(s): " + pastAppointments.size());
    System.out.println("after filterd size" + pastAppointments.size());

    int numColumns = 1;
    int x = 0;
    int y = 0;

    this.entryGridPane.getChildren().clear();
    for (int i = 0; i < pastAppointments.size(); i++) {
      x = i % numColumns;
      y = i / numColumns;

      String slotId = pastAppointments.get(i)[2];
      String[] slot = slotService.getSlotFromId(slotId);
      String[] lecturerInfo = new UserService().getLecturerById(slot[1]);
      String lecturerFullName = lecturerInfo[5];
      String lecturerVenue = lecturerInfo[6];
      String appointmentPurpose = pastAppointments.get(i)[3];
      String slotStart = slot[2];
      String slotEnd = slot[3];
      String formatTime = TimeUtils.getTime(slotStart) + " to " + TimeUtils.getTime(slotEnd) + " ("
          + TimeUtils.getDate(slotStart) + ")";
      ;
      addAppointmentItem(pastAppointments.get(i)[0], lecturerFullName, appointmentPurpose, formatTime,
          lecturerVenue, pastAppointments.get(i)[4], x, y);
    }
  }

  public void addAppointmentItem(String appointmentId, String fullName, String purpose, String time, String venue,
      String status,
      int x, int y) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/consultify/StudentHistoryItem.fxml"));
      Parent appointmentItem = loader.load();
      StudentHistoryItemController itemController = loader.getController();
      itemController.setAppointmentDetails(appointmentId, fullName, purpose, time, venue, status);
      this.entryGridPane.add(appointmentItem, x, y);
    } catch (IOException e) {
      System.out.println("Error loading UpcomingAppointmentItem.fxml: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void openFilterStage() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/consultify/StudentHistoryFilter.fxml"));
      Parent root = loader.load();

      StudentHistoryFilterController filterController = loader.getController();

      filterController.init();
      filterController.setOnConfirmCallback(filterData -> {
        this.currentFilters = filterData;
        populateGrid();
      });
      filterController.setUniqueLecturers(uniqueLecturers);
      filterController.setPreviousData(currentFilters);
      filterController.setUniqueStatus(uniqueStatuses);

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
