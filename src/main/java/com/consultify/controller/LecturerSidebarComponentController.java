package com.consultify.controller;

import java.util.ArrayList;
import java.util.Arrays;

import com.consultify.utils.SceneSwitcher;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class LecturerSidebarComponentController {
  @FXML
  private Text homeText;

  @FXML
  private Text manageSlotsText;

  @FXML
  private Text approveRequestsText;

  @FXML
  private Text viewFeedbackText;

  private String selectedColor = "#394368";

  public void updateCurrentPage(String currentPage) {
    ArrayList<Text> texts = new ArrayList<>(
        Arrays.asList(homeText, manageSlotsText, approveRequestsText, viewFeedbackText));
    for (Text text : texts) {
      if (text.getText().equals(currentPage)) { // Use equals for string comparison
        text.setStyle("-fx-fill: " + selectedColor);
        text.setCursor(Cursor.DEFAULT);
      } else {
        text.setCursor(Cursor.OPEN_HAND);
      }
    }
  }

  public void clickPage(MouseEvent e) {
    String page = ((Text) e.getSource()).getText();
    String formattedPage = page.replaceAll("\\s", "");
    try {
      SceneSwitcher.switchTo("Lecturer" + formattedPage + "Page.fxml", page);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }
}
