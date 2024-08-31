package me.yukun.spaceflares.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Redeems {

  private static FileConfiguration config;
  private static File file;

  protected static void setup(FileConfiguration config, File file) {
    Redeems.config = config;
    Redeems.file = file;
  }

  /**
   * Gets a map of redeemable flare to amount redeemable for specified player.
   *
   * @param player Player to get redeemable flares for.
   * @return Map of redeemable flare to amount redeemable for specified player.
   */
  public static Map<String, Integer> getRedeems(Player player) {
    Map<String, Integer> redeems = new HashMap<>();
    if (!config.isConfigurationSection(player.getUniqueId().toString())) {
      return redeems;
    }
    ConfigurationSection playerSection = config.getConfigurationSection(
        player.getUniqueId().toString());
    if (playerSection == null) {
      return redeems;
    }
    for (String flare : playerSection.getKeys(false)) {
      int amount = config.getInt(player.getUniqueId() + "." + flare);
      redeems.put(flare, amount);
    }
    return redeems;
  }

  /**
   * Adds specified amount of redeemable flares of specified type to specified player.
   *
   * @param player Player to add redeemable flares for.
   * @param flare  Flare to add to redeemables.
   * @param amount Amount of flare to add to redeemables.
   */
  public static void addRedeems(Player player, String flare, int amount) {
    if (!config.isConfigurationSection(player.getUniqueId().toString()) ||
        !config.isInt(player.getUniqueId() + "." + flare)) {
      config.set(player.getUniqueId() + "." + flare, amount);
      return;
    }
    int currentAmount = config.getInt(player.getUniqueId() + "." + flare);
    config.set(player.getUniqueId() + "." + flare, currentAmount + amount);
    try {
      config.save(file);
    } catch (IOException e) {
      Messages.printSaveError(ConfigTypeEnum.REDEEMS.toString());
    }
  }

  /**
   * Removes specified amount of redeemable flares of specified type from specified player.
   *
   * @param player Player to remove redeemable flares for.
   * @param flare  Flare to remove from redeemables.
   * @param amount Amount of flare to remove from redeemables.
   */
  public static void removeRedeems(Player player, String flare, int amount) {
    if (!config.isConfigurationSection(player.getUniqueId().toString()) ||
        !config.isInt(player.getUniqueId() + "." + flare)) {
      config.set(player.getUniqueId() + "." + flare, amount);
      return;
    }
    int currentAmount = config.getInt(player.getUniqueId() + "." + flare);
    int remain = currentAmount - amount;
    if (remain > 0) {
      config.set(player.getUniqueId() + "." + flare, remain);
    } else {
      config.set(player.getUniqueId() + "." + flare, null);
    }
    try {
      config.save(file);
    } catch (IOException e) {
      Messages.printSaveError(ConfigTypeEnum.REDEEMS.toString());
    }
  }
}
