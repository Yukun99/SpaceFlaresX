package me.yukun.spaceflares.config;

import static me.yukun.spaceflares.util.TextFormatter.applyColor;

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
  private static final String HELP_HEADER = "&b&l===============SpaceFlares===============";
  private static final String HELP_COMMANDS = "&b&l----------Commands----------";
  private static final String HELP_ALIASES = "Command aliases: spaceflares, spaceflare, spacef, sflare, sf";
  private static final String HELP_HELP = "/spaceflares help: Shows commands, aliases and permissions.";
  private static final String HELP_REDEEM = "/spaceflares redeem: Opens flare redeeming GUI.";
  private static final String HELP_GIVE1 = "/spaceflares give (player) (flare) (amount): Give player (amount) flares of specified tier.";
  private static final String HELP_GIVE2 = "/spaceflares give (player) (flare): Give player 1 flare of specified tier.";
  private static final String HELP_GIVE3 = "/spaceflares give (flare) (amount): Give yourself (amount) flares of specified tier.";
  private static final String HELP_GIVE4 = "/spaceflares give (flare): Give yourself 1 flare of specified tier.";
  private static final String HELP_SUMMON1 = "/spaceflares summon (flare) (x) (y) (z): Summons flare of specified tier at specified coordinates.";
  private static final String HELP_SUMMON2 = "/spaceflares summon (flare) ~ ~ ~: Summons flare of specified tier at own coordinates.";
  private static final String HELP_SUMMON3 = "/spaceflares summon (flare) ~x ~y ~z: Summons flare of specified tier at own coordinates with offsets.";
  private static final String HELP_SUMMON4 = "  - Any of the above coordinate formats can be mixed, including negative numbers.";
  private static final String HELP_SUMMON5 = "  - Example: '/spaceflares summon Example 1 ~ ~-24' is a valid command.";
  private static final String HELP_RELOAD = "/spaceflares reload: Reloads all configuration files.";
  private static final String HELP_PERMISSIONS = "&b&l----------Permissions----------";
  private static final String HELP_WILDCARD_P = "spaceflares.*: All permissions combined";
  private static final String HELP_ADMIN_P = "spaceflares.admin: Ability to use commands";
  private static final String HELP_HELP_P = "spaceflares.help: Ability to use help command.";
  private static final String HELP_REDEEM_P = "spaceflares.redeem: Ability to use redeem command.";
  private static final String HELP_GIVE_P = "spaceflares.give: Ability to use give command.";
  private static final String HELP_SUMMON_P = "spaceflares.summon: Ability to use summon command.";
  private static final String HELP_RELOAD_P = "spaceflares.reload: Ability to use reload command.";
  private static final String HELP_FOOTER = "&b&l======================================";
  private static final String RELOAD_SUCCESS = "&aReload successful!";
  private static final String NO_PERMISSION = "&cYou do not have permission to use the command!";
  // Prefix appended before all messages.
  private static String prefix = "&bSpace&eFlares&f >> &7";
  // Placeholder formats.
  private static String locP;
  private static String playerP;
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

  protected static void setup(FileConfiguration config) {
    setupStrings(config);
    setupStringLists(config);
  }

  private static void setupStrings(FileConfiguration config) {
    prefix = config.getString("Prefix");
    locP = config.getString("Placeholder.Loc");
    playerP = config.getString("Placeholder.Player");
    give = prefix + config.getString("Give");
    giveFull = prefix + config.getString("GiveFull");
    receive = prefix + config.getString("Receive");
    receiveFull = prefix + config.getString("ReceiveFull");
    noSummon = prefix + config.getString("NoSummon");
    despawnNotify = prefix + config.getString("Despawn.Notify");
    despawnItems = config.getString("Despawn.Items");
    redeem = prefix + config.getString("Redeem");
    redeemFull = prefix + config.getString("RedeemFull");
  }

  private static void setupStringLists(FileConfiguration config) {
    summon = config.getStringList("Summon");
    summonAll = config.getStringList("SummonAll");
    land = config.getStringList("Land");
    landAll = config.getStringList("LandAll");
    claim = config.getStringList("Claim");
    claimAll = config.getStringList("ClaimAll");
    despawnAll = config.getStringList("Despawn");
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
  public static void sendHelp(CommandSender sender) {
    sender.sendMessage(applyColor(HELP_HEADER));
    sender.sendMessage(applyColor(HELP_COMMANDS));
    sender.sendMessage(applyColor(HELP_ALIASES));
    if (CommandManager.hasCommandPermissions(sender, CommandTypeEnum.HELP)) {
      sender.sendMessage(applyColor(HELP_HELP));
    }
    if (CommandManager.hasCommandPermissions(sender, CommandTypeEnum.REDEEM)) {
      sender.sendMessage(applyColor(HELP_REDEEM));
    }
    if (CommandManager.hasCommandPermissions(sender, CommandTypeEnum.GIVE)) {
      sender.sendMessage(applyColor(HELP_GIVE1));
      sender.sendMessage(applyColor(HELP_GIVE2));
      sender.sendMessage(applyColor(HELP_GIVE3));
      sender.sendMessage(applyColor(HELP_GIVE4));
    }
    if (CommandManager.hasCommandPermissions(sender, CommandTypeEnum.SUMMON)) {
      sender.sendMessage(applyColor(HELP_SUMMON1));
      sender.sendMessage(applyColor(HELP_SUMMON2));
      sender.sendMessage(applyColor(HELP_SUMMON3));
      sender.sendMessage(applyColor(HELP_SUMMON4));
      sender.sendMessage(applyColor(HELP_SUMMON5));
    }
    if (CommandManager.hasCommandPermissions(sender, CommandTypeEnum.RELOAD)) {
      sender.sendMessage(applyColor(HELP_RELOAD));
    }
    if (CommandManager.hasAdminPermissions(sender)) {
      sender.sendMessage(applyColor(HELP_PERMISSIONS));
      sender.sendMessage(applyColor(HELP_WILDCARD_P));
      sender.sendMessage(applyColor(HELP_ADMIN_P));
      sender.sendMessage(applyColor(HELP_HELP_P));
      sender.sendMessage(applyColor(HELP_REDEEM_P));
      sender.sendMessage(applyColor(HELP_GIVE_P));
      sender.sendMessage(applyColor(HELP_SUMMON_P));
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
   * Sends confirmation message for when flare give command is successfully used.
   *
   * @param sender CommandSender to send confirmation message to.
   * @param player Player who was successfully given flares.
   * @param tier   Tier of flares given to player.
   * @param amount Amount of flares given to player.
   */
  public static void sendGive(CommandSender sender, Player player, String tier, int amount) {
    String message = give.replaceAll("%player%", getPlayerName(player));
    message = message.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    message = message.replaceAll("%amount%", String.valueOf(amount));
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
    String message = giveFull.replaceAll("%player%", getPlayerName(player));
    message = message.replaceAll("%amount%", String.valueOf(amount));
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
    String message = receive.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    message = message.replaceAll("%amount%", String.valueOf(amount));
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

  private static String replaceLoc(String line, Location loc) {
    return line.replaceAll("%loc%", getLocation(loc));
  }

  public static String replacePlayer(String line, Player player) {
    return line.replaceAll("%player%", getPlayerName(player));
  }

  private static String replaceTierLoc(String line, String tier, Location loc) {
    String message = line.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    message = message.replaceAll("%loc%", getLocation(loc));
    return message;
  }

  private static String replaceTierAmount(String line, String tier, int amount) {
    String message = line.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    message = message.replaceAll("%amount%", String.valueOf(amount));
    return message;
  }

  private static String replaceTierTimeLoc(String line, String tier, int time, Location loc) {
    String message = line.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    message = message.replaceAll("%time%", String.valueOf(time));
    message = message.replaceAll("%loc%", getLocation(loc));
    return message;
  }

  private static String replacePlayerTierLoc(String line, Player player, String tier,
      Location loc) {
    String message = line.replaceAll("%player%", getPlayerName(player));
    message = message.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    message = message.replaceAll("%loc%", String.valueOf(loc));
    return message;
  }

  private static String replacePlayerTierTimeLoc(String line, Player player, String tier, int time,
      Location loc) {
    String message = line.replaceAll("%player%", getPlayerName(player));
    message = message.replaceAll("%tier%", FlareConfig.getFlareTier(tier));
    message = message.replaceAll("%time%", String.valueOf(time));
    message = message.replaceAll("%loc%", getLocation(loc));
    return message;
  }

  private static Set<Player> getPlayersInRange(Location location, int range) {
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
}
