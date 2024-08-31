package me.yukun.spaceflares;

import java.util.Objects;
import me.yukun.spaceflares.command.CommandManager;
import me.yukun.spaceflares.config.FileManager;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.gui.RedeemGUI;
import me.yukun.spaceflares.gui.RedeemGUIListener;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

  private boolean isConfigErrored = false;

  @Override
  public void onEnable() {
    isConfigErrored = !FileManager.onEnable(this);
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(this, this);
    if (isConfigErrored) {
      Bukkit.getPluginManager().disablePlugin(this);
      return;
    }
    Objects.requireNonNull(getCommand("spaceflares")).setExecutor(new CommandManager());
    pm.registerEvents(new RedeemGUIListener(), this);
  }

  @Override
  public void onDisable() {
    if (isConfigErrored) {
      return;
    }
    FileManager.onDisable();
    RedeemGUI.onDisable();
  }

  @EventHandler
  private void DevJoinEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!player.getName().equals("xu_yukun")) {
      return;
    }
    Messages.sendPluginVersion(player, this);
    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    if (isConfigErrored) {
      Messages.sendConfigError(player);
      player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
    }
  }

  @EventHandler
  private void AdminJoinEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!player.isOp() && !player.hasPermission("captchas.admin")) {
      return;
    }
    if (isConfigErrored) {
      Messages.sendConfigError(player);
    }
  }
}