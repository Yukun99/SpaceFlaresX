package me.yukun.spaceflares.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import me.yukun.spaceflares.config.validator.ConfigValidator;
import me.yukun.spaceflares.config.validator.MessagesValidator;
import me.yukun.spaceflares.config.validator.RedeemsValidator;
import me.yukun.spaceflares.config.validator.ValidationException;
import me.yukun.spaceflares.util.IOExceptionConsumer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Class that manages configuration file I/O.
 */
public class FileManager {

  private static FileManager fileManager;
  private final File dataFolder;

  private final Map<ConfigTypeEnum, FileConfiguration> fileConfigurationMap = new HashMap<>();

  // Processing maps
  private final Map<ConfigTypeEnum, Consumer<ConfigTypeEnum>> validateConfigMap = new HashMap<>() {{
    put(ConfigTypeEnum.CONFIG, configType -> {
      try {
        new ConfigValidator().validate(fileManager.fileConfigurationMap.get(configType));
        Messages.printValidationSuccess(configType);
      } catch (ValidationException e) {
        Messages.printConfigError(e);
      }
    });
    put(ConfigTypeEnum.MESSAGES, configType -> {
      try {
        new MessagesValidator().validate(fileManager.fileConfigurationMap.get(configType));
        Messages.printValidationSuccess(configType);
      } catch (ValidationException e) {
        Messages.printConfigError(e);
      }
    });
    put(ConfigTypeEnum.REDEEMS, configType -> {
      try {
        new RedeemsValidator().validate(fileManager.fileConfigurationMap.get(configType));
        Messages.printValidationSuccess(configType);
      } catch (ValidationException e) {
        Messages.printConfigError(e);
      }
    });
    put(ConfigTypeEnum.FLARES, configType -> FlareConfig.validate());
    put(ConfigTypeEnum.CRATES, configType -> CrateConfig.validate());
  }};
  // Why have setup step? Validate before loading info so that we *don't* get YAML parsing errors.
  private final Map<ConfigTypeEnum, Consumer<ConfigTypeEnum>> setupConfigMap = new HashMap<>() {{
    put(ConfigTypeEnum.CONFIG, configType -> {
      validateConfigMap.get(configType).accept(configType);
      Config.setup(fileManager.fileConfigurationMap.get(configType));
    });
    put(ConfigTypeEnum.MESSAGES, configType -> {
      validateConfigMap.get(configType).accept(configType);
      Messages.setup(fileManager.fileConfigurationMap.get(configType));
    });
    put(ConfigTypeEnum.REDEEMS, configType -> {
      validateConfigMap.get(configType).accept(configType);
      Redeems.setup(fileManager.fileConfigurationMap.get(configType), redeemsFile);
    });
    put(ConfigTypeEnum.FLARES, configType -> {
      validateConfigMap.get(configType).accept(configType);
      FlareConfig.setup();
    });
    put(ConfigTypeEnum.CRATES, configType -> {
      validateConfigMap.get(configType).accept(configType);
      CrateConfig.setup();
    });
  }};

  // Files and FileConfigurations
  private File configFile;
  private File messagesFile;
  private File redeemsFile;
  private final Map<ConfigTypeEnum, IOExceptionConsumer<FileConfiguration>> saveConfigMap = new HashMap<>(
      3) {{
    put(ConfigTypeEnum.REDEEMS, (redeems) -> redeems.save(redeemsFile));
  }};
  private final Map<ConfigTypeEnum, Consumer<ConfigTypeEnum>> reloadConfigMap = new HashMap<>() {{
    put(ConfigTypeEnum.CONFIG, configType -> {
      fileConfigurationMap.put(configType, YamlConfiguration.loadConfiguration(configFile));
      setupConfigMap.get(configType).accept(configType);
      Messages.printReloaded(configType);
    });
    put(ConfigTypeEnum.MESSAGES, configType -> {
      fileConfigurationMap.put(configType, YamlConfiguration.loadConfiguration(messagesFile));
      setupConfigMap.get(configType).accept(configType);
      Messages.printReloaded(configType);
    });
    put(ConfigTypeEnum.REDEEMS, configType -> {
      fileConfigurationMap.put(configType, YamlConfiguration.loadConfiguration(redeemsFile));
      setupConfigMap.get(configType).accept(configType);
      Messages.printReloaded(configType);
    });
    put(ConfigTypeEnum.FLARES, configType -> {
      FlareConfig.reload();
      setupConfigMap.get(configType).accept(configType);
      Messages.printReloaded(configType);
    });
    put(ConfigTypeEnum.CRATES, configType -> {
      CrateConfig.reload();
      setupConfigMap.get(configType).accept(configType);
      Messages.printReloaded(configType);
    });
  }};

  /**
   * Constructor for a new FileManager instance.
   *
   * @param plugin Plugin to create FileManager instance for.
   * @throws Exception If config files cannot be loaded properly.
   */
  private FileManager(Plugin plugin) throws Exception {
    createFolder(plugin);
    dataFolder = plugin.getDataFolder();

    configFile = createFile("Config.yml");
    messagesFile = createFile("Messages.yml");
    redeemsFile = createFile("Redeems.yml");

    fileConfigurationMap.put(ConfigTypeEnum.CONFIG,
        YamlConfiguration.loadConfiguration(configFile));
    fileConfigurationMap.put(ConfigTypeEnum.MESSAGES,
        YamlConfiguration.loadConfiguration(messagesFile));
    fileConfigurationMap.put(ConfigTypeEnum.REDEEMS,
        YamlConfiguration.loadConfiguration(redeemsFile));

    loadFlareFolder(plugin);
    loadCrateFolder(plugin);
  }

