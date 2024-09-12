package me.yukun.spaceflares.command.envoy;

import me.yukun.spaceflares.command.AbstractCommand;
import me.yukun.spaceflares.command.HelpCommand;
import me.yukun.spaceflares.config.EnvoyConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.envoy.Envoy;
import org.bukkit.command.CommandSender;

public class EnvoyStartCommand extends AbstractCommand {

  private final String envoy;

  public EnvoyStartCommand(CommandSender sender, String envoy) {
    super(sender);
    this.envoy = envoy;
  }

  public static AbstractCommand parseCommand(CommandSender sender, String envoy) {
    if (!EnvoyConfig.isEnvoy(envoy)) {
      return new HelpCommand(sender, false);
    }
    return new EnvoyStartCommand(sender, envoy);
  }

  @Override
  public boolean execute() {
    if (Envoy.senderSummonEnvoy(sender, envoy) != null) {
      return true;
    }
    Messages.sendEnvoyNoSummon(sender);
    return false;
  }
}
