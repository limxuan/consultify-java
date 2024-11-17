package com.consultify;

import javafx.application.Application;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX Appgroup
 */
public class App extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.setResizable(false);
      stage.setWidth(850);
      stage.setHeight(600);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch();
  }

}
