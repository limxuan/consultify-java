package com.consultify.controller;

import java.util.Arrays;

import com.consultify.service.UserService;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class LecturerLoginController {
  @FXML
  TextField usernameInput;

  @FXML
  PasswordField passwordInput;

  private UserService userService = new UserService();

  public void signInBtnOnClick() {
    String username = usernameInput.getText();
    String password = passwordInput.getText();
    String[] userCredentials = userService.validateLecturer(username, password);
    System.out.println(Arrays.toString(userCredentials));
    if (userCredentials == null) {
      new Alert(Alert.AlertType.ERROR, "Invalid username or password!").show();
      passwordInput.clear();
      return;
    }
    new Alert(Alert.AlertType.INFORMATION, "Signed in as: " + userCredentials[5]).show();
  }

  public void studentLoginTextOnClick(MouseEvent e) {
    System.out.println("Lecturer Login Clicked");
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
