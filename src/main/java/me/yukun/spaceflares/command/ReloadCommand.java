package me.yukun.spaceflares.command;

import me.yukun.spaceflares.config.FileManager;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.crate.CrateClickListener;
import me.yukun.spaceflares.flare.FlareLandListener;
import me.yukun.spaceflares.gui.RedeemGUI;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends SpaceFlaresCommand {

  public ReloadCommand(CommandSender sender) {
    super(sender);
  }

  @Override
  public boolean execute() {
    FileManager.reload();
    RedeemGUI.onReload();
    FlareLandListener.onReload();
    CrateClickListener.onReload();
    Messages.sendReloadSuccess(sender);
    if (sender instanceof Player player) {
      player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    }
    return true;
  }
}
