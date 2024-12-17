package com.consultify.controller;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class LecturerSidebarComponentController extends SidebarComponentController {
  @FXML
  private Text homeText;

  @FXML
  private Text manageSlotsText;

  @FXML
  private Text approveRequestsText;

  @FXML
  private Text viewFeedbackText;

  @FXML
  public void initialize() {
    setPages(new ArrayList<Text>(Arrays.asList(homeText, manageSlotsText, approveRequestsText, viewFeedbackText)));
    setRedirectPrefix("Lecturer");
  }
}
