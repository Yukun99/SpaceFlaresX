package me.yukun.spaceflares.config;

import static me.yukun.spaceflares.util.TextFormatter.applyColor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import me.yukun.spaceflares.config.validator.CrateConfigValidator;
import me.yukun.spaceflares.config.validator.ValidationException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CrateConfig {

  private static final Map<String, CrateConfig> nameConfigMap = new HashMap<>();
  private static final Random random = new Random();

  private final String name;
  private FileConfiguration config;
  private final File file;
  private final Map<ItemStack, Integer> itemRewardChanceMap = new HashMap<>();
  private final Map<List<String>, Integer> commandRewardChanceMap = new HashMap<>();

  public CrateConfig(String name, FileConfiguration config, File file) {
    this.name = name;
    this.config = config;
    this.file = file;
    nameConfigMap.put(this.name, this);
  }

  public static void setup() {
    for (String crate : nameConfigMap.keySet()) {
      nameConfigMap.get(crate).setupRewards();
    }
  }

  private void setupRewards() {
    itemRewardChanceMap.clear();
    commandRewardChanceMap.clear();
    for (String name : Objects.requireNonNull(config.getConfigurationSection("Rewards"))
        .getKeys(false)) {
      String type = config.getString("Rewards." + name + ".Type");
      assert type != null;
      if (type.equals("ITEM")) {
        setupItemReward(name);
      }
      if (type.equals("COMMAND")) {
        setupCommandReward(name);
      }
    }
  }

  private void setupItemReward(String name) {
    String materialName = config.getString("Rewards." + name + ".Item");
    assert materialName != null;
    Material material = Material.getMaterial(materialName);
    int amount = config.getInt("Rewards." + name + ".Amount");
    assert material != null;
    ItemStack reward = new ItemStack(material, amount);
    ItemMeta itemMeta = reward.getItemMeta();
    String itemName = applyColor(config.getString("Rewards." + name + ".Name"));
    assert itemMeta != null;
    itemMeta.setDisplayName(itemName);
    List<String> itemLore = new ArrayList<>();
    for (String line : config.getStringList("Rewards." + name + ".Lore")) {
      itemLore.add(applyColor(line));
    }
    itemMeta.setLore(itemLore);
    reward.setItemMeta(itemMeta);
    int chance = config.getInt("Rewards." + name + ".Chance");
    itemRewardChanceMap.put(reward, chance);
  }

  private void setupCommandReward(String name) {
    List<String> reward = config.getStringList("Rewards." + name + ".Commands");
    int chance = config.getInt("Rewards." + name + ".Chance");
    commandRewardChanceMap.put(reward, chance);
  }

  public static void validate() {
    CrateConfigValidator validator = new CrateConfigValidator();
    for (CrateConfig crateConfig : nameConfigMap.values()) {
      try {
        validator.validate(crateConfig.config);
        Messages.printValidationSuccess("crates/" + crateConfig.name + ".yml");
      } catch (ValidationException e) {
        Messages.printConfigError(e);
      }
    }
  }

  public static void reload() {
    for (CrateConfig crateConfig : nameConfigMap.values()) {
      crateConfig.config = YamlConfiguration.loadConfiguration(crateConfig.file);
    }
  }

  public static BlockData getCrateBlock(String crate) {
    return nameConfigMap.get(crate).getBlock();
  }

  public static boolean getCrateDoFastDrop(String crate) {
    return nameConfigMap.get(crate).doFastDrop();
  }

  public static boolean getCrateDoFastExit(String crate) {
    return nameConfigMap.get(crate).doFastExit();
  }

  public static boolean getCrateDoFastSneak(String crate) {
    return nameConfigMap.get(crate).doFastSneak();
  }

  public static boolean getCrateDoFastPunch(String crate) {
    return nameConfigMap.get(crate).doFastPunch();
  }

  public static boolean getCrateDoFastUse(String crate) {
    return nameConfigMap.get(crate).doFastUse();
  }

  public static int getCrateDespawnTime(String crate) {
    return nameConfigMap.get(crate).getDespawnTime();
  }

  public static boolean getCrateDoDespawnGive(String crate) {
    return nameConfigMap.get(crate).getDoDespawnGive();
  }

  public static List<ItemStack> getCrateItemRewards(String crate) {
    return nameConfigMap.get(crate).getItemRewards();
  }

  public static List<String> getCrateCommandRewards(String crate) {
    return nameConfigMap.get(crate).getCommandRewards();
  }

  private BlockData getBlock() {
    String materialName = config.getString("Block");
    assert materialName != null;
    Material material = Material.getMaterial(materialName);
    assert material != null;
    return Bukkit.createBlockData(material);
  }

  private boolean doFastDrop() {
    return config.getBoolean("FastClaim.Drop");
  }

  private boolean doFastExit() {
    return config.getBoolean("FastClaim.Exit");
  }

  private boolean doFastSneak() {
    return config.getBoolean("FastClaim.Sneak");
  }

  private boolean doFastPunch() {
    return config.getBoolean("FastClaim.Punch");
  }

  private boolean doFastUse() {
    return config.getBoolean("FastClaim.Use");
  }

  private boolean doDespawn() {
    return config.getBoolean("Despawn.Enable");
  }

  private int getDespawnTime() {
    return doDespawn() ? config.getInt("Despawn.Time") : 0;
  }

  private boolean getDoDespawnGive() {
    return config.getBoolean("Despawn.Give");
  }

  private List<ItemStack> getItemRewards() {
    List<ItemStack> rewards = new ArrayList<>();
    for (ItemStack reward : itemRewardChanceMap.keySet()) {
      int chance = itemRewardChanceMap.get(reward) - 1;
      int roll = random.nextInt(100);
      if (roll > chance) {
        rewards.add(reward);
      }
    }
    return rewards;
  }

  private List<String> getCommandRewards() {
    List<String> rewards = new ArrayList<>();
    for (List<String> reward : commandRewardChanceMap.keySet()) {
      int chance = commandRewardChanceMap.get(reward) - 1;
      int roll = random.nextInt(100);
      if (roll > chance) {
        rewards.addAll(reward);
      }
    }
    return rewards;
  }
}
