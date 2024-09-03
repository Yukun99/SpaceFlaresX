package me.yukun.spaceflares.flare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.yukun.spaceflares.Main;
import me.yukun.spaceflares.config.CrateConfig;
import me.yukun.spaceflares.config.FlareConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.integration.SupportManager;
import me.yukun.spaceflares.util.Fireworks;
import me.yukun.spaceflares.util.InventoryHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FlareUseListener implements Listener {

  private static final Map<FallingBlock, Integer> blockTimerMap = new HashMap<>();

  private static void startFireworks(FallingBlock fallingBlock, String flare) {
    int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
      Location loc = fallingBlock.getLocation();
      Firework firework = Fireworks.spawnFirework(loc, flare);
      FlareFireworkListener.registerFirework(firework);
    }, 0, 5);
    blockTimerMap.put(fallingBlock, task);
  }

  public static void stopFireworks(FallingBlock fallingBlock) {
    int task = blockTimerMap.get(fallingBlock);
    Bukkit.getScheduler().cancelTask(task);
    blockTimerMap.remove(fallingBlock);
  }

  @EventHandler
  private void flareUseEvent(PlayerInteractEvent e) {
    if (!isFlareUseEvent(e)) {
      return;
    }
    e.setCancelled(true);
    Player player = e.getPlayer();
    List<ItemStack> heldItems = InventoryHandler.getItemsInHand(player);
    boolean isMainFlare = FlareConfig.getFlareFromItem(heldItems.get(0)) != null;
    String flare = isMainFlare
        ? FlareConfig.getFlareFromItem(heldItems.get(0))
        : FlareConfig.getFlareFromItem(heldItems.get(1));
    if (!SupportManager.canSpawnFlare(player, flare)) {
      Messages.sendNoSummon(player);
      return;
    }
    if (isMainFlare) {
      heldItems.get(0).setAmount(heldItems.get(0).getAmount() - 1);
    } else {
      heldItems.get(1).setAmount(heldItems.get(1).getAmount() - 1);
    }
    Location location = FlareConfig.getFlareSpawnLocation(flare, player);
    BlockData flareBlockData = CrateConfig.getCrateBlock(flare);
    FallingBlock flareBlock = Objects.requireNonNull(location.getWorld())
        .spawnFallingBlock(location, flareBlockData);
    startFireworks(flareBlock, flare);
    FlareLandListener.registerFallingBlock(flareBlock, flare, player);
    Messages.sendSummon(player, flare, location);
    Messages.sendSummonAll(player, flare, location);
  }

  /**
   * Checks if interact event is a flare use event.
   *
   * @param e Event to be checked.
   * @return True if interact eventt is a flare use event, false otherwise.
   */
  private boolean isFlareUseEvent(PlayerInteractEvent e) {
    if (!e.getAction().equals(Action.RIGHT_CLICK_AIR)
        && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      return false;
    }
    Player player = e.getPlayer();
    for (ItemStack item : InventoryHandler.getItemsInHand(player)) {
      if (FlareConfig.getFlareFromItem(item) != null) {
        return true;
      }
    }
    return false;
  }
}
