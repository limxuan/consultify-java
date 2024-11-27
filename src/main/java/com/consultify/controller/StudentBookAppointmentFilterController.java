package com.consultify.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.consultify.utils.TimeUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class StudentBookAppointmentFilterController {
  @FXML
  private ChoiceBox<String> lecturerDropdown;

  @FXML
  private DatePicker startDate;

  @FXML
  private DatePicker endDate;

  @FXML
  private Button clearBtn;

  @FXML
  private Button cancelBtn;

  @FXML
  private Button confirmBtn;

  private Consumer<Map<String, String>> onConfirmCallback;

  public void init() {
    cancelBtn.setOnAction(event -> handleCancel());
    clearBtn.setOnAction(event -> handleClear());
    confirmBtn.setOnAction(event -> onConfirm());
  }

  @FXML
  public void onConfirm() {
    if (onConfirmCallback != null) {
      Map<String, String> filterData = new HashMap<>();
      LocalDate start = startDate.getValue();
      LocalDate end = endDate.getValue();

      if (start != null && end != null && start.isAfter(end)) {
        new Alert(Alert.AlertType.ERROR, "Start date must be before end date").show();
      } else if (start == null && end != null) {
        new Alert(Alert.AlertType.ERROR,
            "You cannot be only filtering by end date, if you want to have a single day please only set the start date!")
            .show();
      } else {
        filterData.put("startDate", TimeUtils.formatDate(startDate.getValue()));
        filterData.put("endDate", TimeUtils.formatDate(endDate.getValue()));
        filterData.put("lecturerName", lecturerDropdown.getValue());
        onConfirmCallback.accept(filterData); // Send data back to the parent
      }
    }

    Stage stage = (Stage) confirmBtn.getScene().getWindow();
    stage.close();
  }

  public void setUniqueLecturers(String[] uniqueLecturers) {
    lecturerDropdown.getItems().addAll(uniqueLecturers);
  }

  public void setOnConfirmCallback(Consumer<Map<String, String>> callback) {
    this.onConfirmCallback = callback;
  }

  public void setPreviousData(Map<String, String> filterData) {
    startDate.setValue(TimeUtils.parseDate(filterData.get("startDate")));
    endDate.setValue(TimeUtils.parseDate(filterData.get("endDate")));
    lecturerDropdown.setValue(filterData.get("lecturerName"));
  }

  private void handleCancel() {
    Stage stage = (Stage) cancelBtn.getScene().getWindow();
    stage.close();
  }

  private void handleClear() {
    System.out.println("hello worlddd");
    startDate.setValue(null);
    endDate.setValue(null);
    lecturerDropdown.setValue(null);
  }
}
