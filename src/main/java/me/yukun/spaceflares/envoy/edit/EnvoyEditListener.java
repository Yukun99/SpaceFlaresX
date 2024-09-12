package me.yukun.spaceflares.envoy.edit;

import me.yukun.spaceflares.config.EnvoyConfig;
import me.yukun.spaceflares.config.Messages;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class EnvoyEditListener implements Listener {

  @EventHandler
  private void envoyAddLocationEvent(BlockPlaceEvent e) {
    Player player = e.getPlayer();
    String envoy = EnvoyEditor.getEditedEnvoy(player);
    if (envoy == null) {
      return;
    }
    Location location = e.getBlock().getLocation();
    Location saved = EnvoyConfig.saveEnvoyLocation(envoy, player, location);
    if (saved == null) {
      e.setCancelled(true);
      return;
    }
    EnvoyEditor.addEnvoyLocation(envoy, saved, location);
    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    Messages.sendEnvoyEditSave(player);
  }

  @EventHandler
  private void envoyRemoveLocationEvent(BlockBreakEvent e) {
    Player player = e.getPlayer();
    String envoy = EnvoyEditor.getEditedEnvoy(player);
    if (envoy == null) {
      return;
    }
    Location location = e.getBlock().getLocation();
    Integer index = EnvoyEditor.getEnvoyLocationIndex(envoy, location);
    if (index == null) {
      e.setCancelled(true);
      return;
    }
    if (!EnvoyConfig.deleteEnvoyLocation(envoy, index)) {
      e.setCancelled(true);
      return;
    }
    EnvoyEditor.removeEnvoyLocation(envoy, location, index);
    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    Messages.sendEnvoyEditDelete(player);
  }

  @EventHandler
  private void envoyEditorChangeSlotEvent(PlayerItemHeldEvent e) {
    Player player = e.getPlayer();
    String envoy = EnvoyEditor.getEditedEnvoy(player);
    if (envoy == null) {
      return;
    }
    e.setCancelled(true);
  }

  @EventHandler
  private void envoyEditorChangeHandEvent(PlayerSwapHandItemsEvent e) {
    Player player = e.getPlayer();
    String envoy = EnvoyEditor.getEditedEnvoy(player);
    if (envoy == null) {
      return;
    }
    e.setCancelled(true);
  }
}
