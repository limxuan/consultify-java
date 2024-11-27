package com.consultify.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.consultify.service.UserSession;
import com.consultify.utils.SceneSwitcher;
import com.consultify.utils.StringUtils;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class StudentSidebarComponentController {
  @FXML
  private Text homeText;

  @FXML
  private Text bookAppointmentText;

  @FXML
  private Text rescheduleText;

  @FXML
  private Text historyText;

  @FXML
  private Text feedbackText;

  private String selectedColor = "#394368";

  public void updateCurrentPage(String currentPage) {
    ArrayList<Text> texts = new ArrayList<>(
        Arrays.asList(homeText, bookAppointmentText, rescheduleText, historyText, feedbackText));
    for (Text text : texts) {
      if (text.getText().equals(currentPage)) { // Use equals for string comparison
        text.setStyle("-fx-fill: " + selectedColor);
        text.setCursor(Cursor.DEFAULT);
      } else {
        text.setCursor(Cursor.OPEN_HAND);
      }
    }
  }

  public void redirectHome(MouseEvent e) throws IOException {
    SceneSwitcher.switchTo("StudentHomePage.fxml", "Home");
  }

  public void clickPage(MouseEvent e) {
    String page = ((Text) e.getSource()).getText();
    String formattedPage = page.replaceAll("\\s", "");
    try {
      System.out.println("Redirecting to: " + "Student" + formattedPage + "Page.fxml");
      SceneSwitcher.switchTo("Student" + formattedPage + "Page.fxml", page);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public void redirectBookAppointment(MouseEvent e) throws IOException {
    SceneSwitcher.switchTo("StudentBookAppointmentPage.fxml", "Book Appointment");
  }
}
