package me.yukun.spaceflares.command;

import me.yukun.spaceflares.config.FlareConfig;
import me.yukun.spaceflares.flare.FlareUseListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SummonCommand extends SpaceFlaresCommand {

  private final Player player;
  private final String flare;
  private final Location location;

  private SummonCommand(CommandSender sender, Player player, String flare, int x, int y, int z) {
    super(sender);
    this.player = player;
    this.flare = flare;
    World world = player.getWorld();
    this.location = new Location(world, x, y, z);
  }

  @Override
  public boolean execute() {
    FlareUseListener.summonFlare(player, flare, location);
    return true;
  }

  public static SpaceFlaresCommand parseSummonCommand(CommandSender sender, String[] args) {
    if (!(sender instanceof Player player)) {
      return new HelpCommand(sender);
    }
    if (!FlareConfig.isFlare(args[1])) {
      return new HelpCommand(sender);
    }
    if (!isValidCoord(args[2]) || !isValidCoord(args[3]) || !isValidCoord(args[4])) {
      return new HelpCommand(sender);
    }
    String flare = args[1];
    int x = getCoord(args[2], player, CoordType.X);
    int y = getCoord(args[3], player, CoordType.Y);
    int z = getCoord(args[4], player, CoordType.Z);
    return new SummonCommand(sender, player, flare, x, y, z);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private static boolean isValidCoord(String arg) {
    if (isInt(arg)) {
      return true;
    }
    if (arg.equals("~")) {
      return true;
    }
    String stripped = arg.replaceFirst("~", "");
    return isInt(stripped);
  }

  private static int getCoord(String arg, Player player, CoordType type) {
    if (isInt(arg)) {
      return Integer.parseInt(arg);
    }
    if (arg.equals("~")) {
      return switch (type) {
        case X -> player.getLocation().getBlockX();
        case Y -> player.getLocation().getBlockY();
        case Z -> player.getLocation().getBlockZ();
      };
    }
    String stripped = arg.replaceFirst("~", "");
    int strippedNum = Integer.parseInt(stripped);
    return switch (type) {
      case X -> player.getLocation().getBlockX() + strippedNum;
      case Y -> player.getLocation().getBlockY() + strippedNum;
      case Z -> player.getLocation().getBlockZ() + strippedNum;
    };
  }

  private enum CoordType {
    X,
    Y,
    Z
  }
}
