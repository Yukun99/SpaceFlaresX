package me.yukun.spaceflares.redeem;

import java.util.Set;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;

public class RedeemGUIListener implements Listener {

  @EventHandler
  private void pageTurnClickEvent(InventoryClickEvent e) {
    if (!RedeemGUI.isRedeemGUIClickEvent(e) || !RedeemGUI.isPageTurnClickEvent(e)) {
      return;
    }
    e.setCancelled(true);
    int slot = e.getSlot();
    RedeemGUI clicked = RedeemGUI.getClickedRedeemGUI(e.getClickedInventory());
    // next page
    if (slot == RedeemGUI.getNextPageSlot()) {
      clicked.nextPage();
    }
    // previous page
    if (slot == RedeemGUI.getPreviousPageSlot()) {
      clicked.prevPage();
    }
  }

  @EventHandler
  private void flareClickEvent(InventoryClickEvent e) {
    if (!RedeemGUI.isRedeemGUIClickEvent(e) || RedeemGUI.isPageTurnClickEvent(e)) {
      return;
    }
    e.setCancelled(true);
    if (e.getCurrentItem() == null) {
      return;
    }
    RedeemGUI clicked = RedeemGUI.getClickedRedeemGUI(e.getClickedInventory());
    clicked.redeemFlare(e.getCurrentItem());
    clicked.reconvertRedeems();
    clicked.refreshGUI();
  }

  @EventHandler
  private void blockItemInsertEvent(InventoryMoveItemEvent e) {
    if (!RedeemGUI.isRedeemGUIMoveItemEvent(e)) {
      return;
    }
    e.setCancelled(true);
  }

  @EventHandler
  private void blockShiftClickEvent(InventoryClickEvent e) {
    // If player is trying to shift click items into GUI, cancel
    Inventory other = e.getView().getTopInventory();
    if (RedeemGUI.getClickedRedeemGUI(other) == null || !e.getClick().isShiftClick()) {
      return;
    }
    e.setCancelled(true);
  }

  @EventHandler
  private void blockDragClickEvent(InventoryDragEvent e) {
    // If player is trying to drag items into GUI, cancel
    Set<Integer> slots = e.getRawSlots();
    Inventory other = e.getWhoClicked().getOpenInventory().getTopInventory();
    if (RedeemGUI.getClickedRedeemGUI(other) == null) {
      return;
    }
    boolean doCancel = false;
    for (int slot : slots) {
      if (slot >= 0 && slot < RedeemGUI.getNextPageSlot()) {
        doCancel = true;
      }
    }
    if (doCancel) {
      e.setCancelled(true);
    }
  }
}
