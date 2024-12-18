package com.consultify.utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {
  private static Stage primaryStage;
  private static String currentPage;
  private static String curFxmlPath;

  public static void init(Stage stage) {
    stage.setResizable(false);
    stage.setWidth(850);
    stage.setHeight(600);
    primaryStage = stage;
  }

  public static void switchTo(String fxmlPath, String pageName) throws IOException {
    currentPage = pageName;
    curFxmlPath = fxmlPath;
    System.out.println("Switching to: " + fxmlPath);
    System.out.println(" >>>> switch to Current page: " + pageName);
    Parent root = FXMLLoader.load(SceneSwitcher.class.getResource("/com/consultify/" + fxmlPath));
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    System.out.println(">>>> Switched to page: " + currentPage);
  }

  public static void refreshPage() {
    try {
      switchTo(curFxmlPath, currentPage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getCurrentPage() {
    return currentPage;
  }
}
