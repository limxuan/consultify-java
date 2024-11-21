package com.consultify.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.consultify.service.DatabaseService;
import com.consultify.service.UserService;
import com.consultify.service.UserSession;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StudentLoginController {
  @FXML
  TextField usernameInput;

  @FXML
  PasswordField passwordInput;

  private UserService userService = new UserService();

  public void signInBtnOnClick() {
    String username = usernameInput.getText();
    String password = passwordInput.getText();
    String[] userCredentials = userService.loginStudent(username, password);
    System.out.println(Arrays.toString(userCredentials));
    if (userCredentials == null) {
      new Alert(Alert.AlertType.ERROR, "Invalid username or password!").show();
      passwordInput.clear();
      return;
    }
    new Alert(Alert.AlertType.INFORMATION, "Signed in as: " + userCredentials[6]).show();
    System.out.println(UserSession.stringify());
    redirectStudentHome();
  }

  public void lecturerLoginTextOnClick(MouseEvent e) {
    System.out.println("Lecturer Login Clicked");
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/com/consultify/LecturerLoginPage.fxml"));
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

  public void redirectStudentHome() {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/com/consultify/StudentHome.fxml"));
      Scene scene = new Scene(root);
      Stage stage = (Stage) ((Node) this.usernameInput).getScene().getWindow();
      stage.setScene(scene);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

}
