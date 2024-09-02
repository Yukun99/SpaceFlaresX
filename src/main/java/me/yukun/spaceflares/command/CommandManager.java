package me.yukun.spaceflares.command;

import me.yukun.spaceflares.config.Config;
import me.yukun.spaceflares.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandManager implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof ConsoleCommandSender) && !Config.hasCommandPermissions(sender)) {
      Messages.sendNoPermission(sender);
      return false;
    }
    SpaceFlaresCommand captchaCommand = new HelpCommand(sender);
    switch (args.length) {
      case 1 -> {
        if (args[0].equals("reload")) {
          captchaCommand = new ReloadCommand(sender);
        }
        if (args[0].equals("redeem")) {
          captchaCommand = RedeemCommand.parseRedeemCommand(sender);
        }
      }
      case 2, 3, 4 -> {
        if (args[0].equals("give")) {
          captchaCommand = GiveCommand.parseGiveCommand(sender, args);
        }
      }
    }
    return captchaCommand.execute();
  }
}