  /**
   * Instantiates the FileManager instance to be used by the plugin.
   *
   * @param plugin Plugin to be instantiated from.
   * @return Whether config files have created successfully.
   */
  public static boolean onEnable(Plugin plugin) {
    try {
      fileManager = new FileManager(plugin);
      for (ConfigTypeEnum configType : fileManager.setupConfigMap.keySet()) {
        fileManager.setupConfigMap.get(configType).accept(configType);
      }
      return true;
    } catch (Exception e) {
      Messages.printConfigError(e);
      return false;
    }
  }

  /**
   * Saves modified config files on server close.
   */
  public static void onDisable() {
    for (ConfigTypeEnum configType : fileManager.saveConfigMap.keySet()) {
      try {
        fileManager.saveConfigMap.get(configType)
            .accept(fileManager.fileConfigurationMap.get(configType));
      } catch (IOException e) {
        Messages.printSaveError(configType.toString());
      }
    }
  }

  /**
   * Reloads config files when plugin is running.
   */
  public static void reload() {
    for (ConfigTypeEnum configType : fileManager.reloadConfigMap.keySet()) {
      fileManager.reloadConfigMap.get(configType).accept(configType);
    }
  }

  /**
   * Creates config folder if it doesn't exist.
   *
   * @param plugin Plugin to create config folder for.
   */
  private void createFolder(Plugin plugin) {
    if (plugin.getDataFolder().mkdir()) {
      Messages.printFolderNotExists("Config folder");
    } else {
      Messages.printFolderExists("Config folder");
    }
  }

  /**
   * Creates flare config folder and files if they don't exist.
   *
   * @param plugin Plugin to create flare folder and files for.
   * @throws IOException If flare config files cannot be loaded properly.
   */
  private void loadFlareFolder(Plugin plugin) throws IOException {
    File file = new File(plugin.getDataFolder(), "/flares");
    if (file.mkdirs()) {
      Messages.printFolderNotExists("Flares folder");
    } else {
      Messages.printFolderExists("Flares folder");
    }
    String[] currentFiles = file.list();
    assert currentFiles != null;
    if (currentFiles.length == 0) {
      loadFlareConfig("Example");
      return;
    }
    for (String filename : currentFiles) {
      if (!filename.endsWith(".yml")) {
        continue;
      }
      String name = filename.substring(0, filename.length() - 4);
      loadFlareConfig(name);
    }
  }

  /**
   * Loads flare config file. Copies default flare config file from plugin if it doesn't exist.
   *
   * @param name Name of flare config file to create/copy.
   * @throws IOException If flare config file cannot be loaded properly.
   */
  private void loadFlareConfig(String name) throws IOException {
    File flareFile = createFile("flares/" + name + ".yml");
    FileConfiguration flareConfig = YamlConfiguration.loadConfiguration(flareFile);
    new FlareConfig(name, flareConfig, flareFile);
  }

  /**
   * Creates crate config folder and files if they don't exist.
   *
   * @param plugin Plugin to create crate folder and files for.
   * @throws IOException If crate config files cannot be loaded properly.
   */
  private void loadCrateFolder(Plugin plugin) throws IOException {
    File file = new File(plugin.getDataFolder(), "/crates");
    if (file.mkdirs()) {
      Messages.printFolderNotExists("Crates folder");
    } else {
      Messages.printFolderExists("Crates folder");
    }
    String[] currentFiles = file.list();
    assert currentFiles != null;
    if (currentFiles.length == 0) {
      loadCrateConfig("Example");
      return;
    }
    for (String filename : currentFiles) {
      if (!filename.endsWith(".yml")) {
        continue;
      }
      String name = filename.substring(0, filename.length() - 4);
      loadCrateConfig(name);
    }
  }

  /**
   * Loads crate config file. Copies default crate config file from plugin if it doesn't exist.
   *
   * @param name Name of crate config file to create/copy.
   * @throws IOException If crate config file cannot be loaded properly.
   */
  private void loadCrateConfig(String name) throws IOException {
    File crateFile = createFile("crates/" + name + ".yml");
    FileConfiguration crateConfig = YamlConfiguration.loadConfiguration(crateFile);
    new CrateConfig(name, crateConfig, crateFile);
  }

  /**
   * Attempts to copy default config file into config folder. Does not copy file if config file
   * already exists in config folder.
   *
   * @param filename Filename of file to be created.
   * @return File instance pointing to the specified filename in config folder.
   * @throws IOException If default config file does not copy successfully.
   */
  private File createFile(String filename) throws IOException {
    File file = new File(dataFolder, filename);
    if (file.exists()) {
      Messages.printFileExists(filename);
      return file;
    }
    Messages.printFileNotExists(filename);
    File defaultFile = new File(dataFolder, "/" + filename);
    InputStream inputStream = getClass().getResourceAsStream("/" + filename);
    try {
      copyFile(inputStream, defaultFile);
    } catch (IOException copyException) {
      Messages.printFileCopyError(filename);
      throw copyException;
    }
    return file;
  }

  /**
   * Copies files from inside the jar to outside. Adapted from\
   * <a href="https://bukkit.org/threads/extracting-file-from-jar.16962/">thread</a>
   *
   * @param in  Where to copy file from.
   * @param out Where to copy file to.
   * @throws IOException If file does not get copied successfully.
   */
  private void copyFile(InputStream in, File out) throws IOException {
    try (InputStream fis = in; FileOutputStream fos = new FileOutputStream(out)) {
      byte[] buf = new byte[1024];
      int i;
      while ((i = fis.read(buf)) != -1) {
        fos.write(buf, 0, i);
      }
    }
  }
}
