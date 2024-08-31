package me.yukun.spaceflares.config;

import static me.yukun.spaceflares.util.TextFormatter.applyColor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.yukun.spaceflares.config.validator.FlareConfigValidator;
import me.yukun.spaceflares.config.validator.ValidationException;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FlareConfig {

  private static final Map<String, FlareConfig> nameConfigMap = new HashMap<>();

  private final String name;
  private FileConfiguration config;
  private final File file;
  private ItemStack flareItem = null;

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
    for (String flare : nameConfigMap.keySet()) {
      ItemStack flareItem = nameConfigMap.get(flare).getFlareItem();
      if (!flareItem.isSimilar(item)) {
        continue;
      }
      return flare;
    }
    return null;
  }

  private String getTierName() {
    return config.getString("TierName");
  }

  private boolean getDoAnnounce() {
    return config.getBoolean("Announce.Enable");
  }

  private int getAnnounceRange() {
    return config.getInt("Announce.Range");
  }

  private ItemStack getFlareItem() {
    return flareItem;
  }
}
