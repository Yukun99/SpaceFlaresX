package me.yukun.spaceflares.config;

import static me.yukun.spaceflares.util.TextFormatter.applyColor;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import me.yukun.spaceflares.command.CommandManager;
import me.yukun.spaceflares.command.CommandTypeEnum;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Messages {

  // Plugin info ping messages.
  private static final String VERSION = "SpaceFlares v%version% loaded.";
  private static final String INTEGRATION = "%integration% integration enabled.";
  // Configuration related logging.
  private static final String EXISTS = " exists, skipping creation.";
  private static final String NOT_EXISTS = " not created, creating now.";
  private static final String COPY_ERROR = "&cERROR! %file% could not be created.";
  private static final String CONFIG_ERROR = "&cERROR! Config files could not load properly. Plugin will be disabled.";
  private static final String SAVE_ERROR = "&cERROR! %file% could not be saved.";
  private static final String VALIDATION_SUCCESS = "&aValidation success! %file% has no errors.";
  private static final String RELOAD = "&a%file% reloaded!";
  // Command reply messages.
  private static final String HELP_HEADER = "&b&l===============SpaceFlares / Envoys===============";
  private static final String HELP_FLARE = "Please use /spaceflares help to view spaceflare commands.";
  private static final String HELP_ENVOY = "Please use /envoy help to view envoy commands.";
  private static final String HELP_COMMANDS = "&b&l----------Commands Page 1----------";
  private static final String HELP_FLARE_ALIASES = "Command aliases: spaceflares, spaceflare, spacef, sflare, sf";
  private static final String HELP_ENVOY_ALIASES = "Command aliases: envoy, envoys";
  private static final String HELP_HELP = "/%cmd% help: Shows commands, aliases and permissions for %type% commands.";
  private static final String HELP_REDEEM = "/%cmd% redeem: Opens %type% redeeming GUI.";
  private static final String HELP_GIVE1 = "/%cmd% give (player) (%type%) (amount): Give player (amount) %type%s of specified type.";
  private static final String HELP_GIVE2 = "/%cmd% give (player) (%type%): Give player 1 %type% of specified type.";
  private static final String HELP_GIVE3 = "/%cmd% give (%type%) (amount): Give yourself (amount) %type%s of specified type.";
  private static final String HELP_GIVE4 = "/%cmd% give (%type%): Give yourself 1 %type% of specified type.";
  private static final String HELP_FLARE_SUMMON1 = "/spaceflares summon (flare) (x) (y) (z): Summons flare of specified tier at specified coordinates.";
  private static final String HELP_FLARE_SUMMON2 = "/spaceflares summon (flare) ~ ~ ~: Summons flare of specified tier at own coordinates.";
  private static final String HELP_FLARE_SUMMON3 = "/spaceflares summon (flare) ~x ~y ~z: Summons flare of specified tier at own coordinates with offsets.";
  private static final String HELP_FLARE_SUMMON4 = "  - Any of the above coordinate formats can be mixed, including negative numbers.";
  private static final String HELP_FLARE_SUMMON5 = "  - Example: '/spaceflares summon Example 1 ~ ~-24' is a valid command.";
  private static final String HELP_ENVOY_LIST = "/envoy list: Sends list of all envoys.";
  private static final String HELP_ENVOY_START = "/envoy start (envoy): Starts envoy of specified tier.";
  private static final String HELP_ENVOY_STOP = "/envoy stop (envoy): Stops envoy of specified tier.";
  private static final String HELP_ENVOY_EDIT = "/envoy edit (envoy): Toggles edit mode for envoy of specified tier.";
  private static final String HELP_RELOAD = "/spaceflares reload: Reloads all configuration files.";
  private static final String HELP_PERMISSIONS = "&b&l----------Permissions----------";
  private static final String HELP_WILDCARD_P = "spaceflares.*: All permissions combined.";
  private static final String HELP_ADMIN_P = "spaceflares.admin: Ability to use any command.";
  private static final String HELP_HELP_P = "spaceflares.help: Ability to use /%cmd% help command.";
  private static final String HELP_REDEEM_P = "spaceflares.redeem: Ability to use /%cmd% redeem command.";
  private static final String HELP_GIVE_P = "spaceflares.give: Ability to use /%cmd% give command.";
  private static final String HELP_SUMMON_P = "spaceflares.summon: Ability to use /spaceflares summon command.";
  private static final String HELP_LIST_P = "spaceflares.list: Ability to use /envoy list command.";
  private static final String HELP_START_P = "spaceflares.start: Ability to use /envoy start command.";
  private static final String HELP_STOP_P = "spaceflares.stop: Ability to use /envoy stop command.";
  private static final String HELP_QUERY_P = "spaceflares.query: Ability to use /envoy query commands.";
  private static final String HELP_EDIT_P = "spaceflares.edit: Ability to use /envoy edit commands.";
  private static final String HELP_RELOAD_P = "spaceflares.reload: Ability to use reload command.";
  private static final String HELP_FOOTER = "&b&l======================================";
  private static final String RELOAD_SUCCESS = "&aReload successful!";
  private static final String NO_PERMISSION = "&cYou do not have permission to use the command!";
  // Prefix appended before all messages.
  private static String prefix = "&bSpace&eFlares&f >> &7";
  // Placeholder formats.
  private static String locP;
  private static String playerP;
  private static String eTimeP;
  // Messages sent to players.
  private static String give;
  private static String giveFull;
  private static String receive;
  private static String receiveFull;
  private static String noSummon;
  private static List<String> summon;
  private static List<String> summonAll;
  private static List<String> land;
  private static List<String> landAll;
  private static List<String> claim;
  private static List<String> claimAll;
  private static String despawnNotify;
  private static String despawnItems;
  private static List<String> despawnAll;
  private static String redeem;
  private static String redeemFull;
  private static String envoyNoSummon;
  private static List<String> envoySummon;
  private static List<String> envoyStart;
  private static String envoyClaim;
  private static String envoyClaimAll;
  private static String envoyEnd;
  private static String envoyNoEnd;
  private static String envoyRemain;
  private static String envoyCooldown;
  private static String envoyNoCooldown;
  private static String envoyNoExist;
  private static String envoyList;
  private static String envoyGive;
  private static String envoyGiveFull;
  private static String envoyReceive;
  private static String envoyReceiveFull;
  private static String envoyRedeem;
  private static String envoyEditEditing;
  private static String envoyEditStart;
  private static String envoyEditStop;
  private static String envoyEditSave;
  private static String envoyEditNotAir;
  private static String envoyEditNoSave;
  private static String envoyEditDelete;

  protected static void setup(FileConfiguration config) {
    setupStrings(config);
    setupStringLists(config);
  }

  private static void setupStrings(FileConfiguration config) {
    prefix = config.getString("Prefix");
    locP = config.getString("Placeholder.Loc");
    playerP = config.getString("Placeholder.Player");
    eTimeP = config.getString("Placeholder.ETime");
    give = prefix + config.getString("Give");
    giveFull = prefix + config.getString("GiveFull");
    receive = prefix + config.getString("Receive");
    receiveFull = prefix + config.getString("ReceiveFull");
    noSummon = prefix + config.getString("NoSummon");
    despawnNotify = prefix + config.getString("Despawn.Notify");
    despawnItems = config.getString("Despawn.Items");
    redeem = prefix + config.getString("Redeem");
    redeemFull = prefix + config.getString("RedeemFull");
    envoyNoSummon = prefix + config.getString("Envoy.NoSummon");
    envoyClaim = prefix + config.getString("Envoy.Claim");
    envoyClaimAll = prefix + config.getString("Envoy.ClaimAll");
    envoyEnd = prefix + config.getString("Envoy.End");
    envoyRemain = prefix + config.getString("Envoy.Remain");
    envoyCooldown = prefix + config.getString("Envoy.Cooldown");
    envoyNoCooldown = prefix + config.getString("Envoy.NoCooldown");
    envoyNoExist = prefix + config.getString("Envoy.NoExist");
    envoyList = config.getString("Envoy.List");
    envoyGive = prefix + config.getString("Envoy.Give");
    envoyGiveFull = prefix + config.getString("Envoy.GiveFull");
    envoyReceive = prefix + config.getString("Envoy.Receive");
    envoyReceiveFull = prefix + config.getString("Envoy.ReceiveFull");
    envoyRedeem = prefix + config.getString("Envoy.Redeem");
    envoyEditEditing = prefix + config.getString("Envoy.Edit.Editing");
    envoyEditStart = prefix + config.getString("Envoy.Edit.Start");
    envoyEditStop = prefix + config.getString("Envoy.Edit.Stop");
    envoyEditSave = prefix + config.getString("Envoy.Edit.Save");
    envoyEditNotAir = prefix + config.getString("Envoy.Edit.NotAir");
    envoyEditNoSave = prefix + config.getString("Envoy.Edit.NoSave");
    envoyEditDelete = prefix + config.getString("Envoy.Edit.Delete");
    envoyNoEnd = prefix + config.getString("Envoy.Edit.NoEnd");
  }

  private static void setupStringLists(FileConfiguration config) {
    summon = config.getStringList("Summon");
    summonAll = config.getStringList("SummonAll");
    land = config.getStringList("Land");
    landAll = config.getStringList("LandAll");
    claim = config.getStringList("Claim");
    claimAll = config.getStringList("ClaimAll");
    despawnAll = config.getStringList("Despawn");
    envoySummon = config.getStringList("Envoy.Summon");
    envoyStart = config.getStringList("Envoy.Start");
  }

  /**
   * Sends plugin version info to specified player.
   *
   * @param player Player to send plugin version info to.
   * @param plugin Plugin to get version info for.
   */
  public static void sendPluginVersion(Player player, Plugin plugin) {
    String message = prefix + VERSION.replaceAll("%version%", plugin.getDescription().getVersion());
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends plugin integration enabled status to specified player.
   *
   * @param player          Player to send plugin integration enabled status to.
   * @param integrationName Name of plugin that has integration enabled.
   */
  public static void sendIntegrationEnabled(Player player, String integrationName) {
    String message = prefix + INTEGRATION.replaceAll("%integration%", integrationName);
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends config error message to specified player.
   *
   * @param player Player to send config error message to.
   */
  public static void sendConfigError(Player player) {
    player.sendMessage(applyColor(prefix + CONFIG_ERROR));
  }

  /**
   * Logging message during setup sent if config folder exists.
   */
  protected static void printFolderExists(String folder) {
    System.out.println(applyColor(prefix + folder + EXISTS));
  }

  /**
   * Logging message during setup sent if config folder does not exist.
   */
  protected static void printFolderNotExists(String folder) {
    System.out.println(applyColor(prefix + folder + NOT_EXISTS));
  }

  /**
   * Logging message during setup sent if specified file exists in config folder.
   *
   * @param filename Filename of specified file.
   */
  protected static void printFileExists(String filename) {
    System.out.println(applyColor(prefix + filename + EXISTS));
  }

  /**
   * Logging message during setup sent if specified file does not exist in config folder.
   *
   * @param filename Filename of specified file.
   */
  protected static void printFileNotExists(String filename) {
    System.out.println(applyColor(prefix + filename + NOT_EXISTS));
  }

  /**
   * Logging message during setup sent if specified file could not be copied to config folder.
   *
   * @param filename Filename of specified file.
   */
  protected static void printFileCopyError(String filename) {
    String message = prefix + COPY_ERROR.replaceAll("%file%", filename);
    System.out.println(applyColor(message));
  }

  /**
   * Logging message during setup sent if config files contain errors and could not load properly.
   */
  public static void printConfigError(Exception exception) {
    System.out.println(applyColor(prefix + CONFIG_ERROR));
    System.out.println(applyColor(prefix + exception.getMessage()));
  }

  /**
   * Logging message during shutdown sent if specified file could not be saved.
   *
   * @param filename Filename of specified file.
   */
  public static void printSaveError(String filename) {
    String message = prefix + SAVE_ERROR.replaceAll("%file%", filename);
    System.out.println(applyColor(message));
  }

  /**
   * Logging message during setup sent if specified file is validated successfully.
   *
   * @param configType Configuration file type that was validated successfully.
   */
  public static void printValidationSuccess(ConfigTypeEnum configType) {
    String message = prefix + VALIDATION_SUCCESS.replaceAll("%file%", configType.toString());
    System.out.println(applyColor(message));
  }

  /**
   * Logging message during setup sent if specified file is validated successfully.
   *
   * @param configName Configuration file name that was validated successfully.
   */
  public static void printValidationSuccess(String configName) {
    String message = prefix + VALIDATION_SUCCESS.replaceAll("%file%", configName);
    System.out.println(applyColor(message));
  }

  /**
   * Logging message during reloading sent if specified file is reloaded successfully.
   *
   * @param configType Configuration file type that was reloaded successfully.
   */
  public static void printReloaded(ConfigTypeEnum configType) {
    String message = prefix + RELOAD.replaceAll("%file%", configType.toString());
    System.out.println(applyColor(message));
  }

  /**
   * Send commands help message to command sender.
   *
   * @param sender Command sender to send commands help message to.
   */
  public static void sendHelp(CommandSender sender, boolean isFlareCommand) {
    String command = isFlareCommand ? "spaceflares" : "envoy";
    String type = isFlareCommand ? "flare" : "envoy";
    sender.sendMessage(applyColor(HELP_HEADER));
    if (isFlareCommand) {
      sender.sendMessage(applyColor((HELP_ENVOY)));
    } else {
      sender.sendMessage(applyColor((HELP_FLARE)));
    }
    sender.sendMessage(applyColor(HELP_COMMANDS));
    if (isFlareCommand) {
      sender.sendMessage(applyColor(HELP_FLARE_ALIASES));
    } else {
      sender.sendMessage(applyColor((HELP_ENVOY_ALIASES)));
    }
    if (CommandManager.hasCommandPermissions(sender, CommandTypeEnum.HELP)) {
      sender.sendMessage(applyColor(replaceCmdType(HELP_HELP, command, type)));
    }
    if (sender instanceof Player && CommandManager.hasCommandPermissions(sender,
        CommandTypeEnum.REDEEM)) {
      sender.sendMessage(applyColor(replaceCmdType(HELP_REDEEM, command, type)));
    }
    if (CommandManager.hasCommandPermissions(sender, CommandTypeEnum.GIVE)) {
      sender.sendMessage(applyColor(replaceCmdType(HELP_GIVE1, command, type)));
      sender.sendMessage(applyColor(replaceCmdType(HELP_GIVE2, command, type)));
      if (sender instanceof Player) {
        sender.sendMessage(applyColor(replaceCmdType(HELP_GIVE3, command, type)));
        sender.sendMessage(applyColor(replaceCmdType(HELP_GIVE4, command, type)));
      }
    }
    if (isFlareCommand && CommandManager.hasCommandPermissions(sender, CommandTypeEnum.SUMMON)) {
      sender.sendMessage(applyColor(HELP_FLARE_SUMMON1));
      if (sender instanceof Player) {
        sender.sendMessage(applyColor(HELP_FLARE_SUMMON2));
        sender.sendMessage(applyColor(HELP_FLARE_SUMMON3));
        sender.sendMessage(applyColor(HELP_FLARE_SUMMON4));
        sender.sendMessage(applyColor(HELP_FLARE_SUMMON5));
      }
    }
    if (!isFlareCommand) {
      if (CommandManager.hasCommandPermissions(sender, CommandTypeEnum.LIST)) {
        sender.sendMessage(applyColor(HELP_ENVOY_LIST));
      }
      if (CommandManager.hasCommandPermissions(sender, CommandTypeEnum.START)) {
        sender.sendMessage(applyColor(HELP_ENVOY_START));
      }
      if (CommandManager.hasCommandPermissions(sender, CommandTypeEnum.STOP)) {
        sender.sendMessage(applyColor(HELP_ENVOY_STOP));
      }
      if (sender instanceof Player &&
          CommandManager.hasCommandPermissions(sender, CommandTypeEnum.EDIT)) {
        sender.sendMessage(applyColor(HELP_ENVOY_EDIT));
      }
    }
    if (CommandManager.hasCommandPermissions(sender, CommandTypeEnum.RELOAD)) {
      sender.sendMessage(applyColor(HELP_RELOAD));
    }
    if (CommandManager.hasAdminPermissions(sender)) {
      sender.sendMessage(applyColor(HELP_PERMISSIONS));
      sender.sendMessage(applyColor(HELP_WILDCARD_P));
      sender.sendMessage(applyColor(HELP_ADMIN_P));
      sender.sendMessage(applyColor(replaceCmd(HELP_HELP_P, command)));
      sender.sendMessage(applyColor(replaceCmd(HELP_REDEEM_P, command)));
      sender.sendMessage(applyColor(replaceCmd(HELP_GIVE_P, command)));
      if (isFlareCommand) {
        sender.sendMessage(applyColor(HELP_SUMMON_P));
      } else {
        sender.sendMessage(applyColor(HELP_LIST_P));
        sender.sendMessage(applyColor(HELP_START_P));
        sender.sendMessage(applyColor(HELP_STOP_P));
        sender.sendMessage(applyColor(HELP_EDIT_P));
        sender.sendMessage(applyColor(HELP_QUERY_P));
      }
      sender.sendMessage(applyColor(HELP_RELOAD_P));
    }
    sender.sendMessage(applyColor(HELP_FOOTER));
  }

  /**
   * Send config reloaded message to command sender.
   *
   * @param sender CommandSender to send reloaded message to.
   */
  public static void sendReloadSuccess(CommandSender sender) {
    sender.sendMessage(applyColor(prefix + RELOAD_SUCCESS));
  }

  /**
   * Send no permission message to command sender.
   *
   * @param sender CommandSender to send no permission message to.
   */
  public static void sendNoPermission(CommandSender sender) {
    sender.sendMessage(applyColor(prefix + NO_PERMISSION));
  }

  /**
   * Gets location placeholder value for specified location.
   *
   * @param loc Location to get placeholder value for.
   * @return Location placeholder value.
   */
  private static String getLocation(Location loc) {
    String message = locP.replaceAll("%world%", Objects.requireNonNull(loc.getWorld()).getName());
    message = message.replaceAll("%x%", String.valueOf((int) loc.getX()));
    message = message.replaceAll("%y%", String.valueOf((int) loc.getY()));
    return message.replaceAll("%z%", String.valueOf((int) loc.getZ()));
  }

  /**
   * Gets player name placeholder value for specified player.
   *
   * @param player Player username to get placeholder value for.
   * @return Player name placeholder value.
   */
  private static String getPlayerName(Player player) {
    String message = playerP.replaceAll("%username%", player.getName());
    return message.replaceAll("%displayname%", player.getDisplayName());
  }

  /**
   * Gets e_time placeholder value for specified time.
   *
   * @param time List of integers corresponding to e_time value.
   * @return E_time placeholder value.
   */
  private static String getETime(List<Integer> time) {
    String message = eTimeP.replaceAll("%day%", String.valueOf(time.get(0)));
    message = message.replaceAll("%hour%", String.valueOf(time.get(1)));
    message = message.replaceAll("%min%", String.valueOf(time.get(2)));
    return message.replaceAll("%sec%", String.valueOf(time.get(3)));
  }

  /**
   * Sends confirmation message for when flare give command is successfully used.
   *
   * @param sender CommandSender to send confirmation message to.
   * @param player Player who was successfully given flares.
   * @param tier   Tier of flares given to player.
   * @param amount Amount of flares given to player.
   */
  public static void sendGive(CommandSender sender, Player player, String tier, int amount) {
    String message = replacePlayerTierAmount(give, player, tier, amount);
    sender.sendMessage(applyColor(message));
  }

  /**
   * Sends message for when flares are sent to redeems because inventory is full.
   *
   * @param sender CommandSender to send message to.
   * @param player Player who was successfully given flares.
   * @param amount Amount of flares sent to redeems inventory.
   */
  public static void sendGiveFull(CommandSender sender, Player player, int amount) {
    String message = replacePlayerAmount(giveFull, player, amount);
    sender.sendMessage(applyColor(message));
  }

  /**
   * Sends confirmation message for when flares are successfully received by player.
   *
   * @param player Player to send confirmation message to.
   * @param tier   Tier of flares given to player.
   * @param amount Amount of flares given to player.
   */
  public static void sendReceive(Player player, String tier, int amount) {
    String message = replaceTierAmount(receive, tier, amount);
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends message for when flares are sent to redeems because inventory is full.
   *
   * @param player Player to send message to.
   * @param amount Amount of flares sent to redeems inventory.
   */
  public static void sendReceiveFull(Player player, int amount) {
    String message = receiveFull.replaceAll("%amount%", String.valueOf(amount));
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends message for when player is in the wrong region and cannot start flares.
   *
   * @param player Player to send invalid region message to.
   */
  public static void sendNoSummon(Player player) {
    player.sendMessage(applyColor(noSummon));
  }

  /**
   * Sends confirmation message for when specified player summons a flare.
   *
   * @param player Player to send confirmation message to.
   * @param tier   Tier of flare summoned.
   * @param loc    Location where flare is summoned.
   */
  public static void sendSummon(Player player, String tier, Location loc) {
    for (String line : summon) {
      String message = replaceTierLoc(line, tier, loc);
      player.sendMessage(applyColor(message));
    }
  }

  /**
   * Sends announcement message for when a player summons a flare.
   *
   * @param player Player who summoned the flare.
   * @param tier   Tier of flare summoned.
   * @param loc    Location where flare is summoned.
   */
  public static void sendSummonAll(Player player, String tier, Location loc) {
    //noinspection DuplicatedCode
    if (!FlareConfig.getFlareDoAnnounce(tier)) {
      return;
    }
    int range = FlareConfig.getFlareAnnounceRange(tier);
    for (String line : summonAll) {
      String message = replacePlayerTierLoc(line, player, tier, loc);
      for (Player other : getPlayersInRange(loc, range)) {
        if (other.equals(player)) {
          continue;
        }
        other.sendMessage(applyColor(message));
      }
    }
  }

  /**
   * Sends confirmation message for when specified player's summoned flare lands.
   *
   * @param player Player to send confirmation message to.
   * @param tier   Tier of flare summoned.
   * @param loc    Location where flare has landed.
   */
  public static void sendLand(Player player, String tier, Location loc) {
    for (String line : land) {
      int time = CrateConfig.getCrateDespawnTime(tier);
      String message = replaceTierTimeLoc(line, tier, time, loc);
      player.sendMessage(applyColor(message));
    }
  }

  /**
   * Sends announcement message for when a player's summoned flare lands.
   *
   * @param player Player who summoned the flare.
   * @param tier   Tier of flare summoned.
   * @param loc    Location where flare has landed.
   */
  public static void sendLandAll(Player player, String tier, Location loc) {
    if (!FlareConfig.getFlareDoAnnounce(tier)) {
      return;
    }
    int range = FlareConfig.getFlareAnnounceRange(tier);
    for (String line : landAll) {
      int time = CrateConfig.getCrateDespawnTime(tier);
      String message = replacePlayerTierTimeLoc(line, player, tier, time, loc);
      for (Player other : getPlayersInRange(loc, range)) {
        if (other.equals(player)) {
          continue;
        }
        other.sendMessage(applyColor(message));
      }
    }
  }

  /**
   * Sends confirmation message for when specified player claims a crate.
   *
   * @param player Player who claimed the crate.
   * @param tier   Tier of crate claimed.
   * @param loc    Location where crate was claimed.
   */
  public static void sendClaim(Player player, String tier, Location loc) {
    for (String line : claim) {
      String message = replaceTierLoc(line, tier, loc);
      player.sendMessage(applyColor(message));
    }
  }

  /**
   * Sends announcement message for when a player claims a crate.
   *
   * @param player Player who claimed the crate.
   * @param tier   Tier of crate claimed.
   * @param loc    Location where crate was claimed.
   */
  public static void sendClaimAll(Player player, String tier, Location loc) {
    //noinspection DuplicatedCode
    if (!FlareConfig.getFlareDoAnnounce(tier)) {
      return;
    }
    int range = FlareConfig.getFlareAnnounceRange(tier);
    for (String line : claimAll) {
      String message = replacePlayerTierLoc(line, player, tier, loc);
      for (Player other : getPlayersInRange(loc, range)) {
        if (other.equals(player)) {
          continue;
        }
        other.sendMessage(applyColor(message));
      }
    }
  }

  /**
   * Sends confirmation message for when specified player's landed crate despawns.
   *
   * @param player Player whose crate despawned.
   * @param tier   Tier of crate despawned.
   * @param loc    Location where crate despawned.
   */
  public static void sendDespawnNotify(Player player, String tier, Location loc) {
    String message = replaceTierLoc(despawnNotify, tier, loc);
    if (CrateConfig.getCrateDoDespawnGive(tier)) {
      message = message + " " + despawnItems;
    }
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends announcement message for when a player's landed crate despawns.
   *
   * @param player Player whose crate despawned.
   * @param tier   Tier of crate despawned.
   * @param loc    Location where crate despawned.
   */
  public static void sendDespawnAll(Player player, String tier, Location loc) {
    if (!FlareConfig.getFlareDoAnnounce(tier)) {
      return;
    }
    int range = FlareConfig.getFlareAnnounceRange(tier);
    for (String line : despawnAll) {
      String message = replaceLoc(line, loc);
      for (Player other : getPlayersInRange(loc, range)) {
        if (other.equals(player)) {
          continue;
        }
        other.sendMessage(applyColor(message));
      }
    }
  }

  /**
   * Sends confirmation message for when player redeems flares.
   *
   * @param player Player who redeemed flares.
   * @param tier   Tier of flares redeemed.
   * @param amount Amount of flares redeemed.
   */
  public static void sendRedeem(Player player, String tier, int amount) {
    String message = replaceTierAmount(redeem, tier, amount);
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends message for when player tries to redeem flares with a full inventory.
   *
   * @param player Player who redeemed flares with a full inventory.
   */
  public static void sendRedeemFull(Player player) {
    player.sendMessage(applyColor(redeemFull));
  }

  /**
   * Sends message for when CommandSender tries to start an active envoy.
   *
   * @param sender CommandSender who tries to start an active envoy.
   */
  public static void sendEnvoyNoSummon(CommandSender sender) {
    sender.sendMessage(applyColor(envoyNoSummon));
  }

  /**
   * Sends message for when player summons an envoy.
   *
   * @param sender CommandSender who summoned the envoy.
   * @param tier   Tier of envoy summoned.
   * @param remain Number of chests in envoy summoned.
   * @param time   Duration of envoy summoned.
   */
  public static void sendEnvoySummon(CommandSender sender, String tier, int remain,
      List<Integer> time) {
    if (sender == null) {
      return;
    }
    for (String line : envoySummon) {
      String message = replaceEnvoyTierRemainETime(line, tier, remain, time);
      sender.sendMessage(applyColor(message));
    }
  }

  /**
   * Sends message for when envoy starts.
   * <p>Sends to all players if no player summoned the envoy.</p>
   *
   * @param player Player who summoned the envoy.
   * @param tier   Tier of envoy started.
   * @param remain Number of chests in envoy started.
   * @param time   Duration of envoy started.
   */
  public static void sendEnvoyStart(Player player, String tier, int remain, List<Integer> time) {
    if (!EnvoyConfig.getEnvoyDoAnnounce(tier)) {
      return;
    }
    int range = EnvoyConfig.getEnvoyAnnounceRange(tier);
    for (String line : envoyStart) {
      String message = replaceEnvoyPlayerTierRemainETime(line, player, tier, remain, time);
      for (Player other : getPlayersInRange(player, range)) {
        if (other.equals(player)) {
          continue;
        }
        other.sendMessage(applyColor(message));
      }
    }
  }

  /**
   * Sends message for when player claims an envoy crate.
   *
   * @param player Player who claimed envoy crate.
   * @param tier   Tier of envoy crate claimed.
   * @param remain Number of crates remaining in the envoy.
   */
  public static void sendEnvoyClaim(Player player, String tier, int remain) {
    String message = replaceTierRemain(envoyClaim, tier, remain);
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends message to all players for when a player claims an envoy crate.
   *
   * @param player Player who claimed envoy crate.
   * @param tier   Tier of envoy crate claimed.
   * @param remain Number of crates remaining in the envoy.
   * @param loc    Location of crate claimed.
   */
  public static void sendEnvoyClaimAll(Player player, String tier, int remain, Location loc) {
    if (!EnvoyConfig.getEnvoyDoAnnounce(tier)) {
      return;
    }
    int range = EnvoyConfig.getEnvoyAnnounceRange(tier);
    String message = replacePlayerTierRemainLoc(envoyClaimAll, player, tier, remain, loc);
    for (Player other : getPlayersInRange(player, range)) {
      if (other.equals(player)) {
        continue;
      }
      other.sendMessage(applyColor(message));
    }
  }

  /**
   * Sends message to all players for when an envoy has ended.
   *
   * @param tier     Tier of envoy that ended.
   * @param cooldown Cooldown to next envoy of specified type.
   */
  public static void sendEnvoyEnd(String tier, List<Integer> cooldown) {
    if (!EnvoyConfig.getEnvoyDoAnnounce(tier)) {
      return;
    }
    int range = EnvoyConfig.getEnvoyAnnounceRange(tier);
    String message = replaceEnvoyTierETime(envoyEnd, tier, cooldown);
    for (Player player : getPlayersInRange((Player) null, range)) {
      player.sendMessage(applyColor(message));
    }
  }

  /**
   * Sends message for when CommandSender tries to stop a stopped envoy.
   *
   * @param sender CommandSender who tried to end a stopped envoy.
   */
  public static void sendEnvoyNoEnd(CommandSender sender) {
    sender.sendMessage(applyColor(envoyNoEnd));
  }

  /**
   * Sends message to CommandSender who queries envoy status during the envoy.
   *
   * @param sender   CommandSender that sent query command.
   * @param remain   Number of crates remaining in envoy.
   * @param duration Duration remaining in the envoy.
   */
  public static void sendEnvoyRemain(CommandSender sender, int remain, List<Integer> duration) {
    String message = replaceRemainETime(envoyRemain, remain, duration);
    sender.sendMessage(applyColor(message));
  }

  /**
   * Sends message to CommandSender who queries envoy status during envoy downtime.
   *
   * @param sender   CommandSender who sent query command.
   * @param tier     Tier of envoy queried.
   * @param cooldown Cooldown to next envoy of speicified type.
   */
  public static void sendEnvoyCooldown(CommandSender sender, String tier, List<Integer> cooldown) {
    String message = replaceEnvoyTierETime(envoyCooldown, tier, cooldown);
    sender.sendMessage(applyColor(message));
  }

  /**
   * Sends message to CommandSender who queries envoy status for a non-cooldown envoy during
   * downtime.
   *
   * @param sender CommandSender who sent query command.
   * @param tier   Tier of envoy queried.
   */
  public static void sendEnvoyNoCooldown(CommandSender sender, String tier) {
    String message = replaceEnvoyTier(envoyNoCooldown, tier);
    sender.sendMessage(applyColor(message));
  }

  /**
   * Sends message to CommandSender who queries envoy status for an envoy that doesn't exist.
   *
   * @param sender CommandSender who sent query command.
   */
  public static void sendEnvoyNoExist(CommandSender sender) {
    sender.sendMessage(applyColor(envoyNoExist));
  }

  /**
   * Sends message to CommandSender who queries a list of all envoys.
   *
   * @param sender CommandSender who sent list command.
   */
  public static void sendEnvoyList(CommandSender sender) {
    String message = envoyList + EnvoyConfig.getEnvoyNamesList();
    sender.sendMessage(applyColor(message));
  }

  /**
   * Sends confirmation message for when envoy flare give command is successfully used.
   *
   * @param sender CommandSender to send confirmation message to.
   * @param player Player who was successfully given envoy flares.
   * @param tier   Tier of envoy flares given to player.
   * @param amount Amount of envoy flares given to player.
   */
  public static void sendEnvoyGive(CommandSender sender, Player player, String tier, int amount) {
    String message = replacePlayerEnvoyTierAmount(envoyGive, player, tier, amount);
    sender.sendMessage(applyColor(message));
  }

  /**
   * Sends message for when flares are sent to redeems because inventory is full.
   *
   * @param sender CommandSender to send message to.
   * @param player Player who was successfully given flares.
   * @param amount Amount of flares sent to redeems inventory.
   */
  public static void sendEnvoyGiveFull(CommandSender sender, Player player, int amount) {
    String message = replacePlayerAmount(envoyGiveFull, player, amount);
    sender.sendMessage(applyColor(message));
  }

  /**
   * Sends confirmation message for when flares are successfully received by player.
   *
   * @param player Player to send confirmation message to.
   * @param tier   Tier of flares given to player.
   * @param amount Amount of flares given to player.
   */
  public static void sendEnvoyReceive(Player player, String tier, int amount) {
    String message = replaceEnvoyTierAmount(envoyReceive, tier, amount);
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends message for when flares are sent to redeems because inventory is full.
   *
   * @param player Player to send message to.
   * @param amount Amount of flares sent to redeems inventory.
   */
  public static void sendEnvoyReceiveFull(Player player, int amount) {
    String message = envoyReceiveFull.replaceAll("%amount%", String.valueOf(amount));
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends confirmation message for when player redeems envoy flares.
   *
   * @param player Player who redeemed envoy flares.
   * @param tier   Tier of envoy flares redeemed.
   * @param amount Amount of envoy flares redeemed.
   */
  public static void sendEnvoyRedeem(Player player, String tier, int amount) {
    String message = replaceEnvoyTierAmount(envoyRedeem, tier, amount);
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends message for when player tries to edit a different envoy while editing an envoy.
   *
   * @param player Player who tries to edit multiple envoys
   */
  public static void sendEnvoyEditEditing(Player player) {
    player.sendMessage(applyColor(envoyEditEditing));
  }

  /**
   * Sends confirmation message for when player starts editing an envoy.
   *
   * @param player Player who started editing envoy.
   * @param tier   Type of envoy being edited.
   */
  public static void sendEnvoyEditStart(Player player, String tier) {
    String message = replaceEnvoyTier(envoyEditStart, tier);
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends confirmation message for when player stops editing an envoy.
   *
   * @param player Player who stopped editing envoy.
   * @param tier   Tier of envoy that player stopped editing.
   */
  public static void sendEnvoyEditStop(Player player, String tier) {
    String message = replaceEnvoyTier(envoyEditStop, tier);
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends confirmation message for when player successfully adds a new location to envoy.
   *
   * @param player Player who added new location to envoy.
   */
  public static void sendEnvoyEditSave(Player player) {
    player.sendMessage(applyColor(envoyEditSave));
  }

  /**
   * Sends message for when player tries to save an envoy location that isn't an air block.
   *
   * @param player   Player who tried to save location.
   * @param location Location that is not an air block.
   */
  public static void sendEnvoyEditNotAir(Player player, Location location) {
    String message = envoyEditNotAir.replaceAll("%loc%", getLocation(location));
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends message for when envoy location could not be saved.
   *
   * @param player Player to send message to.
   */
  public static void sendEnvoyEditNoSave(Player player) {
    player.sendMessage(applyColor(envoyEditNoSave));
  }

  /**
   * Sends confirmation message for when player successfully deletes a location from the envoy.
   *
   * @param player Player who deleted location from envoy.
   */
  public static void sendEnvoyEditDelete(Player player) {
    player.sendMessage(applyColor(envoyEditDelete));
  }

  private static String replaceCmd(String line, String cmd) {
    return line.replaceAll("%cmd%", cmd);
  }

  private static String replaceLoc(String line, Location loc) {
    return line.replaceAll("%loc%", getLocation(loc));
  }

  public static String replacePlayer(String line, Player player) {
    return line.replaceAll("%player%", getPlayerName(player));
  }

  private static String replaceCmdType(String line, String cmd, String type) {
    String message = replaceCmd(line, cmd);
    return message.replaceAll("%type%", type);
  }

  public static String replaceEnvoyTier(String line, String tier) {
    return line.replaceAll("%tier%", EnvoyConfig.getEnvoyName(tier));
  }

  private static String replaceTierLoc(String line, String tier, Location loc) {
    String message = line.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    return message.replaceAll("%loc%", getLocation(loc));
  }

  private static String replaceTierAmount(String line, String tier, int amount) {
    String message = line.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    return message.replaceAll("%amount%", String.valueOf(amount));
  }

  private static String replaceTierRemain(String line, String tier, int remain) {
    String message = line.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    return message.replaceAll("%remain%", String.valueOf(remain));
  }

  private static String replacePlayerAmount(String line, Player player, int amount) {
    String message = line.replaceAll("%player%", getPlayerName(player));
    return message.replaceAll("%amount%", String.valueOf(amount));
  }

  private static String replaceRemainETime(String line, int remain, List<Integer> etime) {
    String message = line.replaceAll("%remain%", String.valueOf(remain));
    return message.replaceAll("%e_time%", getETime(etime));
  }

  private static String replaceEnvoyTierAmount(String line, String tier, int amount) {
    String message = line.replaceAll("%tier%", EnvoyConfig.getEnvoyName(tier));
    return message.replaceAll("%amount%", String.valueOf(amount));
  }

  private static String replaceEnvoyTierETime(String line, String tier, List<Integer> etime) {
    String message = line.replaceAll("%tier%", EnvoyConfig.getEnvoyName(tier));
    return message.replaceAll("%e_time%", getETime(etime));
  }

  private static String replaceTierTimeLoc(String line, String tier, int time, Location loc) {
    String message = line.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    message = message.replaceAll("%time%", String.valueOf(time));
    return message.replaceAll("%loc%", getLocation(loc));
  }

  private static String replaceEnvoyTierRemainETime(String line, String tier, int remain,
      List<Integer> etime) {
    String message = line.replaceAll("%tier%", EnvoyConfig.getEnvoyName(tier));
    message = message.replaceAll("%remain%", String.valueOf(remain));
    return message.replaceAll("%e_time%", getETime(etime));
  }

  private static String replacePlayerTierLoc(String line, Player player, String tier,
      Location loc) {
    String message = line.replaceAll("%player%", getPlayerName(player));
    message = message.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    return message.replaceAll("%loc%", String.valueOf(loc));
  }

  private static String replacePlayerTierAmount(String line, Player player, String tier,
      int amount) {
    String message = line.replaceAll("%player%", getPlayerName(player));
    message = message.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    return message.replaceAll("%amount%", String.valueOf(amount));
  }

  private static String replacePlayerEnvoyTierAmount(String line, Player player, String tier,
      int amount) {
    String message = line.replaceAll("%player%", getPlayerName(player));
    message = message.replaceAll("%tier%", EnvoyConfig.getEnvoyName(tier));
    return message.replaceAll("%amount%", String.valueOf(amount));
  }

  private static String replacePlayerTierTimeLoc(String line, Player player, String tier, int time,
      Location loc) {
    String message = line.replaceAll("%player%", getPlayerName(player));
    message = message.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    message = message.replaceAll("%time%", String.valueOf(time));
    message = message.replaceAll("%loc%", getLocation(loc));
    return message;
  }

  private static String replacePlayerTierRemainLoc(String line, Player player, String tier,
      int remain, Location loc) {
    String message = line.replaceAll("%player%", getPlayerName(player));
    message = message.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    message = message.replaceAll("%remain%", String.valueOf(remain));
    return message.replaceAll("%loc%", getLocation(loc));
  }

  private static String replaceEnvoyPlayerTierRemainETime(String line, Player player, String tier,
      int remain, List<Integer> etime) {
    String message = line.replaceAll("%tier%", EnvoyConfig.getEnvoyName(tier));
    if (player != null) {
      message = line.replaceAll("%player%", getPlayerName(player));
    }
    message = message.replaceAll("%remain%", String.valueOf(remain));
    return message.replaceAll("%e_time%", getETime(etime));
  }

  private static Collection<? extends Player> getPlayersInRange(Location location, int range) {
    if (range == -1) {
      return Bukkit.getOnlinePlayers();
    }
    Set<Player> result = new HashSet<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (!player.getWorld().equals(location.getWorld())) {
        continue;
      }
      if (player.getLocation().distance(location) > range) {
        continue;
      }
      result.add(player);
    }
    return result;
  }

  private static Collection<? extends Player> getPlayersInRange(Player player, int range) {
    if (player == null) {
      return Bukkit.getOnlinePlayers();
    }
    return getPlayersInRange(player.getLocation(), range);
  }
}
