package me.yukun.spaceflares.config.validator;

import java.util.Objects;
import me.yukun.spaceflares.config.ConfigTypeEnum;
import me.yukun.spaceflares.config.EnvoyConfig;
import me.yukun.spaceflares.config.FieldTypeEnum;
import me.yukun.spaceflares.config.FlareConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class RedeemsValidator implements IValidator {

  public void validate(FileConfiguration config) throws ValidationException {
    validateSections(config);
  }

  private void validateSections(FileConfiguration config) throws ValidationException {
    for (String uuid : Objects.requireNonNull(config.getConfigurationSection("")).getKeys(false)) {
      if (config.isConfigurationSection(uuid + ".Flare")) {
        validateFlareFields(config, uuid);
      }
      if (config.isConfigurationSection(uuid + ".Envoy")) {
        validateEnvoyFlareFields(config, uuid);
      }
    }
  }

  private void validateFlareFields(FileConfiguration config, String uuid)
      throws ValidationException {
    for (String field : Objects.requireNonNull(config.getConfigurationSection(uuid + ".Flare"))
        .getKeys(false)) {
      String fieldKey = uuid + ".Flare." + field;
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

  private void validateEnvoyFlareFields(FileConfiguration config, String uuid)
      throws ValidationException {
    for (String field : Objects.requireNonNull(config.getConfigurationSection(uuid + ".Envoy"))
        .getKeys(false)) {
      String fieldKey = uuid + ".Envoy." + field;
      if (!EnvoyConfig.isEnvoy(field)) {
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
