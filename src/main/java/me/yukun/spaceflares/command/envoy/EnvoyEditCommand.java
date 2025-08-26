package me.yukun.spaceflares.command.envoy;

import me.yukun.spaceflares.command.AbstractCommand;
import me.yukun.spaceflares.config.EnvoyConfig;
import me.yukun.spaceflares.envoy.edit.EnvoyEditor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnvoyEditCommand extends AbstractCommand {

  private final Player player;
  private final String envoy;

  private EnvoyEditCommand(CommandSender sender, Player player, String envoy) {
    super(sender);
    this.player = player;
    this.envoy = envoy;
  }

  public static AbstractCommand parseCommand(CommandSender sender, String envoy) {
    if (!(sender instanceof Player player)) {
      return getDefaultHelpCommand(sender);
    }
    if (!EnvoyConfig.isEnvoy(envoy)) {
      return getDefaultHelpCommand(sender);
    }
    return new EnvoyEditCommand(sender, player, envoy);
  }

  @Override
  public boolean execute() {
    EnvoyEditor.toggleEditing(envoy, player);
    return true;
  }
}
