package com.consultify.controller;

import com.consultify.utils.SceneSwitcher;

import javafx.fxml.FXML;

public class StudentSidebarBaseController {
  @FXML
  private StudentSidebarComponentController studentSidebarComponentController;

  public void initBase() {
    String currentPage = SceneSwitcher.getCurrentPage();
    System.out.println("ParentPageController: Current page is " + currentPage);
    System.out.println("Sidebar Controller" + studentSidebarComponentController);
    if (studentSidebarComponentController != null) {
      studentSidebarComponentController.updateCurrentPage(currentPage);
    }
  }
}
