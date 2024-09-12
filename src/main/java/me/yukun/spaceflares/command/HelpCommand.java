package me.yukun.spaceflares.command;

import me.yukun.spaceflares.config.Messages;
import org.bukkit.command.CommandSender;

public class HelpCommand extends AbstractCommand {

  private final boolean isFlareCommand;

  public HelpCommand(CommandSender sender, boolean isFlareCommand) {
    super(sender);
    this.isFlareCommand = isFlareCommand;
  }

  @Override
  public boolean execute() {
    Messages.sendHelp(sender, isFlareCommand);
    return false;
  }
}
