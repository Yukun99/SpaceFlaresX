package me.yukun.spaceflares.envoy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.yukun.spaceflares.SpaceFlares;
import me.yukun.spaceflares.config.EnvoyConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.envoy.edit.EnvoyEditor;
import me.yukun.spaceflares.util.Time;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Envoy {

  private static final Map<String, Envoy> typeEnvoyActiveMap = new HashMap<>();
  private static final Map<String, Envoy> typeEnvoyMap = new HashMap<>();
  private static final Time TIME = new Time();

  private final String type;
  private final Set<EnvoyFlare> flares = new HashSet<>();
  private Integer durationTimer;
  private int duration = 0;
  private Integer cooldownTimer;
  private int cooldown;

  private Envoy(String type) {
    this.type = type;
    this.duration = EnvoyConfig.getEnvoyDuration(type);
    stopCooldownTimer();
    startDurationTimer();
    summonFlares();
    typeEnvoyActiveMap.put(type, this);
    typeEnvoyMap.put(type, this);
  }

  private void summonFlares() {
    Map<String, Integer> crates = EnvoyConfig.getEnvoyCrates(type);
    int count = crates.values().stream().reduce(0, Integer::sum);
    List<Location> locations = EnvoyConfig.getEnvoyRandomLocations(type, count);
    for (String crate : crates.keySet()) {
      for (int i = 0; i < crates.get(crate); i++) {
        Location location = locations.get(i);
        flares.add(new EnvoyFlare(crate, location, this));
      }
    }
  }

  private Envoy(String type, int cooldown) {
    this.type = type;
    this.cooldown = cooldown;
    startCooldownTimer();
    typeEnvoyMap.put(type, this);
  }

  public static void onDisable() {
    stopAllEnvoys();
    for (Envoy envoy : typeEnvoyMap.values()) {
      EnvoyConfig.saveEnvoyCooldown(envoy.type, TIME.getListFromSecs(envoy.cooldown));
    }
  }

  public static void reload() {
    stopAllEnvoys();
  }

  private static void stopAllEnvoys() {
    for (String envoyName : typeEnvoyActiveMap.keySet()) {
      Envoy envoy = typeEnvoyActiveMap.get(envoyName);
      envoy.disable();
    }
    typeEnvoyActiveMap.clear();
  }

  /**
   * Summons an envoy.
   * <p>Does not start if envoy of specified type is already active.</p>
   *
   * @param sender CommandSender who summoned envoy.
   * @param type   Type of envoy to summon.
   * @return True if envoy summons successfully, False otherwise.
   */
  public static Envoy senderSummonEnvoy(CommandSender sender, String type) {
    if (isActive(type) || EnvoyEditor.isEditing(type)) {
      return null;
    }
    Envoy envoy = new Envoy(type);
    if (EnvoyConfig.doEnvoyCooldownReset(type)) {
      envoy.cooldown = EnvoyConfig.getEnvoyCooldown(type);
    }
    typeEnvoyActiveMap.put(type, envoy);

    List<Integer> durationList = TIME.getListFromSecs(envoy.duration);
    Messages.sendEnvoySummon(sender, type, envoy.flares.size(), durationList);
    return envoy;
  }

  /**
   * Summons an envoy.
   * <p>Does not start if envoy of specified type is already active.</p>
   *
   * @param player Player who summoned envoy.
   * @param type   Type of envoy to summon.
   * @return True if envoy summons successfully, False otherwise.
   */
  public static boolean playerSummonEnvoy(Player player, String type) {
    Envoy envoy = senderSummonEnvoy(player, type);
    if (envoy == null) {
      return false;
    }
    envoy.consumeFlare(player);
    List<Integer> durationList = TIME.getListFromSecs(envoy.duration);
    Messages.sendEnvoyStart(player, type, envoy.flares.size(), durationList);
    return true;
  }

  /**
   * Consumes a single envoy flare item currently held by player.
   */
  private void consumeFlare(Player player) {
    if (player == null) {
      return;
    }
    ItemStack main = player.getInventory().getItemInMainHand();
    ItemStack off = player.getInventory().getItemInOffHand();
    if (EnvoyConfig.getEnvoyFlareFromItem(main) != null) {
      main.setAmount(main.getAmount() - 1);
    } else {
      off.setAmount(off.getAmount() - 1);
    }
  }

  /**
   * Starts an envoy.
   * <p>Does not start if envoy of specified type is already active.</p>
   *
   * @param type Type of envoy to start.
   */
  public static void startEnvoy(String type) {
    if (isActive(type)) {
      return;
    }
    if (EnvoyEditor.isEditing(type)) {
      return;
    }
    Envoy envoy = new Envoy(type);
    typeEnvoyActiveMap.put(type, envoy);
    Messages.sendEnvoyStart(null, type, envoy.flares.size(), TIME.getListFromSecs(envoy.duration));
  }

  /**
   * Starts an envoy in cooldown mode.
   * <p>Does not start if envoy of specified type is already active.</p>
   *
   * @param type     Type of envoy to start.
   * @param cooldown Cooldown to assign to envoy.
   */
  public static void startEnvoyCooldown(String type, int cooldown) {
    if (isActive(type)) {
      return;
    }
    new Envoy(type, cooldown);
  }

  /**
   * Stops an ongoing envoy.
   *
   * @param type Type of envoy stopped.
   * @return True if ongoing envoy stopped, False otherwise.
   */
  public static boolean stopEnvoy(String type) {
    return typeEnvoyActiveMap.get(type).end();
  }

  /**
   * Checks if specified envoy is active.
   *
   * @param type Type of envoy to be checked.
   * @return Whether specified envoy is active.
   */
  public static boolean isActive(String type) {
    return typeEnvoyActiveMap.containsKey(type);
  }

  public static void startCooldownTimer(String envoy) {
    typeEnvoyMap.get(envoy).startCooldownTimer();
  }

  public static void stopCooldownTimer(String envoy) {
    typeEnvoyMap.get(envoy).stopCooldownTimer();
  }

  private void startCooldownTimer() {
    cooldownTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(SpaceFlares.getPlugin(), () -> {
      cooldown -= 1;
      if (cooldown == 0) {
        startEnvoy(type);
      }
    }, 20, 20);
  }

  private void stopCooldownTimer() {
    if (cooldownTimer == null) {
      return;
    }
    Bukkit.getScheduler().cancelTask(cooldownTimer);
    cooldownTimer = null;
  }

  private void startDurationTimer() {
    durationTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(SpaceFlares.getPlugin(), () -> {
      duration -= 1;
      if (duration == 0) {
        end();
      }
    }, 20, 20);
  }

  private void stopDurationTimer() {
    if (durationTimer == null) {
      return;
    }
    Bukkit.getScheduler().cancelTask(durationTimer);
    durationTimer = null;
  }

  private void disable() {
    clearAllFlares();
    stopDurationTimer();
    stopCooldownTimer();
  }

  private boolean end() {
    if (!typeEnvoyActiveMap.containsKey(type)) {
      return false;
    }
    clearAllFlares();
    stopDurationTimer();
    if (EnvoyConfig.doEnvoyCooldownEnable(type)) {
      if (cooldown == 0) {
        cooldown = EnvoyConfig.getEnvoyCooldown(type);
      }
      startCooldownTimer();
    }
    typeEnvoyActiveMap.remove(type);
    Messages.sendEnvoyEnd(type, TIME.getListFromSecs(cooldown));
    return true;
  }

  private void clearAllFlares() {
    for (EnvoyFlare flare : flares) {
      flare.clearCrate();
      flare.endFlare();
      flare.deregisterFlare();
    }
    flares.clear();
  }

  public void deregisterFlare(EnvoyFlare flare) {
    flares.remove(flare);
    if (flares.isEmpty()) {
      end();
    }
  }

  public static int getEnvoyRemain(String type) {
    return typeEnvoyActiveMap.get(type).getRemain();
  }

  public static List<Integer> getEnvoyDuration(String type) {
    return typeEnvoyActiveMap.get(type).getDuration();
  }

  public static List<Integer> getEnvoyCooldown(String type) {
    return typeEnvoyMap.get(type).getCooldown();
  }

  protected int getRemain() {
    return flares.size();
  }

  private List<Integer> getDuration() {
    return TIME.getListFromSecs(duration);
  }

  private List<Integer> getCooldown() {
    return TIME.getListFromSecs(cooldown);
  }
}
