package me.yukun.spaceflares.util;

import java.util.Collection;
import me.yukun.spaceflares.config.EnvoyConfig;
import me.yukun.spaceflares.config.FlareConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.config.Redeems;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryHandler {

  public static ItemStack giveEnvoyFlare(Player player, String type, int amount) {
    ItemStack given = EnvoyConfig.getEnvoyFlareItem(type).clone();
    given.setAmount(amount);
    ItemStack remainItem = tryAddItems(player, given);
    Messages.sendEnvoyReceive(player, type, given.getAmount());
    if (remainItem != null) {
      int remain = remainItem.getAmount();
      Redeems.addEnvoyFlareRedeems(player, type, remain);
      Messages.sendEnvoyReceiveFull(player, remain);
    }
    return remainItem;
  }

  /**
   * Tries to give flares of specified tier and amount to specified player.
   * <p>Excess flares will be sent to the redeems inventory.</p>
   *
   * @param player Player to give flares to.
   * @param type   Type of flare to give.
   * @param amount Amount of flares to give.
   */
  public static ItemStack giveFlare(Player player, String type, int amount) {
    ItemStack given = FlareConfig.getFlareItem(type).clone();
    given.setAmount(amount);
    ItemStack remainItem = tryAddItems(player, given);
    Messages.sendReceive(player, type, given.getAmount());
    if (remainItem != null) {
      int remain = remainItem.getAmount();
      Redeems.addFlareRedeems(player, type, remain);
      Messages.sendReceiveFull(player, remain);
    }
    return remainItem;
  }

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
}
