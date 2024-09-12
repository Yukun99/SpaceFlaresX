package me.yukun.spaceflares.command;

import me.yukun.spaceflares.config.FileManager;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.envoy.Envoy;
import me.yukun.spaceflares.envoy.edit.EnvoyEditor;
import me.yukun.spaceflares.flare.Flare;
import me.yukun.spaceflares.redeem.RedeemGUI;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends AbstractCommand {

  public ReloadCommand(CommandSender sender) {
    super(sender);
  }

  @Override
  public boolean execute() {
    FileManager.reload();
    RedeemGUI.reload();
    Flare.reload();
    Envoy.reload();
    EnvoyEditor.reload();
    Messages.sendReloadSuccess(sender);
    if (sender instanceof Player player) {
      player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    }
    return true;
  }
}
