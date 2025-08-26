package me.yukun.spaceflares.config;

import me.yukun.spaceflares.SpaceFlares;
import me.yukun.spaceflares.config.validator.FlareConfigValidator;
import me.yukun.spaceflares.config.validator.ValidationException;
import me.yukun.spaceflares.util.Fireworks;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.*;

import static me.yukun.spaceflares.util.TextFormatter.applyColor;

public class FlareConfig {

  private static final Map<String, FlareConfig> nameConfigMap = new HashMap<>();
  private static final Random random = new Random();
  private static final NamespacedKey flareKey = new NamespacedKey(SpaceFlares.getPlugin(), "Flare");

  private final String name;
  private FileConfiguration config;
  private final File file;
  private ItemStack flareItem = null;
  private final List<Color> colors = new ArrayList<>();

  public FlareConfig(String name, FileConfiguration config, File file) {
    this.name = name;
    this.config = config;
    this.file = file;
    nameConfigMap.put(this.name, this);
  }

  /**
   * Creates flare items and chaches them for future access.
   */
  protected static void setup() {
    for (FlareConfig flareConfig : nameConfigMap.values()) {
      flareConfig.setupFlareItem();
    }
  }

  /**
   * Creates flare item and caches it for future access.
   */
  private void setupFlareItem() {
    // Create item
    String materialName = config.getString("Item");
    assert materialName != null;
    Material material = Material.getMaterial(materialName);
    assert material != null;
    ItemStack item = new ItemStack(material);

    // Set item metadata
    ItemMeta itemMeta = item.getItemMeta();
    assert itemMeta != null;

    String itemName = config.getString("Name");
    assert itemName != null;
    itemName = itemName.replace("%tier%", getTierName());

    List<String> itemLore = new ArrayList<>();
    for (String line : config.getStringList("Lore")) {
      itemLore.add(applyColor(line.replace("%tier%", name)));
    }

    // Set item PDC
    PersistentDataContainer container = itemMeta.getPersistentDataContainer();
    container.set(flareKey, PersistentDataType.STRING, name);

    // Apply item metadata
    itemMeta.setDisplayName(applyColor(itemName));
    itemMeta.setLore(itemLore);
    item.setItemMeta(itemMeta);

    // Cache flare item so we don't have to build it again.
    this.flareItem = item;
  }

  /**
   * Validates all flare configs.
   */
  public static void validate() {
    FlareConfigValidator validator = new FlareConfigValidator();
    for (FlareConfig flareConfig : nameConfigMap.values()) {
      try {
        validator.validate(flareConfig.config);
        Messages.printValidationSuccess("flares/" + flareConfig.name + ".yml");
      } catch (ValidationException e) {
        Messages.printConfigError(e);
      }
    }
  }

  /**
   * Clears all cached flare configs.
   */
  public static void reloadClear() {
    nameConfigMap.clear();
  }

  /**
   * Reloads all flare configs.
   */
  public static void reload() {
    for (FlareConfig flareConfig : nameConfigMap.values()) {
      flareConfig.config = YamlConfiguration.loadConfiguration(flareConfig.file);
    }
  }

  /**
   * Checks if specified flare exists.
   * @param name Name of flare to check.
   * @return True if flare exists, false otherwise.
   */
  public static boolean isFlare(String name) {
    return nameConfigMap.containsKey(name);
  }

  /**
   * Gets tier name of specified flare.
   * @param flare Flare to get tier name for.
   * @return Tier name of specified flare.
   */
  public static String getFlareTier(String flare) {
    return nameConfigMap.get(flare).getTierName();
  }

  /**
   * Gets whether specified flare should announce when it spawns.
   * @param flare Flare to check.
   * @return True if flare should announce, false otherwise.
   */
  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public static boolean getFlareDoAnnounce(String flare) {
    return nameConfigMap.get(flare).getDoAnnounce();
  }

  /**
   * Gets the range in which specified flare should announce.
   * @param flare Flare to get announce range for.
   * @return Announce range of specified flare.
   */
  public static int getFlareAnnounceRange(String flare) {
    return nameConfigMap.get(flare).getAnnounceRange();
  }

  /**
   * Gets flare item for specified flare type.
   * @param flare Flare to get item for.
   * @return Flare item of specified flare.
   */
  public static ItemStack getFlareItem(String flare) {
    return nameConfigMap.get(flare).getFlareItem().clone();
  }

  /**
   * Gets flare type from specified flare item.
   * @param item Flare item to get type for.
   * @return Flare type of specified item.
   */
  public static String getFlareFromItem(ItemStack item) {
    if (!item.hasItemMeta()) {
      return null;
    }
    ItemMeta itemMeta = item.getItemMeta();
    assert itemMeta != null;
    PersistentDataContainer container = itemMeta.getPersistentDataContainer();
    if (!container.has(flareKey)) {
      return null;
    }
    return container.get(flareKey, PersistentDataType.STRING);
  }

  /**
   * Gets spawn location of specified flare type for specified player.
   * @param flare Flare type to get spawn location for.
   * @param player Player who summoned flare of specified type.
   * @return Location where flare should be spawned.
   */
  public static Location getFlareSpawnLocation(String flare, Player player) {
    return nameConfigMap.get(flare).getSpawnLocation(player);
  }

