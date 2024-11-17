package com.consultify.controller;

import com.consultify.service.UserService;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
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

  @FXML
  Text warningText;

  private UserService userService = new UserService();

  public void signInBtnOnClick() {
    String username = usernameInput.getText();
    String password = passwordInput.getText();
    Boolean validUser = userService.validateStudent(username, password);
    System.out.println(validUser);
    if (!validUser) {
      errorMessage("Invalid username or password!");
      passwordInput.clear();
      return;
    }
    warningText.setText("");
    System.out.println("Sign In");
  }

  public void lecturerLoginTextOnClick(MouseEvent e) {
    System.out.println("Lecturer Login Clicked");
    try {
    Parent root = FXMLLoader.load(getClass().getResource("/com/consultify/LecturerLoginPage.fxml"));
    Scene scene = new Scene(root);
    Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    stage.setScene(scene);
    } catch (Exception err) {
    err.printStackTrace();
    }
  }

  private void errorMessage(String msg) {
    warningText.setText("* " + msg);
  }
}
