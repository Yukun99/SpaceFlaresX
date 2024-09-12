package me.yukun.spaceflares.command.flare;

import me.yukun.spaceflares.command.AbstractCommand;
import me.yukun.spaceflares.command.HelpCommand;
import me.yukun.spaceflares.config.FlareConfig;
import me.yukun.spaceflares.flare.Flare;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlareSummonCommand extends AbstractCommand {

  private final String flare;
  private final Player player;
  private final Location location;

  private FlareSummonCommand(CommandSender sender, String flare, Player player, int x, int y,
      int z) {
    super(sender);
    this.flare = flare;
    this.player = player;
    World world = player.getWorld();
    this.location = new Location(world, x, y, z);
  }

  @Override
  public boolean execute() {
    new Flare(flare, player, location);
    return true;
  }

  public static AbstractCommand parseSummonCommand(CommandSender sender, String[] args) {
    if (!(sender instanceof Player player)) {
      return new HelpCommand(sender, true);
    }
    if (!FlareConfig.isFlare(args[1])) {
      return new HelpCommand(sender, true);
    }
    if (!isValidCoord(args[2]) || !isValidCoord(args[3]) || !isValidCoord(args[4])) {
      return new HelpCommand(sender, true);
    }
    String flare = args[1];
    int x = getCoord(args[2], player, CoordType.X);
    int y = getCoord(args[3], player, CoordType.Y);
    int z = getCoord(args[4], player, CoordType.Z);
    return new FlareSummonCommand(sender, flare, player, x, y, z);
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
