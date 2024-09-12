package me.yukun.spaceflares.util;

import java.util.ArrayList;
import java.util.List;

public class Time {

  public int getSecsFromList(List<Integer> list) {
    int secs = 0;
    secs += getSecsFromDays(list.get(0));
    secs += getSecsFromHours(list.get(1));
    secs += getSecsFromMins(list.get(2));
    return secs + list.get(3);
  }

  private int getSecsFromDays(int days) {
    return days * 24 * 60 * 60;
  }

  private int getSecsFromHours(int hours) {
    return hours * 60 * 60;
  }

  private int getSecsFromMins(int mins) {
    return mins * 60;
  }

  public List<Integer> getListFromSecs(int secs) {
    int day = getDaysFromSecs(secs);
    secs -= getSecsFromDays(day);
    int hour = getHoursFromSecs(secs);
    secs -= getSecsFromHours(hour);
    int min = getMinsFromSecs(secs);
    secs -= getSecsFromMins(min);
    int sec = secs;
    return new ArrayList<>() {{
      add(day);
      add(hour);
      add(min);
      add(sec);
    }};
  }

  private int getDaysFromSecs(int secs) {
    return secs / 24 / 60 / 60;
  }

  private int getHoursFromSecs(int secs) {
    return secs / 60 / 60;
  }

  private int getMinsFromSecs(int secs) {
    return secs / 60;
  }
}
