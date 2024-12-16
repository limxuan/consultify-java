package com.consultify.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.consultify.utils.TimeUtils;

public class SlotService {
  private DatabaseService slotDatabaseService = new DatabaseService("slots.txt");
  private UserService userService = new UserService();

  public String[] getSlotFromId(String slotId) {
    ArrayList<String[]> records = slotDatabaseService.parseContent();
    for (String[] record : records) {
      if (record[0].equals(slotId)) {
        return record;
      }
    }
    return null;
  }

  public ArrayList<String[]> getAvailableSlots() {
    ArrayList<String[]> records = slotDatabaseService.parseContent();
    return records.stream().filter(record -> record[4].equals("true")
        && TimeUtils.isBefore(record[2]))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public List<Set<String>> getUniqueLecturersAndDates(ArrayList<String[]> slots) {
    Set<String> uniqueLecturers = new HashSet<>();
    Set<String> uniqueDates = new HashSet<>();

    for (String[] slot : slots) {
      String[] lecturer = userService.getLecturerById(slot[1]);

      uniqueLecturers.add(lecturer[5]);
      uniqueDates.add(TimeUtils.getDate(slot[3]));
    }

    System.out.println(Arrays.asList(uniqueLecturers, uniqueDates));
    return Arrays.asList(uniqueLecturers, uniqueDates);
  }

  public void setAvailable(String slotId, String available) {
    ArrayList<String[]> records = slotDatabaseService.parseContent();
    for (String[] record : records) {
      if (record[0].equals(slotId)) {
        record[4] = available;
        break;
      }
    }
    slotDatabaseService.saveData(records);
  }

  public ArrayList<String[]> getAvailableSlotsForLecturer(String lecturerId) {
    ArrayList<String[]> records = slotDatabaseService.parseContent();
    return records.stream().filter(record -> record[1].equals(lecturerId) && record[4].equals("true"))
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
