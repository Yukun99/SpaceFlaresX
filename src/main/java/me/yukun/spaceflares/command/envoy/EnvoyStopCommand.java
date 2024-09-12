package me.yukun.spaceflares.command.envoy;

import me.yukun.spaceflares.command.AbstractCommand;
import me.yukun.spaceflares.command.HelpCommand;
import me.yukun.spaceflares.config.EnvoyConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.envoy.Envoy;
import org.bukkit.command.CommandSender;

public class EnvoyStopCommand extends AbstractCommand {

  private final String envoy;

  public EnvoyStopCommand(CommandSender sender, String envoy) {
    super(sender);
    this.envoy = envoy;
  }

  public static AbstractCommand parseCommand(CommandSender sender, String envoy) {
    if (!EnvoyConfig.isEnvoy(envoy)) {
      return new HelpCommand(sender, false);
    }
    return new EnvoyStopCommand(sender, envoy);
  }

  @Override
  public boolean execute() {
    if (Envoy.stopEnvoy(envoy)) {
      return true;
    }
    Messages.sendEnvoyNoEnd(sender);
    return false;
  }
}
