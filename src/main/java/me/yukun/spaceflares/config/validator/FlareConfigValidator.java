package me.yukun.spaceflares.config.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.yukun.spaceflares.config.ConfigTypeEnum;
import me.yukun.spaceflares.config.FieldTypeEnum;
import me.yukun.spaceflares.integration.SupportManager;
import me.yukun.spaceflares.util.Fireworks;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class FlareConfigValidator implements IValidator {

  private final List<String> SECTIONS = new ArrayList<>(6) {{
    add("Announce");
    add("Fall");
    add("Random");
    add("Firework");
    add("Region");
    add("Region.WorldGuard");
  }};

  private final Map<String, FieldTypeEnum> FIELDS = new HashMap<>(16) {{
    put("TierName", FieldTypeEnum.STRING);
    put("Announce.Enable", FieldTypeEnum.BOOLEAN);
    put("Announce.Radius", FieldTypeEnum.INTEGER);
    put("Fall.Enable", FieldTypeEnum.BOOLEAN);
    put("Fall.Height", FieldTypeEnum.INTEGER);
    put("Random.Enable", FieldTypeEnum.BOOLEAN);
    put("Random.Radius", FieldTypeEnum.INTEGER);
    put("Item", FieldTypeEnum.MATERIAL);
    put("Name", FieldTypeEnum.STRING);
    put("Lore", FieldTypeEnum.STRINGLIST);
    put("Region.WorldGuard.Enable", FieldTypeEnum.BOOLEAN);
    put("Region.WorldGuard.List", FieldTypeEnum.REGION);
    put("Region.WorldGuard.PvPFlag", FieldTypeEnum.BOOLEAN);
    put("Region.WorldGuard.NoBuild", FieldTypeEnum.BOOLEAN);
    put("Region.Warzone", FieldTypeEnum.BOOLEAN);
    put("Firework.Type", FieldTypeEnum.FIREWORK);
    put("Firework.Colors", FieldTypeEnum.COLOR);
  }};

  private final Map<String, FieldTypeEnum> TIER_PLACEHOLDER_FIELDS = new HashMap<>(2) {{
    put("Name", FieldTypeEnum.STRING);
    put("Lore", FieldTypeEnum.STRINGLIST);
  }};

  public void validate(FileConfiguration config) throws ValidationException {
    validateSections(config);
    validateFields(config);
  }

  private void validateSections(FileConfiguration config) throws ValidationException {
    for (String section : SECTIONS) {
      if (!config.isConfigurationSection(section)) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.SECTION,
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

  @Override
  public void validateStringField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.STRING, field));
    }
    if (TIER_PLACEHOLDER_FIELDS.containsKey(field)) {
      return;
    }
    String placeholder = "%tier%";
    if (Objects.requireNonNull(config.getString(field)).contains(placeholder)) {
      throw new ValidationException(
          ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.FLARES,
              FieldTypeEnum.STRING, field, placeholder));
    }
  }

  @Override
  public void validateIntegerField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isInt(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.INTEGER,
              field));
    }
  }

  @Override
  public void validateBooleanField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isBoolean(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.BOOLEAN,
              field));
    }
  }

  @Override
  public void validateStringListField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isList(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.STRINGLIST,
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
            ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.FLARES,
                FieldTypeEnum.STRINGLIST, field, placeholder));
      }
    }
  }

  @Override
  public void validateMaterialField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.MATERIAL,
              field));
    }
    String materialName = config.getString(field);
    assert materialName != null;
    if (Material.getMaterial(materialName) == null) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.MATERIAL,
              field));
    }
  }

  @Override
  public void validateFireworkField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.FIREWORK,
              field));
    }
    String fireworkType = config.getString(field);
    assert fireworkType != null;
    if (Fireworks.getType(fireworkType) == null) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.FIREWORK,
              field));
    }
  }

  @Override
  public void validateColorField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isList(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.COLOR,
              field));
    }
    for (String colorName : config.getStringList(field)) {
      if (Fireworks.getColor(colorName) == null) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.COLOR,
                field));
      }
    }
  }

  @Override
  public void validateRegionField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isList(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.REGION,
              field));
    }
    for (String region : config.getStringList(field)) {
      if (!SupportManager.isRegion(region)) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.FLARES, FieldTypeEnum.REGION,
                field));
      }
    }
  }
}
