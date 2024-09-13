package me.yukun.spaceflares.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Serialiser {

  public static int deserialiseTime(List<Integer> list) {
    int secs = 0;
    secs += getSecsFromDays(list.get(0));
    secs += getSecsFromHours(list.get(1));
    secs += getSecsFromMins(list.get(2));
    return secs + list.get(3);
  }

  private static int getSecsFromDays(int days) {
    return days * 24 * 60 * 60;
  }

  private static int getSecsFromHours(int hours) {
    return hours * 60 * 60;
  }

  private static int getSecsFromMins(int mins) {
    return mins * 60;
  }

  public static List<Integer> serialiseTime(int secs) {
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

  private static int getDaysFromSecs(int secs) {
    return secs / 24 / 60 / 60;
  }

  private static int getHoursFromSecs(int secs) {
    return secs / 60 / 60;
  }

  private static int getMinsFromSecs(int secs) {
    return secs / 60;
  }

  public static Location deserialiseLocation(String location) {
    String[] split = location.split("\\s*,\\s*");
    World world = Bukkit.getWorld(split[0]);
    int x = Integer.parseInt(split[1]);
    int y = Integer.parseInt(split[2]);
    int z = Integer.parseInt(split[3]);
    return new Location(world, x, y, z);
  }

  public static String serialiseLocation(Location location) {
    String world = Objects.requireNonNull(location.getWorld()).getName();
    int x = location.getBlockX();
    int y = location.getBlockY();
    int z = location.getBlockZ();
    return world + "," + x + "," + y + "," + z;
  }
}
