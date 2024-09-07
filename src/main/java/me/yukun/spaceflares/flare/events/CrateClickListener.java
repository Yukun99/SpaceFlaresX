package me.yukun.spaceflares.flare.events;

import me.yukun.spaceflares.flare.Flare;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CrateClickListener implements Listener {

  @EventHandler
  private void crateClickEvent(PlayerInteractEvent e) {
    if (!isCrateClickEvent(e)) {
      return;
    }
    Flare flare = getFlareFromBlock(e.getClickedBlock());
    if (flare == null) {
      return;
    }
    e.setCancelled(true);
    Player player = e.getPlayer();
    flare.rewardPlayer(player, e.getAction());
  }

  private boolean isCrateClickEvent(PlayerInteractEvent e) {
    return e.getAction().equals(Action.LEFT_CLICK_BLOCK)
        || e.getAction().equals(Action.RIGHT_CLICK_BLOCK);
  }

  private Flare getFlareFromBlock(Block block) {
    return Flare.getClickedFlare(block);
  }
}
