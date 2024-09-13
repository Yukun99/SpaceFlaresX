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
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnvoyEditor {

  private static final Map<String, Set<Player>> envoyEditorsMap = new HashMap<>();
  private static final Map<Player, EnvoyEditor> playerEditorMap = new HashMap<>();
  private static final Map<String, List<Material>> envoyBlocksMap = new HashMap<>();
  private static final Map<String, List<Location>> envoyLocationsMap = new HashMap<>();

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
        stopEdit(envoy, player, true);
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
        stopEdit(envoy, player, false);
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

  private static void startEdit(String envoy, Player player) {
    if (hasNoEditors(envoy)) {
      setupFirstEdit(envoy);
      Envoy.stopCooldownTimer(envoy);
    }
    Set<Player> editors = envoyEditorsMap.get(envoy);
    editors.add(player);
    playerEditorMap.put(player, new EnvoyEditor(player));
  }

  private static void stopEdit(String envoy, Player player, boolean isDisable) {
    Set<Player> editors = envoyEditorsMap.get(envoy);
    editors.remove(player);
    playerEditorMap.get(player).revertInventory();
    playerEditorMap.remove(player);
    if (hasNoEditors(envoy)) {
      if (!isDisable) {
        Envoy.startCooldownTimer(envoy);
      }
      hideEditLocations(envoy);
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
    List<Location> locations = new ArrayList<>();
    envoyLocationsMap.put(envoy, locations);
    List<Material> types = new ArrayList<>();
    envoyBlocksMap.put(envoy, types);
    for (Location location : EnvoyConfig.getEnvoyLocations(envoy)) {
      locations.add(location);
      Block block = location.getBlock();
      types.add(block.getType());
      block.setType(Material.BEDROCK);
    }
  }

  private static void hideEditLocations(String envoy) {
    List<Location> locations = envoyLocationsMap.get(envoy);
    List<Material> types = envoyBlocksMap.get(envoy);
    for (int i = 0; i < types.size(); i++) {
      Block block = locations.get(i).getBlock();
      block.setType(types.get(i));
    }
    envoyLocationsMap.remove(envoy);
    envoyBlocksMap.remove(envoy);
  }

  public static void addEnvoyLocation(String envoy, Location saved, Location placed) {
    List<Location> locations = envoyLocationsMap.get(envoy);
    locations.add(saved);
    Material original = saved.getBlock().getType();
    List<Material> materials = envoyBlocksMap.get(envoy);
    materials.add(original);
    placed.getBlock().setType(Material.AIR);
    saved.getBlock().setType(Material.BEDROCK);
    playSaveParticles(placed.add(0.5, 0.5, 0.5), saved);
  }

  public static Integer getEnvoyLocationIndex(String envoy, Location location) {
    List<Location> locations = envoyLocationsMap.get(envoy);
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
    List<Location> locations = envoyLocationsMap.get(envoy);
    locations.remove(index);
    List<Material> materials = envoyBlocksMap.get(envoy);
    Material material = materials.get(index);
    materials.remove(index);
    location.getBlock().setType(material);
    playDeleteParticles(location.add(0.5, 0.5, 0.5));
  }

  private static void playSaveParticles(Location from, Location to) {
    Vibration data = new Vibration(from, new BlockDestination(to), 5);
    int spawnTask = Bukkit.getScheduler()
        .scheduleSyncRepeatingTask(SpaceFlares.getPlugin(),
            () -> Objects.requireNonNull(from.getWorld())
                .spawnParticle(Particle.VIBRATION, from, 3, data), 1, 1);
    Bukkit.getScheduler().scheduleSyncDelayedTask(SpaceFlares.getPlugin(),
        () -> Bukkit.getScheduler().cancelTask(spawnTask), 40);
  }

  private static void playDeleteParticles(Location deleted) {
    World world = deleted.getWorld();
    BlockData data = Bukkit.createBlockData(Material.REDSTONE_BLOCK);
    assert world != null;
    world.spawnParticle(Particle.BLOCK, deleted, 50, data);
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
