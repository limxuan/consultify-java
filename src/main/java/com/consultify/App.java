package com.consultify;

import javafx.application.Application;
import java.io.IOException;

import com.consultify.utils.SceneSwitcher;
import javafx.stage.Stage;

/**
 * JavaFX Appgroup
 */
public class App extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    SceneSwitcher.init(stage);
    SceneSwitcher.switchTo("StudentLoginPage.fxml", "Login");
  }

  public static void main(String[] args) {
    launch();
  }

}
