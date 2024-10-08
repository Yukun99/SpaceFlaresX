package me.yukun.spaceflares;

import java.util.Objects;
import me.yukun.spaceflares.command.envoy.EnvoyCommandManager;
import me.yukun.spaceflares.command.flare.FlareCommandManager;
import me.yukun.spaceflares.config.FileManager;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.envoy.Envoy;
import me.yukun.spaceflares.envoy.EnvoyFlareUseListener;
import me.yukun.spaceflares.envoy.edit.EnvoyEditListener;
import me.yukun.spaceflares.envoy.edit.EnvoyEditor;
import me.yukun.spaceflares.flare.Flare;
import me.yukun.spaceflares.flare.events.CrateClickListener;
import me.yukun.spaceflares.flare.events.FlareFireworkListener;
import me.yukun.spaceflares.flare.events.FlareLandListener;
import me.yukun.spaceflares.flare.events.FlareUseListener;
import me.yukun.spaceflares.flare.events.RewardGUIListener;
import me.yukun.spaceflares.integration.hologram.HologramSupportManager;
import me.yukun.spaceflares.integration.region.RegionSupportManager;
import me.yukun.spaceflares.redeem.RedeemGUI;
import me.yukun.spaceflares.redeem.RedeemGUIListener;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SpaceFlares extends JavaPlugin implements Listener {

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
    Objects.requireNonNull(getCommand("spaceflares")).setExecutor(new FlareCommandManager());
    Objects.requireNonNull(getCommand("envoy")).setExecutor(new EnvoyCommandManager());
    pm.registerEvents(new RedeemGUIListener(), this);
    pm.registerEvents(new FlareUseListener(), this);
    pm.registerEvents(new FlareFireworkListener(), this);
    pm.registerEvents(new FlareLandListener(), this);
    pm.registerEvents(new CrateClickListener(), this);
    pm.registerEvents(new RewardGUIListener(), this);
    pm.registerEvents(new EnvoyFlareUseListener(), this);
    pm.registerEvents(new EnvoyEditListener(), this);
  }

  public static Plugin getPlugin() {
    return JavaPlugin.getPlugin(SpaceFlares.class);
  }

  @Override
  public void onDisable() {
    if (isConfigErrored) {
      return;
    }
    FileManager.onDisable();
    RedeemGUI.onDisable();
    Flare.onDisable();
    Envoy.onDisable();
    EnvoyEditor.onDisable();
  }

  @EventHandler
  private void DevJoinEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!player.getName().equals("xu_yukun")) {
      return;
    }
    Messages.sendPluginVersion(player, this);
    if (RegionSupportManager.hasWorldGuard()) {
      Messages.sendIntegrationEnabled(player, "WorldGuard");
    }
    if (RegionSupportManager.hasSaberFactions()) {
      Messages.sendIntegrationEnabled(player, "Factions");
    }
    if (HologramSupportManager.hasCMIHolograms()) {
      Messages.sendIntegrationEnabled(player, "CMI");
    }
    if (HologramSupportManager.hasDecentHolograms()) {
      Messages.sendIntegrationEnabled(player, "DecentHolograms");
    }
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