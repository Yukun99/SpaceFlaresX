package me.yukun.spaceflares.config.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.yukun.spaceflares.config.ConfigTypeEnum;
import me.yukun.spaceflares.config.FieldTypeEnum;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class FlareConfigValidator implements IValidator {

  private final List<String> SECTIONS = new ArrayList<>(4) {{
    add("Announce");
    add("Fall");
    add("Random");
  }};

  private final Map<String, FieldTypeEnum> FIELDS = new HashMap<>(13) {{
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
  }};

  private final Map<String, FieldTypeEnum> TIER_PLACEHOLDER_FIELDS = new HashMap<>(2) {{
    put("Name", FieldTypeEnum.STRING);
    put("Lore", FieldTypeEnum.STRINGLIST);
  }};

  @Override
  public void validate(FileConfiguration config) throws ValidationException {
    validateSections(config);
    validateFields(config);
  }

  private void validateSections(FileConfiguration config) throws ValidationException {
    for (String section : SECTIONS) {
      if (!config.isConfigurationSection(section)) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.SECTION,
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

  public void validateStringField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.REDEEMS, FieldTypeEnum.STRING, field));
    }
    if (TIER_PLACEHOLDER_FIELDS.containsKey(field)) {
      return;
    }
    String placeholder = "%tier%";
    if (Objects.requireNonNull(config.getString(field)).contains(placeholder)) {
      throw new ValidationException(
          ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.REDEEMS,
              FieldTypeEnum.STRING,
              field, placeholder));
    }
  }

  @Override
  public void validateIntegerField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isInt(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.REDEEMS, FieldTypeEnum.INTEGER,
              field));
    }
  }

  @Override
  public void validateBooleanField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isBoolean(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.REDEEMS, FieldTypeEnum.BOOLEAN,
              field));
    }
  }

  @Override
  public void validateStringListField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isList(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.REDEEMS, FieldTypeEnum.STRINGLIST,
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
            ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.REDEEMS,
                FieldTypeEnum.STRINGLIST,
                field, placeholder));
      }
    }
  }

  @Override
  public void validateMaterialField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.REDEEMS, FieldTypeEnum.MATERIAL,
              field));
    }
    String materialName = config.getString(field);
    assert materialName != null;
    if (Material.getMaterial(materialName) == null) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.REDEEMS, FieldTypeEnum.MATERIAL,
              field));
    }
  }
}
