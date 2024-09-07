package me.yukun.spaceflares.flare.events;

import me.yukun.spaceflares.config.FlareConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.flare.Flare;
import me.yukun.spaceflares.integration.SupportManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FlareUseListener implements Listener {

  @EventHandler
  private void flareUseEvent(PlayerInteractEvent e) {
    if (!isFlareUseEvent(e)) {
      return;
    }
    Player player = e.getPlayer();
    String type = getFlareFromHeldItems(player);
    if (type == null) {
      return;
    }
    e.setCancelled(true);
    if (!SupportManager.canSpawnFlare(player, type)) {
      Messages.sendNoSummon(player);
      return;
    }
    new Flare(type, player);
  }

  /**
   * Gets flare type held by specified player.
   *
   * @param player Player to get held flare type for.
   * @return Type of flare specified player is holding.
   */
  private String getFlareFromHeldItems(Player player) {
    ItemStack main = player.getInventory().getItemInMainHand();
    ItemStack off = player.getInventory().getItemInOffHand();
    if (FlareConfig.getFlareFromItem(main) != null) {
      return FlareConfig.getFlareFromItem(main);
    }
    return FlareConfig.getFlareFromItem(off);
  }

  /**
   * Checks if interact event can be a flare use event.
   *
   * @param e Event to be checked.
   * @return True if interact event can be a flare use event, false otherwise.
   */
  private boolean isFlareUseEvent(PlayerInteractEvent e) {
    return e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction()
        .equals(Action.RIGHT_CLICK_BLOCK);
  }
}
