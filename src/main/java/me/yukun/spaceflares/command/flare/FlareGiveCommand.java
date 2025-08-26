package me.yukun.spaceflares.command.flare;

import me.yukun.spaceflares.command.AbstractCommand;
import me.yukun.spaceflares.command.HelpCommand;
import me.yukun.spaceflares.config.FlareConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.util.InventoryHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FlareGiveCommand extends AbstractCommand {

  private final Player player;
  private final String flare;
  private int amount = 1;

  private FlareGiveCommand(CommandSender sender, Player player, String flare, int amount) {
    super(sender);
    this.player = player;
    this.flare = flare;
    this.amount = amount;
  }

  private FlareGiveCommand(CommandSender sender, Player player, String flare) {
    super(sender);
    this.player = player;
    this.flare = flare;
  }

  @Override
  public boolean execute() {
    Messages.sendGive(sender, player, flare, amount);
    ItemStack remain = InventoryHandler.giveFlare(player, flare, amount);
    if (remain != null) {
      Messages.sendGiveFull(sender, player, remain.getAmount());
      return false;
    }
    return true;
  }

  public static AbstractCommand parseGiveCommand(CommandSender sender, String[] args) {
    return switch (args.length) {
      case 2 -> parse2ArgCommand(sender, args);
      case 3 -> parse3ArgCommand(sender, args);
      case 4 -> parse4ArgCommand(sender, args);
      default -> new HelpCommand(sender, true);
    };
  }

  private static AbstractCommand parse2ArgCommand(CommandSender sender, String[] args) {
    if (!FlareConfig.isFlare(args[1]) || !(sender instanceof Player)) {
      return getDefaultHelpCommand(sender);
    }
    return new FlareGiveCommand(sender, (Player) sender, args[1]);
  }

  private static AbstractCommand parse3ArgCommand(CommandSender sender, String[] args) {
    if (!FlareConfig.isFlare(args[1]) && Bukkit.getPlayer(args[1]) == null) {
      return getDefaultHelpCommand(sender);
    }
    if (FlareConfig.isFlare(args[1])) {
      if (!isValidAmount(args[2])) {
        return getDefaultHelpCommand(sender);
      }
      int amount = Integer.parseInt(args[2]);
      return new FlareGiveCommand(sender, (Player) sender, args[1], amount);
    }
    if (Bukkit.getPlayer(args[1]) != null) {
      if (!FlareConfig.isFlare(args[2])) {
        return getDefaultHelpCommand(sender);
      }
      Player player = Bukkit.getPlayer(args[1]);
      return new FlareGiveCommand(sender, player, args[2]);
    }
    return getDefaultHelpCommand(sender);
  }

  private static AbstractCommand parse4ArgCommand(CommandSender sender, String[] args) {
    if (Bukkit.getPlayer(args[1]) == null || !FlareConfig.isFlare(args[2]) || !isValidAmount(
        args[3])) {
      return getDefaultHelpCommand(sender);
    }
    Player player = Bukkit.getPlayer(args[1]);
    int amount = Integer.parseInt(args[3]);
    return new FlareGiveCommand(sender, player, args[2], amount);
  }
}
