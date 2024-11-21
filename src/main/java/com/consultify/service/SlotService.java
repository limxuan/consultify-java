package com.consultify.service;

import java.util.ArrayList;

public class SlotService {
  private DatabaseService slotDatabaseService = new DatabaseService("slots.txt");

  public String[] getSlotFromId(String slotId) {
    System.out.println("getslotfromid slotId: " + slotId);
    ArrayList<String[]> records = slotDatabaseService.parseContent();
    for (String[] record : records) {
      if (record[0].equals(slotId)) {
        return record;
      }
    }
    return null;
  }
}
