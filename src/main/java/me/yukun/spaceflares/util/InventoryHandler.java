package me.yukun.spaceflares.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryHandler {

  /**
   * Tries to add specified item to player's inventory.
   *
   * @param player Player to give specified item to.
   * @param item   Item to be given to specified player.
   * @return Items that could not be added to player's inventory due to fullness, null if not full.
   */
  public static ItemStack tryAddItems(Player player, ItemStack item) {
    // Empty slot found, add item.
    if (player.getInventory().firstEmpty() != -1) {
      player.getInventory().addItem(item);
      return null;
    }
    int remain = item.getAmount();
    for (ItemStack invenItem : player.getInventory().getContents()) {
      // Check if item needs to be added.
      if (remain == 0) {
        return null;
      }
      // Check if inventory item exists and is similar.
      if (invenItem == null || !invenItem.isSimilar(item)) {
        continue;
      }
      // Item is similar, get possible added amount.
      int empty = 64 - invenItem.getAmount();
      int added = Math.min(remain, empty);
      ItemStack addedItem = item.clone();
      addedItem.setAmount(added);
      // Add item.
      player.getInventory().addItem(addedItem);
      // Reduce remaining amount.
      remain -= added;
    }
    item.setAmount(remain);
    return item;
  }
}
