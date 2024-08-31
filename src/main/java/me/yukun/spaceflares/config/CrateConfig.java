package me.yukun.spaceflares.config;

import java.util.HashMap;
import java.util.Map;
import me.yukun.spaceflares.config.validator.CrateConfigValidator;
import me.yukun.spaceflares.config.validator.ValidationException;
import org.bukkit.configuration.file.FileConfiguration;

public class CrateConfig {

  private static final Map<String, CrateConfig> nameConfigMap = new HashMap<>();

  private final String name;
  private final FileConfiguration config;

  public CrateConfig(String name, FileConfiguration config) {
    this.name = name;
    this.config = config;
    nameConfigMap.put(this.name, this);
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
    nameConfigMap.clear();
  }

  public static boolean getCrateDoDespawnGive(String crate) {
    return nameConfigMap.get(crate).getDoDespawnGive();
  }

  private boolean getDoDespawnGive() {
    return config.getBoolean("Despawn.Give");
  }
}
