package com.consultify.controller;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class StudentSidebarComponentController extends SidebarComponentController {
  @FXML
  private Text homeText;

  @FXML
  private Text bookAppointmentText;

  @FXML
  private Text historyText;

  @FXML
  public void initialize() {
    setPages(new ArrayList<Text>(Arrays.asList(homeText, bookAppointmentText, historyText)));
    setRedirectPrefix("Student");
  }
}
