package com.consultify.controller;

import com.consultify.service.UserService;
import com.consultify.utils.SceneSwitcher;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LecturerLoginController {
  @FXML
  TextField usernameInput;

  @FXML
  PasswordField passwordInput;

  private UserService userService = new UserService();

  public void signInBtnOnClick() {
    String username = usernameInput.getText();
    String password = passwordInput.getText();
    String[] userCredentials = userService.loginLecturer(username, password);
    if (userCredentials == null) {
      new Alert(Alert.AlertType.ERROR, "Invalid username or password!").show();
      passwordInput.clear();
      return;
    }
    new Alert(Alert.AlertType.INFORMATION, "Signed in as: " + userCredentials[5]).show();
    redirectLecturerHome();
  }

  public void redirectLecturerHome() {
    try {
      SceneSwitcher.switchTo("LecturerHomePage.fxml", "Home");
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public void studentLoginTextOnClick(MouseEvent e) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/com/consultify/StudentLoginPage.fxml"));
      Scene scene = new Scene(root);
      Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
      stage.setScene(scene);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public void registerBtnOnClick(MouseEvent e) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/com/consultify/UserRegistration.fxml"));
      Scene scene = new Scene(root);
      Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
      stage.setScene(scene);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

}
