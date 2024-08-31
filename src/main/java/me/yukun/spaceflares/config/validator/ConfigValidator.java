package me.yukun.spaceflares.config.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.yukun.spaceflares.config.ConfigTypeEnum;
import me.yukun.spaceflares.config.FieldTypeEnum;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigValidator implements IValidator {

  private final List<String> SECTIONS = new ArrayList<>(18) {{
    add("Redeem");
    add("Next");
    add("Previous");
  }};

  private final Map<String, FieldTypeEnum> FIELDS = new HashMap<>(37) {{
    put("Redeem.Size", FieldTypeEnum.INTEGER);
    put("Redeem.Name", FieldTypeEnum.STRING);
    put("Next.Item", FieldTypeEnum.MATERIAL);
    put("Next.Name", FieldTypeEnum.STRING);
    put("Next.Lore", FieldTypeEnum.STRINGLIST);
    put("Previous.Item", FieldTypeEnum.MATERIAL);
    put("Previous.Name", FieldTypeEnum.STRING);
    put("Previous.Lore", FieldTypeEnum.STRINGLIST);
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

  @Override
  public void validateStringField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.STRING, field));
    }
  }

  @Override
  public void validateIntegerField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isInt(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.INTEGER, field));
    }
  }

  @Override
  public void validateBooleanField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isBoolean(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.BOOLEAN, field));
    }
  }

  @Override
  public void validateStringListField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isList(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.STRINGLIST,
              field));
    }
  }

  @Override
  public void validateMaterialField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.MATERIAL,
              field));
    }
    String materialName = config.getString(field);
    assert materialName != null;
    if (Material.getMaterial(materialName) == null) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.MATERIAL,
              field));
    }
  }
}
