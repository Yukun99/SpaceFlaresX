package me.yukun.spaceflares.command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
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
    SpaceFlaresCommand captchaCommand = new HelpCommand(sender);
    boolean hasPermission = false;
    switch (args.length) {
      case 1 -> {
        if (args[0].equals("reload")) {
          hasPermission = hasCommandPermissions(sender, CommandTypeEnum.RELOAD);
          captchaCommand = new ReloadCommand(sender);
        }
        if (args[0].equals("redeem")) {
          hasPermission = hasCommandPermissions(sender, CommandTypeEnum.REDEEM);
          captchaCommand = RedeemCommand.parseRedeemCommand(sender);
        }
      }
      case 2, 3, 4 -> {
        if (args[0].equals("give")) {
          hasPermission = hasCommandPermissions(sender, CommandTypeEnum.GIVE);
          captchaCommand = GiveCommand.parseGiveCommand(sender, args);
        }
      }
      case 5 -> {
        if (args[0].equals("summon")) {
          hasPermission = hasCommandPermissions(sender, CommandTypeEnum.SUMMON);
          captchaCommand = SummonCommand.parseSummonCommand(sender, args);
        }
      }
    }
    if (captchaCommand instanceof HelpCommand) {
      hasPermission = hasCommandPermissions(sender, CommandTypeEnum.HELP);
    }
    if (!hasPermission) {
      Messages.sendNoPermission(sender);
      return false;
    }
    return captchaCommand.execute();
  }

  private static final Map<CommandTypeEnum, BiFunction<CommandSender, CommandTypeEnum, Boolean>> commandPermissorMap = new HashMap<>() {{
    put(CommandTypeEnum.GIVE, (sender, command) -> {
      if (hasAdminPermissions(sender)) {
        return true;
      }
      return sender.hasPermission("spaceflares.give");
    });
    put(CommandTypeEnum.HELP, (sender, command) -> {
      if (hasAdminPermissions(sender)) {
        return true;
      }
      return sender.hasPermission("spaceflares.help");
    });
    put(CommandTypeEnum.REDEEM, (sender, command) -> {
      if (hasAdminPermissions(sender)) {
        return true;
      }
      return sender.hasPermission("spaceflares.redeem");
    });
    put(CommandTypeEnum.RELOAD, (sender, command) -> {
      if (hasAdminPermissions(sender)) {
        return true;
      }
      return sender.hasPermission("spaceflares.reload");
    });
    put(CommandTypeEnum.SUMMON, (sender, command) -> {
      if (hasAdminPermissions(sender)) {
        return true;
      }
      return sender.hasPermission("spaceflares.summon");
    });
  }};

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public static boolean hasCommandPermissions(CommandSender sender, CommandTypeEnum command) {
    if (sender instanceof ConsoleCommandSender) {
      return true;
    }
    return commandPermissorMap.get(command).apply(sender, command);
  }

  public static boolean hasAdminPermissions(CommandSender sender) {
    return sender.hasPermission("spaceflares.*") || sender.hasPermission("spaceflares.admin");
  }
}
