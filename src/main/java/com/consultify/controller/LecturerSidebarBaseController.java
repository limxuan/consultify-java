package com.consultify.controller;

import com.consultify.utils.SceneSwitcher;

import javafx.fxml.FXML;

public class LecturerSidebarBaseController {
  @FXML
  private LecturerSidebarComponentController lecturerSidebarComponentController;

  public void initBase() {
    String currentPage = SceneSwitcher.getCurrentPage();
    System.out.println("ParentPageController: Current page is " + currentPage);
    System.out.println("Sidebar Controller" + lecturerSidebarComponentController);
    if (lecturerSidebarComponentController != null) {
      lecturerSidebarComponentController.updateCurrentPage(currentPage);
    }
  }
}
