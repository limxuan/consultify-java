package com.consultify.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseService {
  public String filePath;
  public File file;

  public DatabaseService(String fileName) {
    this.filePath = "src/main/resources/com/consultify/database/" + fileName;

    this.file = new File(filePath);
    try {
      if (!file.exists()) {
        boolean isFileCreated = file.createNewFile();
        if (isFileCreated) {
          System.out.println("File created: " + filePath);
        }
      }
    } catch (Exception e) {
      System.err.println("Error while initializing the database file: " + e.getMessage());
    }
  }

  public String getFilePath() {
    return filePath;
  }

  public String getContent() {
    // use string builder memory overhead compared to concatenating strings with +
    StringBuilder content = new StringBuilder();
    try (Scanner myReader = new Scanner(file)) { // Auto-close the scanner
      while (myReader.hasNextLine()) {
        content.append(myReader.nextLine()).append(System.lineSeparator());
      }
    } catch (IOException e) {
      System.err.println("Error while reading the database file: " + e.getMessage());
    }
    return content.toString().trim();
  }

  public ArrayList<String[]> parseContent() {
    ArrayList<String[]> records = new ArrayList<String[]>();
    if (this.file.length() == 0) {
      System.err.println("The file is empty.");
      return records;
    }

    try (Scanner myReader = new Scanner(file)) {
      if (myReader.hasNextLine()) {
        myReader.nextLine();
      }
      while (myReader.hasNextLine()) {
        String line = myReader.nextLine().trim();
        if (!line.isEmpty()) {
          String[] record = line.split("\\|");
          records.add(record);
        }
      }
    } catch (IOException e) {
      System.err.println("Error while reading the database file: " + e.getMessage());
    }
    return records;
  }

  public void saveData(ArrayList<String[]> data) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(this.file));
      String dataHeaders = br.readLine();
      br.close();

      try (FileWriter fw = new FileWriter(this.file)) {
        fw.write(dataHeaders + "\n");
        for (String[] record : data) {
          String formattedRecord = String.join("|", record);
          fw.write(formattedRecord + "\n");
        }

      } catch (IOException e) {
        System.err.println("Error while writing to the database file: " + e.getMessage());
      }
    } catch (IOException e) {
      System.err.println("Error while writing to the database file: " + e.getMessage());
    }
  }
}
