package me.yukun.spaceflares.flare.events;

import me.yukun.spaceflares.flare.Flare;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class RewardGUIListener implements Listener {

  @EventHandler
  private void rewardGUICloseEvent(InventoryCloseEvent e) {
    Flare flare = getFlareFromInventory(e.getInventory());
    if (flare == null) {
      return;
    }
    flare.rewardClosedPlayer((Player) e.getPlayer());
  }

  @EventHandler
  private void rewardGUIEmptyEvent(InventoryClickEvent e) {
    Inventory inventory = e.getInventory();
    if (!inventory.isEmpty()) {
      return;
    }
    Flare flare = getFlareFromInventory(inventory);
    if (flare == null) {
      return;
    }
    flare.endCrate();
  }

  private Flare getFlareFromInventory(Inventory inventory) {
    return Flare.getClosedFlare(inventory);
  }
}
