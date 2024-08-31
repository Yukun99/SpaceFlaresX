package me.yukun.spaceflares.util;

import org.bukkit.ChatColor;

public class TextFormatter {

  public static String applyColor(String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }
}
