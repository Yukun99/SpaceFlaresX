package me.yukun.spaceflares.command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import me.yukun.spaceflares.command.envoy.EnvoyListCommand;
import me.yukun.spaceflares.util.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class CommandManager {

  protected static final Map<CommandTypeEnum, BiFunction<CommandSender, CommandTypeEnum, Boolean>> commandPermissorMap = new HashMap<>() {{
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
    put(CommandTypeEnum.LIST, (sender, command) -> {
      if (hasAdminPermissions(sender)) {
        return true;
      }
      return sender.hasPermission("spaceflares.list");
    });
    put(CommandTypeEnum.QUERY, (sender, command) -> {
      if (hasAdminPermissions(sender)) {
        return true;
      }
      return sender.hasPermission("spaceflares.query");
    });
    put(CommandTypeEnum.EDIT, (sender, command) -> {
      if (hasAdminPermissions(sender)) {
        return true;
      }
      return sender.hasPermission("spaceflares.edit");
    });
    put(CommandTypeEnum.START, (sender, command) -> {
      if (hasAdminPermissions(sender)) {
        return true;
      }
      return sender.hasPermission("spaceflares.start");
    });
    put(CommandTypeEnum.STOP, (sender, command) -> {
      if (hasAdminPermissions(sender)) {
        return true;
      }
      return sender.hasPermission("spaceflares.stop");
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

  protected static Pair<AbstractCommand, Boolean> parseSimpleCommand(CommandSender sender,
      String name, boolean isFlareCommand) {
    AbstractCommand command = new HelpCommand(sender, isFlareCommand);
    boolean hasPermission = false;
    if (name.equals("reload")) {
      hasPermission = hasCommandPermissions(sender, CommandTypeEnum.RELOAD);
      command = new ReloadCommand(sender);
    }
    if (name.equals("redeem")) {
      hasPermission = hasCommandPermissions(sender, CommandTypeEnum.REDEEM);
      command = RedeemCommand.parseRedeemCommand(sender, isFlareCommand);
    }
    if (name.equals("list")) {
      hasPermission = hasCommandPermissions(sender, CommandTypeEnum.LIST);
      command = new EnvoyListCommand(sender);
    }
    return new Pair<>(command, hasPermission);
  }
}
