package me.yukun.spaceflares.command.envoy;

import me.yukun.spaceflares.command.AbstractCommand;
import me.yukun.spaceflares.config.Messages;
import org.bukkit.command.CommandSender;

public class EnvoyListCommand extends AbstractCommand {

  public EnvoyListCommand(CommandSender sender) {
    super(sender);
  }

  @Override
  public boolean execute() {
    Messages.sendEnvoyList(sender);
    return true;
  }
}
