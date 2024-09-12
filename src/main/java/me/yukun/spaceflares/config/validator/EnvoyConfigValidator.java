package me.yukun.spaceflares.config.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.yukun.spaceflares.config.ConfigTypeEnum;
import me.yukun.spaceflares.config.FieldTypeEnum;
import me.yukun.spaceflares.config.FlareConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class EnvoyConfigValidator implements IValidator {

  private final List<String> SECTIONS = new ArrayList<>(11) {{
    add("Announce");
    add("Item");
    add("Duration");
    add("Duration.Min");
    add("Duration.Max");
    add("Cooldown");
    add("Cooldown.Min");
    add("Cooldown.Max");
    add("Cooldown.Current");
    add("Crates");
    add("Offsets");
  }};

  private final Map<String, FieldTypeEnum> FIELDS = new HashMap<>(33) {{
    put("Name", FieldTypeEnum.STRING);
    put("Announce.Enable", FieldTypeEnum.BOOLEAN);
    put("Announce.Radius", FieldTypeEnum.INTEGER);
    put("Item.Type", FieldTypeEnum.MATERIAL);
    put("Item.Name", FieldTypeEnum.STRING);
    put("Item.Lore", FieldTypeEnum.STRINGLIST);
    put("Duration.Random", FieldTypeEnum.BOOLEAN);
    put("Duration.Min.Day", FieldTypeEnum.INTEGER);
    put("Duration.Min.Hour", FieldTypeEnum.INTEGER);
    put("Duration.Min.Min", FieldTypeEnum.INTEGER);
    put("Duration.Min.Sec", FieldTypeEnum.INTEGER);
    put("Duration.Max.Day", FieldTypeEnum.INTEGER);
    put("Duration.Max.Hour", FieldTypeEnum.INTEGER);
    put("Duration.Max.Min", FieldTypeEnum.INTEGER);
    put("Duration.Max.Sec", FieldTypeEnum.INTEGER);
    put("Cooldown.Enable", FieldTypeEnum.BOOLEAN);
    put("Cooldown.Reset", FieldTypeEnum.BOOLEAN);
    put("Cooldown.Random", FieldTypeEnum.BOOLEAN);
    put("Cooldown.Min.Day", FieldTypeEnum.INTEGER);
    put("Cooldown.Min.Hour", FieldTypeEnum.INTEGER);
    put("Cooldown.Min.Min", FieldTypeEnum.INTEGER);
    put("Cooldown.Min.Sec", FieldTypeEnum.INTEGER);
    put("Cooldown.Max.Day", FieldTypeEnum.INTEGER);
    put("Cooldown.Max.Hour", FieldTypeEnum.INTEGER);
    put("Cooldown.Max.Min", FieldTypeEnum.INTEGER);
    put("Cooldown.Max.Sec", FieldTypeEnum.INTEGER);
    put("Cooldown.Current.Day", FieldTypeEnum.INTEGER);
    put("Cooldown.Current.Hour", FieldTypeEnum.INTEGER);
    put("Cooldown.Current.Min", FieldTypeEnum.INTEGER);
    put("Cooldown.Current.Sec", FieldTypeEnum.INTEGER);
    put("Offsets.X", FieldTypeEnum.INTEGER);
    put("Offsets.Y", FieldTypeEnum.INTEGER);
    put("Offsets.Z", FieldTypeEnum.INTEGER);
  }};

  private final Map<String, FieldTypeEnum> CRATE_FIELDS = new HashMap<>(3) {{
    put("Random", FieldTypeEnum.BOOLEAN);
    put("Min", FieldTypeEnum.INTEGER);
    put("Max", FieldTypeEnum.INTEGER);
  }};

  private final Map<String, FieldTypeEnum> TIER_PLACEHOLDER_FIELDS = new HashMap<>(2) {{
    put("Item.Name", FieldTypeEnum.STRING);
    put("Item.Lore", FieldTypeEnum.STRINGLIST);
  }};

  public void validate(FileConfiguration config) throws ValidationException {
    validateSections(config);
    validateFields(config);
    validateCrates(config);
    validateLocations(config);
  }

  private void validateSections(FileConfiguration config) throws ValidationException {
    for (String section : SECTIONS) {
      if (!config.isConfigurationSection(section)) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.ENVOYS, FieldTypeEnum.SECTION,
                section));
      }
    }
  }

  private void validateFields(FileConfiguration config) throws ValidationException {
    for (String field : FIELDS.keySet()) {
      FieldTypeEnum fieldType = FIELDS.get(field);
      validateField(fieldType, config, field);
    }
  }

  private void validateCrates(FileConfiguration config) throws ValidationException {
    for (String crate : Objects.requireNonNull(config.getConfigurationSection("Crates"))
        .getKeys(false)) {
      if (!FlareConfig.isFlare(crate)) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.ENVOYS, FieldTypeEnum.CRATE,
                crate));
      }
      String crateKey = "Crates." + crate;
      validateCrateField(config, crateKey);
    }
  }

  private void validateLocations(FileConfiguration config) throws ValidationException {
    String errorMessage = ValidationException.getErrorMessage(ConfigTypeEnum.ENVOYS,
        FieldTypeEnum.STRINGLIST,
        "Locations");
    if (!config.isList("Locations")) {
      return;
    }
    for (String line : config.getStringList("Locations")) {
      String[] split = line.split("\\s*,\\s*");
      if (split.length != 4) {
        throw new ValidationException(errorMessage);
      }
      if (Bukkit.getWorld(split[0]) == null) {
        throw new ValidationException(errorMessage);
      }
      if (!isInt(split[1]) || !isInt(split[2]) || !isInt(split[3])) {
        throw new ValidationException(errorMessage);
      }
    }
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private static boolean isInt(String string) {
    try {
      Integer.parseInt(string);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  @Override
  public void validateCrateField(FileConfiguration config, String crateKey)
      throws ValidationException {
    for (String field : CRATE_FIELDS.keySet()) {
      String fieldKey = crateKey + "." + field;
      validateField(CRATE_FIELDS.get(field), config, fieldKey);
    }
    int min = config.getInt(crateKey + ".Min");
    int max = config.getInt(crateKey + ".Max");
    if (min > max) {
      throw new ValidationException(
          ValidationException.getMaxMinErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.REWARD));
    }
  }

  @Override
  public void validateStringField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.ENVOYS, FieldTypeEnum.STRING,
              field));
    }
    if (TIER_PLACEHOLDER_FIELDS.containsKey(field)) {
      return;
    }
    String placeholder = "%tier%";
    if (Objects.requireNonNull(config.getString(field)).contains(placeholder)) {
      throw new ValidationException(
          ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.ENVOYS,
              FieldTypeEnum.STRING, field, placeholder));
    }
  }

  @Override
  public void validateIntegerField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isInt(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.ENVOYS, FieldTypeEnum.INTEGER,
              field));
    }
  }

  @Override
  public void validateBooleanField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isBoolean(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.ENVOYS, FieldTypeEnum.BOOLEAN,
              field));
    }
  }

  @Override
  public void validateStringListField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isList(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.ENVOYS, FieldTypeEnum.STRINGLIST,
              field));
    }
    if (TIER_PLACEHOLDER_FIELDS.containsKey(field)) {
      return;
    }
    List<String> fieldList = config.getStringList(field);
    String placeholder = "%tier%";
    for (String line : fieldList) {
      if (line.contains(placeholder)) {
        throw new ValidationException(
            ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.ENVOYS,
                FieldTypeEnum.STRINGLIST, field, placeholder));
      }
    }
  }

  @Override
  public void validateMaterialField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.ENVOYS, FieldTypeEnum.MATERIAL,
              field));
    }
    String materialName = config.getString(field);
    assert materialName != null;
    if (Material.getMaterial(materialName) == null) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.ENVOYS, FieldTypeEnum.MATERIAL,
              field));
    }
  }

  @Override
  public void validateWorldField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.ENVOYS, FieldTypeEnum.WORLD,
              field));
    }
    String worldName = config.getString(field);
    assert worldName != null;
    if (Bukkit.getWorld(worldName) == null) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.ENVOYS, FieldTypeEnum.WORLD,
              field));
    }
  }
}
