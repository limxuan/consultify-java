package com.consultify.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.consultify.service.UserService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class UserRegistrationController implements Initializable {
  @FXML
  ChoiceBox<String> roleChoiceBox;

  @FXML
  TextField usernameInput;

  @FXML
  TextField passwordInput;

  @FXML
  TextField fullNameInput;

  @FXML
  TextField emailInput;

  @FXML
  TextField phoneInput;

  private String[] roleOptions = { "Student", "Lecturer" };
  private UserService userService = new UserService();

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    roleChoiceBox.getItems().addAll(roleOptions);
  }

  public void backButtonOnClick(MouseEvent e) {
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
    if (usernameInput.getText().isEmpty() ||
        passwordInput.getText().isEmpty() ||
        fullNameInput.getText().isEmpty() ||
        emailInput.getText().isEmpty() ||
        phoneInput.getText().isEmpty() ||
        roleChoiceBox.getValue() == null) {
      new Alert(Alert.AlertType.ERROR, "Please fill all the fields!").show();
      return;
    }

    if (roleChoiceBox.getValue().equals("Student")) {
      String message = userService.registerStudent(usernameInput.getText(), passwordInput.getText(),
          fullNameInput.getText(),
          emailInput.getText(), phoneInput.getText());

      if (message.equals("Registered successfully!")) {
        new Alert(Alert.AlertType.INFORMATION, message + "\nYou can now sign in.").show();
      } else {
        new Alert(Alert.AlertType.ERROR, message).show();
      }
      changeLoginScene("Student", e);
    } else {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Office Location Input");
      dialog.setHeaderText("Where is your office situated?");
      dialog.setContentText("Please enter your office location:");
      Optional<String> result = dialog.showAndWait();

      if (result.isPresent()) {
        String officeLocation = result.get();

        String message = userService.registerLecturer(usernameInput.getText(), passwordInput.getText(),
            phoneInput.getText(),
            fullNameInput.getText(),
            emailInput.getText(), officeLocation);
        if (message.equals("Registered successfully!")) {
          new Alert(Alert.AlertType.INFORMATION, message + "\nYou can now sign in.").show();
          changeLoginScene("Lecturer", e);
        } else {
          new Alert(Alert.AlertType.ERROR, message).show();
        }
      } else {
        System.out.println("User canceled the input");
      }
    }
  }

  public void changeLoginScene(String role, MouseEvent e) {
    // change scenes here
    try

    {
      Parent root = FXMLLoader
          .load(getClass().getResource("/com/consultify/" + roleChoiceBox.getValue() + "LoginPage.fxml"));
      Scene scene = new Scene(root);
      Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
      stage.setScene(scene);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public void clearForm(MouseEvent e) {
    usernameInput.clear();
    passwordInput.clear();
    fullNameInput.clear();
    emailInput.clear();
    phoneInput.clear();
    roleChoiceBox.setValue(null);
  }
}
