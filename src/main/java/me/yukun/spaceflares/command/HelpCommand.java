package me.yukun.spaceflares.command;

import me.yukun.spaceflares.config.Messages;
import org.bukkit.command.CommandSender;

public class HelpCommand extends SpaceFlaresCommand {

  public HelpCommand(CommandSender sender) {
    super(sender);
  }

  @Override
  public boolean execute() {
    Messages.sendHelp(sender);
    return false;
  }
}
