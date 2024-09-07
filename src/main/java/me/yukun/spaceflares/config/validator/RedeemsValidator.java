package me.yukun.spaceflares.config.validator;

import java.util.Objects;
import me.yukun.spaceflares.config.ConfigTypeEnum;
import me.yukun.spaceflares.config.FieldTypeEnum;
import me.yukun.spaceflares.config.FlareConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class RedeemsValidator implements IValidator {

  public void validate(FileConfiguration config) throws ValidationException {
    validateSections(config);
  }

  private void validateSections(FileConfiguration config) throws ValidationException {
    for (String uuid : Objects.requireNonNull(config.getConfigurationSection("")).getKeys(false)) {
      validateFields(config, uuid);
    }
  }

  private void validateFields(FileConfiguration config, String uuid) throws ValidationException {
    for (String field : Objects.requireNonNull(config.getConfigurationSection(uuid))
        .getKeys(false)) {
      String fieldKey = uuid + "." + field;
      if (!FlareConfig.isFlare(field)) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.REDEEMS, FieldTypeEnum.INTEGER,
                fieldKey));
      }
      validateIntegerField(config, fieldKey);
      if (config.getInt(fieldKey, -1) < 1) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.REDEEMS, FieldTypeEnum.INTEGER,
                fieldKey));
      }
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
}
