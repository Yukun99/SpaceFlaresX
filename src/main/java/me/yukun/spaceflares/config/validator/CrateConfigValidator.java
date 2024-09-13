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

public class CrateConfigValidator implements IValidator {

  private final List<String> SECTIONS = new ArrayList<>(6) {{
    add("FastClaim");
    add("Despawn");
    add("Rewards");
    add("Hologram");
    add("Hologram.Flare");
    add("Hologram.Envoy");
  }};

  private final Map<String, FieldTypeEnum> FIELDS = new HashMap<>(14) {{
    put("Block", FieldTypeEnum.MATERIAL);
    put("FastClaim.Drop", FieldTypeEnum.BOOLEAN);
    put("FastClaim.Exit", FieldTypeEnum.BOOLEAN);
    put("FastClaim.Sneak", FieldTypeEnum.BOOLEAN);
    put("FastClaim.Punch", FieldTypeEnum.BOOLEAN);
    put("FastClaim.Use", FieldTypeEnum.BOOLEAN);
    put("Despawn.Enable", FieldTypeEnum.BOOLEAN);
    put("Despawn.Time", FieldTypeEnum.INTEGER);
    put("Despawn.Give", FieldTypeEnum.BOOLEAN);
    put("Size", FieldTypeEnum.INTEGER);
    put("Hologram.Flare.Enable", FieldTypeEnum.BOOLEAN);
    put("Hologram.Flare.Label", FieldTypeEnum.STRINGLIST);
    put("Hologram.Envoy.Enable", FieldTypeEnum.BOOLEAN);
    put("Hologram.Envoy.Label", FieldTypeEnum.STRINGLIST);
  }};

  private final Map<String, FieldTypeEnum> REWARD_ITEM_FIELDS = new HashMap<>(4) {{
    put("Amount", FieldTypeEnum.INTEGER);
    put("Item", FieldTypeEnum.MATERIAL);
    put("Name", FieldTypeEnum.STRING);
    put("Lore", FieldTypeEnum.STRINGLIST);
  }};

  private final Map<String, FieldTypeEnum> REWARD_COMMAND_FIELDS = new HashMap<>(1) {{
    put("Commands", FieldTypeEnum.STRINGLIST);
  }};

  public void validate(FileConfiguration config) throws ValidationException {
    validateSections(config);
    validateFields(config);
    validateRewards(config);
  }

  private void validateSections(FileConfiguration config) throws ValidationException {
    for (String section : SECTIONS) {
      if (!config.isConfigurationSection(section)) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.SECTION,
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

  private void validateRewards(FileConfiguration config) throws ValidationException {
    for (String reward : Objects.requireNonNull(config.getConfigurationSection("Rewards"))
        .getKeys(false)) {
      String rewardKey = "Rewards." + reward;
      validateRewardField(config, rewardKey);
      String rewardType = config.getString(rewardKey + ".Type");
      switch (Objects.requireNonNull(rewardType)) {
        case "ITEM":
          validateItemReward(config, rewardKey);
          continue;
        case "COMMAND":
          validateCommandReward(config, rewardKey);
          continue;
        default:
          throw new ValidationException(
              ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.REWARD,
                  rewardKey));
      }
    }
  }

  private void validateItemReward(FileConfiguration config, String rewardKey)
      throws ValidationException {
    for (String field : REWARD_ITEM_FIELDS.keySet()) {
      if (field.equals("Item")) {
        validateMaterialField(config, rewardKey + "." + field);
        continue;
      }
      switch (REWARD_ITEM_FIELDS.get(field)) {
        case INTEGER -> validateOptionalAmountField(config, rewardKey + "." + field);
        case STRING -> validateOptionalNameField(config, rewardKey + "." + field);
        case STRINGLIST -> validateOptionalLoreField(config, rewardKey + "." + field);
      }
    }
  }

  private void validateCommandReward(FileConfiguration config, String rewardKey)
      throws ValidationException {
    for (String field : REWARD_COMMAND_FIELDS.keySet()) {
      if (!field.equals("Commands")) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.STRINGLIST,
                field));
      }
      validateCommandField(config, rewardKey + "." + field);
    }
  }

  @Override
  public void validateIntegerField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isInt(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.INTEGER,
              field));
    }
  }

  @Override
  public void validateBooleanField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isBoolean(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.BOOLEAN,
              field));
    }
  }

  @Override
  public void validateStringListField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isList(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.STRINGLIST,
              field));
    }
  }

  @Override
  public void validateMaterialField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.MATERIAL,
              field));
    }
    String materialName = config.getString(field);
    assert materialName != null;
    if (Material.getMaterial(materialName) == null) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.MATERIAL,
              field));
    }
  }

  @Override
  public void validateRewardField(FileConfiguration config, String rewardKey)
      throws ValidationException {
    String typeField = rewardKey + ".Type";
    String chanceField = rewardKey + ".Chance";
    if (!config.isString(typeField)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.REWARD,
              typeField));
    }
    String type = config.getString(typeField);
    assert type != null;
    if (!type.equals("ITEM") && !type.equals("COMMAND")) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.REWARD,
              typeField));
    }
    if (!config.isInt(chanceField)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.REWARD,
              chanceField));
    }
    int chance = config.getInt(chanceField);
    if (chance <= 0) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.REWARD,
              chanceField));
    }
  }

  private void validateOptionalNameField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.contains(field)) {
      return;
    }
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.STRING, field));
    }
  }

  private void validateOptionalAmountField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.contains(field)) {
      return;
    }
    if (!config.isInt(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.INTEGER, field));
    }
  }

  private void validateOptionalLoreField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.contains(field)) {
      return;
    }
    if (!config.isList(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.STRINGLIST,
              field));
    }
  }

  private void validateCommandField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isList(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CRATES, FieldTypeEnum.STRINGLIST,
              field));
    }
  }
}
