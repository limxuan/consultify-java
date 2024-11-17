package com.consultify.controller;

import com.consultify.service.UserService;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

public class LecturerLoginController {
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
    Boolean validUser = userService.validateLecturer(username, password);
    System.out.println(validUser);
    if (!validUser) {
      errorMessage("Invalid username or password!");
      passwordInput.clear();
      return;
    }
    warningText.setText("");
    System.out.println("Sign In");
  }

  public void studentLoginTextOnClick() {
    System.out.println("Lecturer Login Clicked");
    // try {
    // Parent root = FXMLLoader.load(getClass().getResource("LecturerLogin.fxml"));
    // Scene scene = new Scene(root);
    // Stage stage = new Stage();
    // stage.setScene(scene);
    // stage.setResizable(false);
    // stage.show();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
  }

  private void errorMessage(String msg) {
    warningText.setText("* " + msg);
  }
}
