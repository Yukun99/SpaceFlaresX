package me.yukun.spaceflares.config;

import static me.yukun.spaceflares.util.TextFormatter.applyColor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import me.yukun.spaceflares.SpaceFlares;
import me.yukun.spaceflares.config.validator.EnvoyConfigValidator;
import me.yukun.spaceflares.config.validator.ValidationException;
import me.yukun.spaceflares.envoy.Envoy;
import me.yukun.spaceflares.util.Time;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EnvoyConfig {

  private static final Map<String, EnvoyConfig> nameConfigMap = new HashMap<>();
  private static final Random RANDOM = new Random();
  private static final NamespacedKey ENVOY_KEY = new NamespacedKey(SpaceFlares.getPlugin(),
      "Envoy");
  private static final Time TIME = new Time();

  private final String name;
  private FileConfiguration config;
  private final File file;
  private ItemStack flareItem = null;
  private int minDuration;
  private int maxDuration;
  private int minCooldown;
  private int maxCooldown;

  public EnvoyConfig(String name, FileConfiguration config, File file) {
    this.name = name;
    this.config = config;
    this.file = file;
    nameConfigMap.put(this.name, this);
  }

  public static void setup() {
    for (String envoyName : nameConfigMap.keySet()) {
      EnvoyConfig envoy = nameConfigMap.get(envoyName);
      envoy.setupFlareItem();
      envoy.setupEnvoyTimes();
      if (envoy.doCooldownEnable()) {
        Envoy.startEnvoyCooldown(envoyName, envoy.getSavedCooldown());
      }
    }
  }

  private void setupFlareItem() {
    // Create item
    String materialName = config.getString("Item.Type");
    assert materialName != null;
    Material material = Material.getMaterial(materialName);
    assert material != null;
    ItemStack item = new ItemStack(material);

    // Set item metadata
    ItemMeta itemMeta = item.getItemMeta();
    assert itemMeta != null;

    String itemName = config.getString("Item.Name");
    assert itemName != null;
    itemName = itemName.replaceAll("%tier%", getName());

    List<String> itemLore = new ArrayList<>();
    for (String line : config.getStringList("Item.Lore")) {
      itemLore.add(applyColor(line.replaceAll("%tier%", name)));
    }

    // Set item PDC
    PersistentDataContainer container = itemMeta.getPersistentDataContainer();
    container.set(ENVOY_KEY, PersistentDataType.STRING, name);

    // Apply item metadate
    itemMeta.setDisplayName(applyColor(itemName));
    itemMeta.setLore(itemLore);
    item.setItemMeta(itemMeta);

    // Cache flare item so we don't have to build it again.
    this.flareItem = item;
  }

  private void setupEnvoyTimes() {
    minDuration = TIME.getSecsFromList(getDurationMin());
    maxDuration = TIME.getSecsFromList(getDurationMax());
    minCooldown = TIME.getSecsFromList(getCooldownMin());
    maxCooldown = TIME.getSecsFromList(getCooldownMax());
  }

  public static void validate() {
    EnvoyConfigValidator validator = new EnvoyConfigValidator();
    for (EnvoyConfig envoyConfig : nameConfigMap.values()) {
      try {
        validator.validate(envoyConfig.config);
        Messages.printValidationSuccess("envoys/" + envoyConfig.name + ".yml");
      } catch (ValidationException e) {
        Messages.printConfigError(e);
      }
    }
  }

  public static void reload() {
    for (EnvoyConfig envoyConfig : nameConfigMap.values()) {
      envoyConfig.config = YamlConfiguration.loadConfiguration(envoyConfig.file);
    }
  }

  public static String getEnvoyName(String envoy) {
    return nameConfigMap.get(envoy).getName();
  }

  public static boolean isEnvoy(String envoy) {
    return nameConfigMap.containsKey(envoy);
  }

  public static String getEnvoyNamesList() {
    StringBuilder builder = new StringBuilder();
    for (String envoyName : nameConfigMap.keySet()) {
      builder.append("- ").append(envoyName).append("\n");
    }
    return builder.toString();
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public static boolean getEnvoyDoAnnounce(String envoy) {
    return nameConfigMap.get(envoy).getDoAnnounce();
  }

  public static int getEnvoyAnnounceRange(String envoy) {
    return nameConfigMap.get(envoy).getAnnounceRange();
  }

  public static ItemStack getEnvoyFlareItem(String envoy) {
    return nameConfigMap.get(envoy).getFlareItem().clone();
  }

  public static String getEnvoyFlareFromItem(ItemStack item) {
    if (!item.hasItemMeta()) {
      return null;
    }
    ItemMeta itemMeta = item.getItemMeta();
    assert itemMeta != null;
    PersistentDataContainer container = itemMeta.getPersistentDataContainer();
    if (!container.has(ENVOY_KEY, PersistentDataType.STRING)) {
      return null;
    }
    return container.get(ENVOY_KEY, PersistentDataType.STRING);
  }

  public static int getEnvoyDuration(String envoy) {
    return nameConfigMap.get(envoy).getDuration();
  }

  public static boolean doEnvoyCooldownEnable(String envoy) {
    return nameConfigMap.get(envoy).doCooldownEnable();
  }

  public static boolean doEnvoyCooldownReset(String envoy) {
    return nameConfigMap.get(envoy).doCooldownReset();
  }

  public static int getEnvoyCooldown(String envoy) {
    return nameConfigMap.get(envoy).getCooldown();
  }

  public static Map<String, Integer> getEnvoyCrates(String envoy) {
    return nameConfigMap.get(envoy).getCrates();
  }

  public static Location saveEnvoyLocation(String envoy, Player player, Location location) {
    return nameConfigMap.get(envoy).saveEnvoyLocation(player, location);
  }

  public static boolean deleteEnvoyLocation(String envoy, int index) {
    return nameConfigMap.get(envoy).deleteEnvoyLocation(index);
  }

  public static List<Location> getEnvoyLocations(String envoy) {
    return nameConfigMap.get(envoy).getLocations();
  }

  public static List<Location> getEnvoyRandomLocations(String envoy, int count) {
    return nameConfigMap.get(envoy).getRandomLocations(count);
  }

  public static void saveEnvoyCooldown(String envoy, List<Integer> cooldown) {
    FileConfiguration config = nameConfigMap.get(envoy).config;
    String key = "Cooldown.Current.";
    config.set(key + "Day", cooldown.get(0));
    config.set(key + "Hour", cooldown.get(1));
    config.set(key + "Min", cooldown.get(2));
    config.set(key + "Sec", cooldown.get(3));
    try {
      config.save(nameConfigMap.get(envoy).file);
    } catch (IOException e) {
      Messages.printSaveError("envoys/" + envoy + ".yml");
    }
  }

  private String getName() {
    return config.getString("Name");
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

  private int getDuration() {
    if (!doDurationRandom()) {
      return minDuration;
    }
    return RANDOM.nextInt(maxDuration - minDuration + 1) + minDuration;
  }

  private boolean doDurationRandom() {
    return config.getBoolean("Duration.Random");
  }

  private List<Integer> getDurationMin() {
    List<Integer> min = new ArrayList<>();
    min.add(config.getInt("Duration.Min.Day"));
    min.add(config.getInt("Duration.Min.Hour"));
    min.add(config.getInt("Duration.Min.Min"));
    min.add(config.getInt("Duration.Min.Sec"));
    return min;
  }

  private List<Integer> getDurationMax() {
    List<Integer> max = new ArrayList<>();
    max.add(config.getInt("Duration.Max.Day"));
    max.add(config.getInt("Duration.Max.Hour"));
    max.add(config.getInt("Duration.Max.Min"));
    max.add(config.getInt("Duration.Max.Sec"));
    return max;
  }

  private boolean doCooldownEnable() {
    return config.getBoolean("Cooldown.Enable");
  }

  private boolean doCooldownReset() {
    return config.getBoolean("Cooldown.Reset");
  }

  private int getCooldown() {
    if (!doCooldownRandom()) {
      return minCooldown;
    }
    return RANDOM.nextInt(maxCooldown - minCooldown + 1) + minCooldown;
  }

  private int getSavedCooldown() {
    return TIME.getSecsFromList(getSavedCooldownList());
  }

  private List<Integer> getSavedCooldownList() {
    List<Integer> cooldown = new ArrayList<>();
    cooldown.add(config.getInt("Cooldown.Current.Day"));
    cooldown.add(config.getInt("Cooldown.Current.Hour"));
    cooldown.add(config.getInt("Cooldown.Current.Min"));
    cooldown.add(config.getInt("Cooldown.Current.Sec"));
    return cooldown;
  }

  private boolean doCooldownRandom() {
    return config.getBoolean("Cooldown.Random");
  }

  private List<Integer> getCooldownMin() {
    List<Integer> min = new ArrayList<>();
    min.add(config.getInt("Cooldown.Min.Day"));
    min.add(config.getInt("Cooldown.Min.Hour"));
    min.add(config.getInt("Cooldown.Min.Min"));
    min.add(config.getInt("Cooldown.Min.Sec"));
    return min;
  }

  private List<Integer> getCooldownMax() {
    List<Integer> max = new ArrayList<>();
    max.add(config.getInt("Cooldown.Max.Day"));
    max.add(config.getInt("Cooldown.Max.Hour"));
    max.add(config.getInt("Cooldown.Max.Min"));
    max.add(config.getInt("Cooldown.Max.Sec"));
    return max;
  }

  private Map<String, Integer> getCrates() {
    Map<String, Integer> crates = new HashMap<>();
    for (String crate : Objects.requireNonNull(config.getConfigurationSection("Crates"))
        .getKeys(false)) {
      crates.put(crate, getCrateAmount(crate));
    }
    return crates;
  }

  private int getCrateAmount(String crate) {
    if (!doCrateRandom(crate)) {
      return getCrateMin(crate);
    }
    int min = getCrateMin(crate);
    int max = getCrateMax(crate);
    return RANDOM.nextInt(max - min + 1) + min;
  }

  private boolean doCrateRandom(String crate) {
    return config.getBoolean("Crates." + crate + ".Random");
  }

  private int getCrateMin(String crate) {
    return config.getInt("Crates." + crate + ".Min");
  }

  private int getCrateMax(String crate) {
    return config.getInt("Crates." + crate + ".Max");
  }

  private Location saveEnvoyLocation(Player player, Location location) {
    Location offset = getOffsetLocation(location);
    if (offset.getBlock().getType() != Material.AIR) {
      Messages.sendEnvoyEditNotAir(player, location);
      return null;
    }
    List<String> locationList =
        config.isList("Locations") ? config.getStringList("Locations") : new ArrayList<>();
    String world = Objects.requireNonNull(offset.getWorld()).getName();
    int x = offset.getBlockX();
    int y = offset.getBlockY();
    int z = offset.getBlockZ();
    String offsetString = world + "," + x + "," + y + "," + z;
    locationList.add(offsetString);
    config.set("Locations", locationList);
    try {
      config.save(file);
      return offset;
    } catch (IOException e) {
      Messages.printSaveError("envoys/" + name + ".yml");
      Messages.sendEnvoyEditNoSave(player);
      return null;
    }
  }

  private boolean deleteEnvoyLocation(int index) {
    List<String> locationList =
        config.isList("Locations") ? config.getStringList("Locations") : new ArrayList<>();
    if (index >= locationList.size()) {
      return false;
    }
    locationList.remove(index);
    config.set("Locations", locationList);
    try {
      config.save(file);
      return true;
    } catch (IOException e) {
      Messages.printSaveError("envoys/" + name + ".yml");
      return false;
    }
  }

  private Location getOffsetLocation(Location location) {
    int x = config.getInt("Offsets.X");
    int y = config.getInt("Offsets.Y");
    int z = config.getInt("Offsets.Z");
    return new Location(location.getWorld(), location.getX() + x, location.getY() + y,
        location.getZ() + z);
  }

  private List<Location> getRandomLocations(int count) {
    List<Location> locations = getLocations();
    Collections.shuffle(locations);
    return locations.subList(0, count);
  }

  private List<Location> getLocations() {
    List<Location> locations = new ArrayList<>();
    if (!config.isList("Locations")) {
      return locations;
    }
    for (String line : config.getStringList("Locations")) {
      String[] split = line.split("\\s*,\\s*");
      World world = Bukkit.getWorld(split[0]);
      int x = Integer.parseInt(split[1]);
      int y = Integer.parseInt(split[2]);
      int z = Integer.parseInt(split[3]);
      locations.add(new Location(world, x, y, z));
    }
    return locations;
  }
}
