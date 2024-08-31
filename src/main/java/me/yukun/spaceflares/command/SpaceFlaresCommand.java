package me.yukun.spaceflares.command;

import org.bukkit.command.CommandSender;

public class SpaceFlaresCommand {

  protected CommandSender sender;

  public SpaceFlaresCommand(CommandSender sender) {
    this.sender = sender;
  }

  /**
   * Executes the command.
   */
  public boolean execute() {
    return false;
  }

  protected static boolean isInt(String string) {
    try {
      Integer.parseInt(string);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }
}
