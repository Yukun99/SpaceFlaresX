package me.yukun.spaceflares.command;

import me.yukun.spaceflares.redeem.RedeemGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RedeemCommand extends AbstractCommand {

  private final Player player;
  private final boolean isFlareCommand;

  private RedeemCommand(CommandSender sender, boolean isFlareCommand) {
    super(sender);
    this.isFlareCommand = isFlareCommand;
    player = (Player) sender;
  }

  public static AbstractCommand parseRedeemCommand(CommandSender sender, boolean isFlareCommand) {
    if (!(sender instanceof Player)) {
      return new HelpCommand(sender, isFlareCommand);
    }
    return new RedeemCommand(sender, isFlareCommand);
  }

  @Override
  public boolean execute() {
    new RedeemGUI(player, isFlareCommand);
    return true;
  }
}
