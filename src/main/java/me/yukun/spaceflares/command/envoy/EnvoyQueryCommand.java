package me.yukun.spaceflares.command.envoy;

import me.yukun.spaceflares.command.AbstractCommand;
import me.yukun.spaceflares.config.EnvoyConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.envoy.Envoy;
import org.bukkit.command.CommandSender;

public class EnvoyQueryCommand extends AbstractCommand {

  private final String type;

  public EnvoyQueryCommand(CommandSender sender, String type) {
    super(sender);
    this.type = type;
  }

  @Override
  public boolean execute() {
    if (!EnvoyConfig.isEnvoy(type)) {
      Messages.sendEnvoyNoExist(sender);
      return false;
    }
    if (Envoy.isActive(type)) {
      Messages.sendEnvoyRemain(sender, Envoy.getEnvoyRemain(type), Envoy.getEnvoyDuration(type));
      return true;
    }
    if (EnvoyConfig.doEnvoyCooldownEnable(type)) {
      Messages.sendEnvoyCooldown(sender, type, Envoy.getEnvoyCooldown(type));
    } else {
      Messages.sendEnvoyNoCooldown(sender, type);
    }
    return true;
  }
}
