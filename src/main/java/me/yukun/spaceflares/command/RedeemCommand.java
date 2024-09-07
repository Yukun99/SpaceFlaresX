package me.yukun.spaceflares.command;

import me.yukun.spaceflares.redeem.RedeemGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RedeemCommand extends SpaceFlaresCommand {

  private final Player player;

  private RedeemCommand(CommandSender sender) {
    super(sender);
    player = (Player) sender;
  }

  public static SpaceFlaresCommand parseRedeemCommand(CommandSender sender) {
    if (!(sender instanceof Player)) {
      return new HelpCommand(sender);
    }
    return new RedeemCommand(sender);
  }

  @Override
  public boolean execute() {
    new RedeemGUI(player);
    return true;
  }
}
