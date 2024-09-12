package me.yukun.spaceflares.command;

import org.bukkit.command.CommandSender;

public abstract class AbstractCommand {

  protected final CommandSender sender;

  public AbstractCommand(CommandSender sender) {
    this.sender = sender;
  }

  /**
   * Executes the command.
   */
  public abstract boolean execute();

  protected static boolean isInt(String string) {
    try {
      Integer.parseInt(string);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  protected static boolean isValidAmount(String argument) {
    if (!isInt(argument)) {
      return false;
    }
    int amount = Integer.parseInt(argument);
    return amount > 0 && amount <= 64;
  }
}
