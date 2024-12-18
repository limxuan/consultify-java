package com.consultify.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.consultify.service.AppointmentService;
import com.consultify.service.SlotService;
import com.consultify.service.UserService;
import com.consultify.service.UserSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class LecturerApproveRequestsController extends LecturerSidebarBaseController {
  @FXML
  private LecturerSidebarComponentController lecturerSidebarComponentController;

  @FXML
  private GridPane entryGridPane;

  @FXML
  private ScrollPane scrollPane;

  @FXML
  private Text centerText;

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
  }

  public void populateGrid() {

    ArrayList<String[]> approvalAppointments = this.appointmentService
        .getApprovalAppointments(UserSession.getUserId());

    centerText.setText(approvalAppointments.size() == 0 ? "No Pending Approvals"
        : "Pending Approval(s): " + approvalAppointments.size());

    int numColumns = 1;
    int x = 0;
    int y = 0;

    this.entryGridPane.getChildren().clear();
    for (int i = 0; i < approvalAppointments.size(); i++) {
      x = i % numColumns;
      y = i / numColumns;

      addAppointmentItem(approvalAppointments.get(i)[0], x, y);
    }
  }

  private void addAppointmentItem(String appointmentId, int x, int y) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/consultify/ApproveRequestGridItem.fxml"));
      Parent appointmentItem = loader.load();
      ApproveRequestGridItemController itemController = loader.getController();
      itemController.setAppointmentDetails(appointmentId);
      this.entryGridPane.add(appointmentItem, x, y);
    } catch (IOException e) {
      System.out.println("Error loading ApproveRequestGridItem.fxml: " + e.getMessage());
      e.printStackTrace();
    }
  }

}
