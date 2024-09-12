package me.yukun.spaceflares.command.flare;

import me.yukun.spaceflares.command.AbstractCommand;
import me.yukun.spaceflares.command.CommandManager;
import me.yukun.spaceflares.command.CommandTypeEnum;
import me.yukun.spaceflares.command.HelpCommand;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.util.Pair;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class FlareCommandManager extends CommandManager implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender,
      @NotNull org.bukkit.command.Command command,
      @NotNull String label, @NotNull String[] args) {
    AbstractCommand flareCmd = new HelpCommand(sender, true);
    boolean hasPermission = false;
    switch (args.length) {
      case 1 -> {
        Pair<AbstractCommand, Boolean> simpleCmd = parseSimpleCommand(sender, args[0], true);
        flareCmd = simpleCmd.key();
        hasPermission = simpleCmd.value();
      }
      case 2, 3, 4 -> {
        if (args[0].equals("give")) {
          hasPermission = hasCommandPermissions(sender, CommandTypeEnum.GIVE);
          flareCmd = FlareGiveCommand.parseGiveCommand(sender, args);
        }
      }
      case 5 -> {
        if (args[0].equals("summon")) {
          hasPermission = hasCommandPermissions(sender, CommandTypeEnum.SUMMON);
          flareCmd = FlareSummonCommand.parseSummonCommand(sender, args);
        }
      }
    }
    if (flareCmd instanceof HelpCommand) {
      hasPermission = hasCommandPermissions(sender, CommandTypeEnum.HELP);
    }
    if (!hasPermission) {
      Messages.sendNoPermission(sender);
      return false;
    }
    return flareCmd.execute();
  }
}
