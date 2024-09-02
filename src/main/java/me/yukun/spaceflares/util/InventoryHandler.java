package me.yukun.spaceflares.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
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
    Collection<ItemStack> overflows = player.getInventory().addItem(item).values();
    if (overflows.isEmpty()) {
      return null;
    }
    ItemStack remainItem = null;
    for (ItemStack overflow : overflows) {
      remainItem = overflow.clone();
    }
    return remainItem;
  }

  /**
   * Gets items held by player in main/off hand.
   * <p>Only gets main hand item for versions below 1.9.</p>
   *
   * @param player Player to get held items for.
   * @return List of items held by player, first item being mainhand and second item being offhand.
   */
  @SuppressWarnings("deprecation")
  public static List<ItemStack> getItemsInHand(Player player) {
    List<ItemStack> items = new ArrayList<>();
    if (getVersion() >= 191) {
      items.add(player.getInventory().getItemInMainHand());
      items.add(player.getInventory().getItemInOffHand());
    } else {
      items.add(player.getItemInHand());
    }
    return items;
  }

  /**
   * Simple version name to int converter.
   *
   * @return Version number as a simple integer.
   */
  private static Integer getVersion() {
    String ver = Bukkit.getServer().getVersion();
    ver = ver.split("-")[0];
    ver = ver.replaceAll("\\.", "");
    return Integer.parseInt(ver);
  }
}
