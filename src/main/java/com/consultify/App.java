package com.consultify;

import java.io.IOException;

import com.consultify.constants.Role;
import com.consultify.service.AppointmentService;
import com.consultify.service.UserSession;
import com.consultify.utils.SceneSwitcher;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX Appgroup
 */
public class App extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    SceneSwitcher.init(stage);
    SceneSwitcher.switchTo("StudentLoginPage.fxml", "Home");
  }

  public static void main(String[] args) {
    launch();
  }
}
