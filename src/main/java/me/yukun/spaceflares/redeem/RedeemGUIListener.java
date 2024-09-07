package me.yukun.spaceflares.redeem;

import static me.yukun.spaceflares.util.InventoryHandler.tryAddItems;

import java.util.Set;
import me.yukun.spaceflares.config.FlareConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.config.Redeems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
    Player player = (Player) e.getWhoClicked();
    ItemStack given = e.getCurrentItem();
    ItemStack remain = giveFlares(player, given);
    int reduceAmount = remain == null ? given.getAmount() : given.getAmount() - remain.getAmount();
    String flare = FlareConfig.getFlareFromItem(given);
    Redeems.removeRedeems(player, flare, reduceAmount);
    RedeemGUI clicked = RedeemGUI.getClickedRedeemGUI(e.getClickedInventory());
    clicked.reconvertRedeems(player);
    clicked.refreshGUI();
  }

  private ItemStack giveFlares(Player player, ItemStack flareItem) {
    ItemStack given = flareItem.clone();
    ItemStack remainItem = tryAddItems(player, given);
    String flare = FlareConfig.getFlareFromItem(given);
    Messages.sendRedeem(player, flare, given.getAmount());
    if (remainItem != null) {
      Messages.sendRedeemFull(player);
    }
    return remainItem;
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
