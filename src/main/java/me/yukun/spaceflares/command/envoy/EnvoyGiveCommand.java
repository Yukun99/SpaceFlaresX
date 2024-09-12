package me.yukun.spaceflares.command.envoy;

import me.yukun.spaceflares.command.AbstractCommand;
import me.yukun.spaceflares.command.HelpCommand;
import me.yukun.spaceflares.config.EnvoyConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.util.InventoryHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnvoyGiveCommand extends AbstractCommand {

  private final Player player;
  private final String envoy;
  private int amount = 1;

  private EnvoyGiveCommand(CommandSender sender, Player player, String envoy, int amount) {
    super(sender);
    this.player = player;
    this.envoy = envoy;
    this.amount = amount;
  }

  private EnvoyGiveCommand(CommandSender sender, Player player, String envoy) {
    super(sender);
    this.player = player;
    this.envoy = envoy;
  }

  @Override
  public boolean execute() {
    Messages.sendEnvoyGive(sender, player, envoy, amount);
    ItemStack remain = InventoryHandler.giveEnvoyFlare(player, envoy, amount);
    if (remain != null) {
      Messages.sendEnvoyGiveFull(sender, player, remain.getAmount());
      return false;
    }
    return true;
  }

  public static AbstractCommand parseCommand(CommandSender sender, String[] args) {
    switch (args.length) {
      case 2:
        if (!EnvoyConfig.isEnvoy(args[1]) || !(sender instanceof Player)) {
          break;
        }
        return new EnvoyGiveCommand(sender, (Player) sender, args[1]);
      case 3:
        if (!EnvoyConfig.isEnvoy(args[1]) && Bukkit.getPlayer(args[1]) == null) {
          break;
        }
        if (EnvoyConfig.isEnvoy(args[1])) {
          if (!isValidAmount(args[2])) {
            break;
          }
          int amount = Integer.parseInt(args[2]);
          return new EnvoyGiveCommand(sender, (Player) sender, args[1], amount);
        }
        if (Bukkit.getPlayer(args[1]) != null) {
          if (!EnvoyConfig.isEnvoy(args[2])) {
            break;
          }
          Player player = Bukkit.getPlayer(args[1]);
          return new EnvoyGiveCommand(sender, player, args[2]);
        }
        break;
      case 4:
        if (Bukkit.getPlayer(args[1]) == null || !EnvoyConfig.isEnvoy(args[2]) || !isValidAmount(
            args[3])) {
          break;
        }
        Player player = Bukkit.getPlayer(args[1]);
        int amount = Integer.parseInt(args[3]);
        return new EnvoyGiveCommand(sender, player, args[2], amount);
    }
    return new HelpCommand(sender, false);
  }
}
