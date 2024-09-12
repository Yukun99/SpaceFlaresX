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
  public static Map<String, Integer> getFlareRedeems(Player player) {
    Map<String, Integer> redeems = new HashMap<>();
    if (!config.isConfigurationSection(player.getUniqueId().toString())) {
      return redeems;
    }
    ConfigurationSection playerSection = config.getConfigurationSection(
        player.getUniqueId() + ".Flare");
    if (playerSection == null) {
      return redeems;
    }
    for (String flare : playerSection.getKeys(false)) {
      int amount = config.getInt(player.getUniqueId() + ".Flare." + flare);
      redeems.put(flare, amount);
    }
    return redeems;
  }

  /**
   * Gets a map of redeemable envoy flare to amount redeemable for specified player.
   *
   * @param player Player to get redeemable envoy flares for.
   * @return Map of redeemable envoy flare to amount redeemable for specified player.
   */
  public static Map<String, Integer> getEnvoyFlareRedeems(Player player) {
    Map<String, Integer> redeems = new HashMap<>();
    if (!config.isConfigurationSection(player.getUniqueId().toString())) {
      return redeems;
    }
    ConfigurationSection playerSection = config.getConfigurationSection(
        player.getUniqueId() + ".Envoy");
    if (playerSection == null) {
      return redeems;
    }
    for (String envoy : playerSection.getKeys(false)) {
      int amount = config.getInt(player.getUniqueId() + ".Envoy." + envoy);
      redeems.put(envoy, amount);
    }
    return redeems;
  }

  /**
   * Adds specified amount of redeemable flares of specified type to specified player.
   *
   * @param player Player to add redeemable flares to.
   * @param flare  Flare to add to redeemables.
   * @param amount Amount of flare to add to redeemables.
   */
  public static void addFlareRedeems(Player player, String flare, int amount) {
    if (!config.isConfigurationSection(player.getUniqueId().toString()) ||
        !config.isInt(player.getUniqueId() + ".Flare." + flare)) {
      config.set(player.getUniqueId() + ".Flare." + flare, amount);
      return;
    }
    int currentAmount = config.getInt(player.getUniqueId() + ".Flare." + flare);
    config.set(player.getUniqueId() + ".Flare." + flare, currentAmount + amount);
    try {
      config.save(file);
    } catch (IOException e) {
      Messages.printSaveError(ConfigTypeEnum.REDEEMS.toString());
    }
  }

  /**
   * Adds specified amount of redeemable envoy flares of specified type to specified player.
   *
   * @param player Player to add redeemable envoy flares to.
   * @param envoy  Envoy flare to add to redeemables.
   * @param amount Amount of envoy flare to add to redeemables.
   */
  public static void addEnvoyFlareRedeems(Player player, String envoy, int amount) {
    if (!config.isConfigurationSection(player.getUniqueId().toString()) ||
        !config.isInt(player.getUniqueId() + ".Envoy." + envoy)) {
      config.set(player.getUniqueId() + ".Envoy." + envoy, amount);
      return;
    }
    int currentAmount = config.getInt(player.getUniqueId() + ".Envoy." + envoy);
    config.set(player.getUniqueId() + ".Envoy." + envoy, currentAmount + amount);
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
  public static void removeFlareRedeems(Player player, String flare, int amount) {
    if (!config.isConfigurationSection(player.getUniqueId().toString()) ||
        !config.isInt(player.getUniqueId() + ".Flare." + flare)) {
      config.set(player.getUniqueId() + ".Flare." + flare, amount);
      return;
    }
    int currentAmount = config.getInt(player.getUniqueId() + ".Flare." + flare);
    int remain = currentAmount - amount;
    if (remain > 0) {
      config.set(player.getUniqueId() + ".Flare." + flare, remain);
    } else {
      config.set(player.getUniqueId() + ".Flare." + flare, null);
    }
    try {
      config.save(file);
    } catch (IOException e) {
      Messages.printSaveError(ConfigTypeEnum.REDEEMS.toString());
    }
  }

  /**
   * Removes specified amount of redeemable envoy flares of specified type from specified player.
   *
   * @param player Player to remove redeemable envoy flares for.
   * @param envoy  Envoy flare to remove from redeemables.
   * @param amount Amount of envoy flare to remove from redeemables.
   */
  public static void removeEnvoyFlareRedeems(Player player, String envoy, int amount) {
    if (!config.isConfigurationSection(player.getUniqueId().toString()) ||
        !config.isInt(player.getUniqueId() + ".Envoy." + envoy)) {
      config.set(player.getUniqueId() + ".Envoy." + envoy, amount);
      return;
    }
    int currentAmount = config.getInt(player.getUniqueId() + ".Envoy." + envoy);
    int remain = currentAmount - amount;
    if (remain > 0) {
      config.set(player.getUniqueId() + ".Envoy." + envoy, remain);
    } else {
      config.set(player.getUniqueId() + ".Envoy." + envoy, null);
    }
    try {
      config.save(file);
    } catch (IOException e) {
      Messages.printSaveError(ConfigTypeEnum.REDEEMS.toString());
    }
  }
}
