package me.yukun.spaceflares.envoy;

import me.yukun.spaceflares.config.EnvoyConfig;
import me.yukun.spaceflares.config.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EnvoyFlareUseListener implements Listener {

  @EventHandler
  private void flareUseEvent(PlayerInteractEvent e) {
    if (!isEnvoyFlareUseEvent(e)) {
      return;
    }
    Player player = e.getPlayer();
    String type = getEnvoyFromHeldItems(player);
    if (type == null) {
      return;
    }
    e.setCancelled(true);
    if (Envoy.playerSummonEnvoy(player, type)) {
      Messages.sendEnvoyNoSummon(player);
    }
  }

  /**
   * Gets envoy flare type held by specified player.
   *
   * @param player Player to get held envoy flare type for.
   * @return Type of envoy flare specified player is holding.
   */
  private String getEnvoyFromHeldItems(Player player) {
    ItemStack main = player.getInventory().getItemInMainHand();
    ItemStack off = player.getInventory().getItemInOffHand();
    if (EnvoyConfig.getEnvoyFlareFromItem(main) != null) {
      return EnvoyConfig.getEnvoyFlareFromItem(main);
    }
    return EnvoyConfig.getEnvoyFlareFromItem(off);
  }

  /**
   * Checks if interact event can be a flare use event.
   *
   * @param e Event to be checked.
   * @return True if interact event can be a flare use event, false otherwise.
   */
  private boolean isEnvoyFlareUseEvent(PlayerInteractEvent e) {
    return e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction()
        .equals(Action.RIGHT_CLICK_BLOCK);
  }
}
