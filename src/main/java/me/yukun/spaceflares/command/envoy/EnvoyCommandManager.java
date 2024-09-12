package me.yukun.spaceflares.command.envoy;

import me.yukun.spaceflares.command.AbstractCommand;
import me.yukun.spaceflares.command.CommandManager;
import me.yukun.spaceflares.command.CommandTypeEnum;
import me.yukun.spaceflares.command.HelpCommand;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.util.Pair;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EnvoyCommandManager extends CommandManager implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender,
      @NotNull org.bukkit.command.Command command,
      @NotNull String label, @NotNull String[] args) {
    AbstractCommand envoyCmd = new HelpCommand(sender, false);
    boolean hasPermission = false;
    switch (args.length) {
      case 1 -> {
        Pair<AbstractCommand, Boolean> simpleCmd = parseSimpleCommand(sender, args[0], false);
        envoyCmd = simpleCmd.key();
        hasPermission = simpleCmd.value();
      }
      case 2 -> {
        if (args[0].equals("query")) {
          hasPermission = hasCommandPermissions(sender, CommandTypeEnum.QUERY);
          envoyCmd = new EnvoyQueryCommand(sender, args[1]);
        }
        if (args[0].equals("edit")) {
          hasPermission = hasCommandPermissions(sender, CommandTypeEnum.EDIT);
          envoyCmd = EnvoyEditCommand.parseCommand(sender, args[1]);
        }
        if (args[0].equals("start")) {
          hasPermission = hasCommandPermissions(sender, CommandTypeEnum.START);
          envoyCmd = EnvoyStartCommand.parseCommand(sender, args[1]);
        }
        if (args[0].equals("stop")) {
          hasPermission = hasCommandPermissions(sender, CommandTypeEnum.STOP);
          envoyCmd = EnvoyStopCommand.parseCommand(sender, args[1]);
        }
        if (args[0].equals("give")) {
          hasPermission = hasCommandPermissions(sender, CommandTypeEnum.GIVE);
          envoyCmd = EnvoyGiveCommand.parseCommand(sender, args);
        }
      }
      case 3, 4 -> {
        if (args[0].equals("give")) {
          hasPermission = hasCommandPermissions(sender, CommandTypeEnum.GIVE);
          envoyCmd = EnvoyGiveCommand.parseCommand(sender, args);
        }
      }
    }
    if (envoyCmd instanceof HelpCommand) {
      hasPermission = hasCommandPermissions(sender, CommandTypeEnum.HELP);
    }
    if (!hasPermission) {
      Messages.sendNoPermission(sender);
      return false;
    }
    return envoyCmd.execute();
  }
}
