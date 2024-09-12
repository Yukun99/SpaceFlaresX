package me.yukun.spaceflares.envoy.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import me.yukun.spaceflares.SpaceFlares;
import me.yukun.spaceflares.config.EnvoyConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.envoy.Envoy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Vibration;
import org.bukkit.Vibration.Destination.BlockDestination;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnvoyEditor {

  private static final Map<String, Set<Player>> envoyEditorsMap = new HashMap<>();
  private static final Map<Player, EnvoyEditor> playerEditorMap = new HashMap<>();
  private static final Map<String, List<Material>> envoyBlocksMap = new HashMap<>();
  private static final Set<String> queuedEnvoys = new HashSet<>();

  private final Player player;
  private final ItemStack[] contents;

  public static void onDisable() {
    stopAllEditing();
  }

  public static void reload() {
    stopAllEditing();
  }

  private static void stopAllEditing() {
    for (String envoy : envoyEditorsMap.keySet()) {
      for (Player player : envoyEditorsMap.get(envoy)) {
        toggleEditing(envoy, player);
      }
    }
  }

  /**
   * Toggles the editing status of a player.
   *
   * @param envoy  Envoy to attempt toggle editing status for.
   * @param player Player to attempt toggle editing status for.
   */
  public static void toggleEditing(String envoy, Player player) {
    String editing = getEditedEnvoy(player);
    if (editing != null) {
      // Player is editing
      if (!editing.equals(envoy)) {
        // Player wants to edit a different envoy, send disallow message.
        Messages.sendEnvoyEditEditing(player);
      } else {
        // Player wants to stop editing stated envoy.
        stopEdit(envoy, player);
        Messages.sendEnvoyEditStop(player, envoy);
      }
    } else {
      // Player wants to start editing stated envoy.
      startEdit(envoy, player);
      Messages.sendEnvoyEditStart(player, envoy);
    }
  }

  /**
   * Gets the envoy that specified player is editing.
   *
   * @param player Player to get edited envoy for.
   * @return Envoy edited by player if present, null otherwise.
   */
  public static String getEditedEnvoy(Player player) {
    for (String envoy : envoyEditorsMap.keySet()) {
      Set<Player> editors = envoyEditorsMap.get(envoy);
      if (editors.contains(player)) {
        return envoy;
      }
    }
    return null;
  }

  public static boolean isEditing(String envoy) {
    return !hasNoEditors(envoy);
  }

  public static void queueEnvoy(String envoy) {
    queuedEnvoys.add(envoy);
  }

  private static void startEdit(String envoy, Player player) {
    if (hasNoEditors(envoy)) {
      setupFirstEdit(envoy);
    }
    Set<Player> editors = envoyEditorsMap.get(envoy);
    editors.add(player);
    playerEditorMap.put(player, new EnvoyEditor(player));
  }

  private static void stopEdit(String envoy, Player player) {
    Set<Player> editors = envoyEditorsMap.get(envoy);
    editors.remove(player);
    playerEditorMap.get(player).revertInventory();
    playerEditorMap.remove(player);
    if (hasNoEditors(envoy)) {
      hideEditLocations(envoy);
      if (queuedEnvoys.contains(envoy)) {
        queuedEnvoys.remove(envoy);
        Envoy.startEnvoy(envoy);
      }
    }
  }

  private static boolean hasNoEditors(String envoy) {
    if (!envoyEditorsMap.containsKey(envoy)) {
      return true;
    }
    Set<Player> editors = envoyEditorsMap.get(envoy);
    return editors.isEmpty();
  }

  private static void setupFirstEdit(String envoy) {
    Set<Player> editors = new HashSet<>();
    envoyEditorsMap.put(envoy, editors);
    showEditLocations(envoy);
  }

  private static void showEditLocations(String envoy) {
    List<Material> types = new ArrayList<>();
    envoyBlocksMap.put(envoy, types);
    for (Location location : EnvoyConfig.getEnvoyLocations(envoy)) {
      Block block = location.getBlock();
      types.add(block.getType());
      block.setType(Material.BEDROCK);
    }
  }

  private static void hideEditLocations(String envoy) {
    List<Material> types = envoyBlocksMap.get(envoy);
    List<Location> locations = EnvoyConfig.getEnvoyLocations(envoy);
    for (int i = 0; i < types.size(); i++) {
      Block block = locations.get(i).getBlock();
      block.setType(types.get(i));
    }
    envoyBlocksMap.remove(envoy);
  }

  public static void addEnvoyLocation(String envoy, Location saved, Location placed) {
    Material original = saved.getBlock().getType();
    List<Material> materials = envoyBlocksMap.get(envoy);
    materials.add(original);
    placed.getBlock().setType(Material.AIR);
    saved.getBlock().setType(Material.BEDROCK);
    playSavedParticles(placed, saved);
  }

  public static Integer getEnvoyLocationIndex(String envoy, Location location) {
    List<Location> locations = EnvoyConfig.getEnvoyLocations(envoy);
    for (int i = 0; i < locations.size(); i++) {
      Location saved = locations.get(i);
      if (!Objects.equals(saved.getWorld(), location.getWorld())) {
        continue;
      }
      if (saved.getBlockX() != location.getBlockX() || saved.getBlockY() != location.getBlockY()
          || saved.getBlockZ() != location.getBlockZ()) {
        continue;
      }
      return i;
    }
    return null;
  }

  public static void removeEnvoyLocation(String envoy, Location location, int index) {
    List<Material> materials = envoyBlocksMap.get(envoy);
    Material material = materials.get(index);
    materials.remove(index);
    location.getBlock().setType(material);
  }

  private static void playSavedParticles(Location from, Location to) {
    Vibration data = new Vibration(from, new BlockDestination(to), 5);
    int spawnTask = Bukkit.getScheduler()
        .scheduleSyncRepeatingTask(SpaceFlares.getPlugin(),
            () -> Objects.requireNonNull(from.getWorld())
                .spawnParticle(Particle.VIBRATION, from, 1, data), 2, 2);
    Bukkit.getScheduler().scheduleSyncDelayedTask(SpaceFlares.getPlugin(),
        () -> Bukkit.getScheduler().cancelTask(spawnTask), 30);
  }

  private EnvoyEditor(Player player) {
    this.player = player;
    this.contents = player.getInventory().getContents();
    player.getInventory().setContents(new ItemStack[0]);
    player.getInventory().setItemInMainHand(new ItemStack(Material.BEDROCK));
  }

  private void revertInventory() {
    player.getInventory().setContents(contents);
  }
}
