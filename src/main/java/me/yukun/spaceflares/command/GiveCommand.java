package me.yukun.spaceflares.command;

import me.yukun.spaceflares.config.FlareConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.util.InventoryHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand extends SpaceFlaresCommand {

  private final Player player;
  private final String flare;
  private int amount = 1;

  private GiveCommand(CommandSender sender, Player player, String flare, int amount) {
    super(sender);
    this.player = player;
    this.flare = flare;
    this.amount = amount;
  }

  private GiveCommand(CommandSender sender, Player player, String flare) {
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
      Messages.sendReceiveFull(player, remain.getAmount());
      return false;
    }
    return true;
  }

  // spaceflares give player tier amount
  public static SpaceFlaresCommand parseGiveCommand(CommandSender sender, String[] args) {
    switch (args.length) {
      case 2:
        if (!FlareConfig.isFlare(args[1]) || !(sender instanceof Player)) {
          break;
        }
        return new GiveCommand(sender, (Player) sender, args[1]);
      case 3:
        if (!FlareConfig.isFlare(args[1]) && Bukkit.getPlayer(args[1]) == null) {
          break;
        }
        if (FlareConfig.isFlare(args[1])) {
          if (!isValidAmount(args[2])) {
            break;
          }
          int amount = Integer.parseInt(args[2]);
          return new GiveCommand(sender, (Player) sender, args[1], amount);
        }
        if (Bukkit.getPlayer(args[1]) != null) {
          if (!FlareConfig.isFlare(args[2])) {
            break;
          }
          Player player = Bukkit.getPlayer(args[1]);
          return new GiveCommand(sender, player, args[2]);
        }
        break;
      case 4:
        if (Bukkit.getPlayer(args[1]) == null || !FlareConfig.isFlare(args[2]) || !isValidAmount(
            args[3])) {
          break;
        }
        Player player = Bukkit.getPlayer(args[1]);
        int amount = Integer.parseInt(args[3]);
        return new GiveCommand(sender, player, args[2], amount);
    }
    return new HelpCommand(sender);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private static boolean isValidAmount(String argument) {
    if (!isInt(argument)) {
      return false;
    }
    int amount = Integer.parseInt(argument);
    return amount > 0 && amount <= 64;
  }
}
