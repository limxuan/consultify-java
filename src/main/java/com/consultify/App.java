package com.consultify;

import javafx.application.Application;
import java.io.IOException;

import com.consultify.constants.Role;
import com.consultify.service.UserSession;
import com.consultify.utils.SceneSwitcher;
import javafx.stage.Stage;

/**
 * JavaFX Appgroup
 */
public class App extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    SceneSwitcher.init(stage);
    // NOTE: faking user session remove later
    UserSession.setSession("S006", "student", Role.STUDENT, "Lim Xuan");
    // NOTE: switch to login page later
    System.out.println(UserSession.getUsername());
    SceneSwitcher.switchTo("StudentHomePage.fxml", "Home");
  }

  public static void main(String[] args) {
    launch();
  }

}
