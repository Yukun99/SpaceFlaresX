package me.yukun.spaceflares.gui.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.yukun.spaceflares.SpaceFlares;
import me.yukun.spaceflares.config.CrateConfig;
import me.yukun.spaceflares.gui.RewardGUI;
import me.yukun.spaceflares.gui.event.RewardGUIEmptyEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class RewardGUIListener implements Listener {

  private static final Map<Inventory, RewardGUIEmptyEvent> guiEmptyEventMap = new HashMap<>();
  private static final Map<Inventory, Set<Player>> guiViewerMap = new HashMap<>();

  public static void registerGUIEvent(Inventory gui, RewardGUIEmptyEvent e) {
    guiEmptyEventMap.put(gui, e);
    guiViewerMap.put(gui, new HashSet<>());
  }

  public static void deregisterGUIEvent(Inventory gui) {
    Bukkit.getPluginManager().callEvent(guiEmptyEventMap.get(gui));
    guiEmptyEventMap.remove(gui);
    for (Player player : guiViewerMap.get(gui)) {
      player.closeInventory();
    }
    guiViewerMap.remove(gui);
  }

  @EventHandler
  private void rewardGUIOpenEvent(InventoryOpenEvent e) {
    if (!isRewardGUIOpenEvent(e)) {
      return;
    }
    Player player = (Player) e.getPlayer();
    guiViewerMap.get(e.getInventory()).add(player);
  }

  private boolean isRewardGUIOpenEvent(InventoryOpenEvent e) {
    Inventory opened = e.getInventory();
    return guiEmptyEventMap.containsKey(opened);
  }

  @EventHandler
  private void rewardGUICloseEvent(InventoryCloseEvent e) {
    if (!isRewardGUICloseEvent(e)) {
      return;
    }
    Player player = (Player) e.getPlayer();
    Inventory gui = e.getInventory();
    guiViewerMap.get(gui).remove(player);
    String crate = RewardGUI.getCrateTier(gui);
    if (CrateConfig.getCrateDoFastExit(crate)) {
      RewardGUI.rewardGUIFastDropContents(gui, player);
    }
  }

  private boolean isRewardGUICloseEvent(InventoryCloseEvent e) {
    Inventory closed = e.getInventory();
    if (!guiEmptyEventMap.containsKey(closed)) {
      return false;
    }
    Player player = (Player) e.getPlayer();
    return guiViewerMap.get(closed).contains(player);
  }

  @EventHandler
  private void rewardGUIEmptyEvent(InventoryClickEvent e) {
    if (!isRewardGUIEmptyEvent(e)) {
      return;
    }
    scheduleForDeregister(e.getInventory());
  }

  private boolean isRewardGUIEmptyEvent(InventoryClickEvent e) {
    Inventory open = e.getClickedInventory();
    return guiEmptyEventMap.containsKey(open);
  }

  private void scheduleForDeregister(Inventory inventory) {
    Bukkit.getScheduler().scheduleSyncDelayedTask(SpaceFlares.getPlugin(), () -> {
      if (inventory.isEmpty()) {
        deregisterGUIEvent(inventory);
      }
    }, 1);
  }
}