  /**
   * Gets random radius for specified flare type.
   * @param flare Flare type to get random radius for.
   * @return Random radius of specified flare.
   */
  public static int getFlareRandomRadius(String flare) {
    return nameConfigMap.get(flare).getRandomRadius();
  }

  /**
   * Gets fall height for specified flare type.
   * @param flare Flare type to get fall height for.
   * @return Fall height of specified flare.
   */
  public static int getFlareFallHeight(String flare) {
    return nameConfigMap.get(flare).getFallHeight();
  }

  /**
   * Gets list of firework colours of specified flare type.
   * @param flare Flare type to get list of firework colours for.
   * @return List of firework colours of specified flare type.
   */
  public static List<Color> getFlareFireworkColors(String flare) {
    return nameConfigMap.get(flare).getFireworkColors();
  }

  /**
   * Gets firework type of specified flare type.
   * @param flare Flare type to get firework type for.
   * @return Firework type of specified flare type.
   */
  public static Type getFlareFireworkType(String flare) {
    return nameConfigMap.get(flare).getFireworkType();
  }

  /**
   * Gets list of WorldGuard regions for specified flare type.
   * @param flare Flare type to get list of WorldGuard regions for.
   * @return List of WorldGuard regions for specified flare type.
   */
  public static List<String> getFlareRegionWGList(String flare) {
    return nameConfigMap.get(flare).getRegionWGList();
  }

  /**
   * Gets whether specified flare type should use WorldGuard PvP flag.
   * @param flare Flare type to check.
   * @return True if flare should use WorldGuard PvP flag, false otherwise.
   */
  public static boolean getFlareRegionWGDoPvPFlag(String flare) {
    return nameConfigMap.get(flare).getRegionWGDoPvPFlag();
  }

  /**
   * Gets whether specified flare type should use WorldGuard NoBuild flag.
   * @param flare Flare type to check.
   * @return True if flare should use WorldGuard NoBuild flag, false otherwise.
   */
  public static boolean getFlareRegionWGDoNoBuild(String flare) {
    return nameConfigMap.get(flare).getRegionWGDoNoBuild();
  }

  /**
   * Gets whether specified flare type should use Warzone.
   * @param flare Flare type to check.
   * @return True if flare should use Warzone, false otherwise.
   */
  public static boolean getFlareRegionUseWarzone(String flare) {
    return nameConfigMap.get(flare).getRegionUseWarzone();
  }

  /**
   * Gets whether specified flare type should use Skyblock.
   * @param flare Flare type to check.
   * @return True if flare should use Skyblock, false otherwise.
   */
  public static boolean getFlareRegionUseSkyblock(String flare) {
    return nameConfigMap.get(flare).getRegionUseSkyblock();
  }

  private String getTierName() {
    return config.getString("TierName");
  }

  private boolean getDoAnnounce() {
    return config.getBoolean("Announce.Enable");
  }

  private int getAnnounceRange() {
    return config.getInt("Announce.Radius");
  }

  private ItemStack getFlareItem() {
    return flareItem;
  }

  private Location getSpawnLocation(Player player) {
    int radius = getRandomRadius();
    int height = getFallHeight();
    Location location = player.getLocation().clone();
    if (height == 0) {
      location = getGroundedLocation(location);
    } else {
      location.setY(location.getY() + height);
    }
    if (radius == 0) {
      return location;
    }
    int xDist = random.nextInt(radius + 1);
    int zDist = random.nextInt(radius + 1);
    xDist = random.nextBoolean() ? xDist : -xDist;
    zDist = random.nextBoolean() ? zDist : -zDist;
    location.setX(location.getX() + xDist);
    location.setZ(location.getZ() + zDist);
    return location;
  }

  private Location getGroundedLocation(Location location) {
    int y = location.getBlockY();
    Location block = location.clone();
    while (block.getBlock().getType() == Material.AIR) {
      block.setY(y);
      y--;
    }
    return block;
  }

  private int getRandomRadius() {
    return config.getBoolean("Random.Enable") ? config.getInt("Random.Radius") : 0;
  }

  private int getFallHeight() {
    return config.getBoolean("Fall.Enable") ? config.getInt("Fall.Height") : 0;
  }

  private List<Color> getFireworkColors() {
    if (!colors.isEmpty()) {
      return colors;
    }
    List<Color> configColors = new ArrayList<>();
    for (String configColorName : config.getStringList("Firework.Colors")) {
      configColors.add(Fireworks.getColor(configColorName));
    }
    this.colors.addAll(configColors);
    return configColors;
  }

  private Type getFireworkType() {
    return Type.valueOf(config.getString("Firework.Type"));
  }

  private boolean useRegionWGList() {
    return config.getBoolean("Region.WorldGuard.Enable");
  }

  private List<String> getRegionWGList() {
    if (!useRegionWGList()) {
      return new ArrayList<>();
    }
    return config.getStringList("Region.WorldGuard.List");
  }

  private boolean getRegionWGDoPvPFlag() {
    return config.getBoolean("Region.WorldGuard.PvPFlag");
  }

  private boolean getRegionWGDoNoBuild() {
    return config.getBoolean("Region.WorldGuard.NoBuild");
  }

  private boolean getRegionUseWarzone() {
    return config.getBoolean("Region.Warzone");
  }

  private boolean getRegionUseSkyblock() {
    return config.getBoolean("Region.SuperiorSkyblock.Enable");
  }
}
