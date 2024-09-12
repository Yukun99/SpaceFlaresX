package me.yukun.spaceflares.config;

import static me.yukun.spaceflares.util.TextFormatter.applyColor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

  protected static void setup() {
    for (FlareConfig flareConfig : nameConfigMap.values()) {
      flareConfig.setupFlareItem();
    }
  }

  private void setupFlareItem() {
    // At this point nothing should be null, but IntelliJ still thinks they can be /shrugs
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
    itemName = itemName.replaceAll("%tier%", getTierName());

    List<String> itemLore = new ArrayList<>();
    for (String line : config.getStringList("Lore")) {
      itemLore.add(applyColor(line.replaceAll("%tier%", name)));
    }

    // Set item PDC
    PersistentDataContainer container = itemMeta.getPersistentDataContainer();
    container.set(flareKey, PersistentDataType.STRING, name);

    // Apply item metadate
    itemMeta.setDisplayName(applyColor(itemName));
    itemMeta.setLore(itemLore);
    item.setItemMeta(itemMeta);

    // Cache flare item so we don't have to build it again.
    this.flareItem = item;
  }

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

  public static void reload() {
    for (FlareConfig flareConfig : nameConfigMap.values()) {
      flareConfig.config = YamlConfiguration.loadConfiguration(flareConfig.file);
    }
  }

  public static boolean isFlare(String name) {
    return nameConfigMap.containsKey(name);
  }

  public static String getFlareTier(String flare) {
    return nameConfigMap.get(flare).getTierName();
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public static boolean getFlareDoAnnounce(String flare) {
    return nameConfigMap.get(flare).getDoAnnounce();
  }

  public static int getFlareAnnounceRange(String flare) {
    return nameConfigMap.get(flare).getAnnounceRange();
  }

  public static ItemStack getFlareItem(String flare) {
    return nameConfigMap.get(flare).getFlareItem().clone();
  }

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
   *
   * @param flare  Flare type to get spawn location for.
   * @param player Player who summoned flare of specified type.
   * @return Location where flare should be spawned.
   */
  public static Location getFlareSpawnLocation(String flare, Player player) {
    return nameConfigMap.get(flare).getSpawnLocation(player);
  }

  public static int getFlareRandomRadius(String flare) {
    return nameConfigMap.get(flare).getRandomRadius();
  }

  public static int getFlareFallHeight(String flare) {
    return nameConfigMap.get(flare).getFallHeight();
  }

  /**
   * Gets list of firework colours of specified flare type.
   *
   * @param flare Flare type to get list of firework colours for.
   * @return List of firework colours of specified flare type.
   */
  public static List<Color> getFlareFireworkColors(String flare) {
    return nameConfigMap.get(flare).getFireworkColors();
  }

  /**
   * Gets firework type of specified flare type.
   *
   * @param flare Flare type to get firework type for.
   * @return Firework type of specified flare type.
   */
  public static Type getFlareFireworkType(String flare) {
    return nameConfigMap.get(flare).getFireworkType();
  }

  public static List<String> getFlareRegionWGList(String flare) {
    return nameConfigMap.get(flare).getRegionWGList();
  }

  public static boolean getFlareRegionWGDoPvPFlag(String flare) {
    return nameConfigMap.get(flare).getRegionWGDoPvPFlag();
  }

  public static boolean getFlareRegionWGDoNoBuild(String flare) {
    return nameConfigMap.get(flare).getRegionWGDoNoBuild();
  }

  public static boolean getFlareRegionUseWarzone(String flare) {
    return nameConfigMap.get(flare).getRegionUseWarzone();
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
    List<Color> colors = new ArrayList<>();
    for (String name : config.getStringList("Firework.Colors")) {
      colors.add(Fireworks.getColor(name));
    }
    this.colors.addAll(colors);
    return colors;
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
}
