package com.consultify.controller;

import java.util.ArrayList;

import com.consultify.utils.SceneSwitcher;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public abstract class SidebarComponentController {
  private ArrayList<Text> pages;
  private String redirectPrefix;

  private String selectedColor = "#394368";

  public void setPages(ArrayList<Text> pages) {
    this.pages = pages;
  }

  public void updateCurrentPage(String currentPage) {
    System.out.println("this is cur page ___----__" + currentPage);
    ArrayList<Text> texts = new ArrayList<>(pages);
    for (Text text : texts) {
      System.out.println(text.getText());
      if (text.getText().equals(currentPage)) {
        text.setStyle("-fx-fill: " + selectedColor);
        text.setCursor(Cursor.DEFAULT);
      } else {
        text.setCursor(Cursor.OPEN_HAND);
      }
    }
  }

  public void clickPage(MouseEvent e) {
    String page = ((Text) e.getSource()).getText();
    String formattedPage = page.replaceAll("\\s", "");
    System.out.println("click page page text what" + page);
    try {
      SceneSwitcher.switchTo(this.redirectPrefix + formattedPage + "Page.fxml", page);
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public void setRedirectPrefix(String prefix) {
    this.redirectPrefix = prefix;
  };
}
