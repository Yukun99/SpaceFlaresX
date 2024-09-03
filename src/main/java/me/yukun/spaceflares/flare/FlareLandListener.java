package me.yukun.spaceflares.flare;

import static me.yukun.spaceflares.util.InventoryHandler.tryAddItems;

import java.util.HashMap;
import java.util.Map;
import me.yukun.spaceflares.SpaceFlares;
import me.yukun.spaceflares.config.CrateConfig;
import me.yukun.spaceflares.config.FlareConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.config.Redeems;
import me.yukun.spaceflares.crate.CrateClickListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class FlareLandListener implements Listener {

  private static final Map<FallingBlock, String> blockFlareMap = new HashMap<>();
  private static final Map<FallingBlock, Player> blockPlayerMap = new HashMap<>();

  public static void registerFallingBlock(FallingBlock block, String flare, Player player) {
    blockFlareMap.put(block, flare);
    blockPlayerMap.put(block, player);
  }

  public static void deregisterFallingBlock(FallingBlock block) {
    blockFlareMap.remove(block);
    blockPlayerMap.remove(block);
  }

  /**
   * Clears all falling flares and refunds flare items on plugin disable.
   */
  public static void onDisable() {
    clearAllFlares();
  }

  /**
   * Clears all falling flares and refunds flare items on plugin reload.
   */
  public static void onReload() {
    clearAllFlares();
  }

  private static void clearAllFlares() {
    for (FallingBlock block : blockFlareMap.keySet()) {
      String flare = blockFlareMap.get(block);
      Player player = blockPlayerMap.get(block);
      block.remove();
      returnFlare(flare, player);
    }
  }

  private static void returnFlare(String flare, Player player) {
    ItemStack given = FlareConfig.getFlareItem(flare).clone();
    ItemStack remainItem = tryAddItems(player, given);
    Messages.sendReceive(player, flare, given.getAmount());
    if (remainItem == null) {
      return;
    }
    int remain = remainItem.getAmount();
    Redeems.addRedeems(player, flare, remain);
    Messages.sendReceiveFull(player, remain);
  }

  @EventHandler
  private void flareLandEvent(EntityChangeBlockEvent e) {
    if (!isFlareLandEvent(e)) {
      return;
    }
    FallingBlock fallingBlock = (FallingBlock) e.getEntity();
    Block block = e.getBlock();
    landCrate(fallingBlock, block);
  }

  private boolean isFlareLandEvent(EntityChangeBlockEvent e) {
    if (!(e.getEntity() instanceof FallingBlock block)) {
      return false;
    }
    return blockFlareMap.containsKey(block);
  }

  @EventHandler
  private void flareBreakEvent(EntityDropItemEvent e) {
    if (!isFlareDeathEvent(e)) {
      return;
    }
    e.setCancelled(true);
    FallingBlock fallingBlock = (FallingBlock) e.getEntity();
    Location location = e.getEntity().getLocation();
    while (location.getBlock().getType() != Material.AIR) {
      location.setY(location.getBlockY() + 1);
    }
    location.getBlock().setType(fallingBlock.getBlockData().getMaterial());
    Block block = location.getBlock();
    landCrate(fallingBlock, block);
  }

  private boolean isFlareDeathEvent(EntityDropItemEvent e) {
    if (!(e.getEntity() instanceof FallingBlock)) {
      return false;
    }
    return blockFlareMap.containsKey((FallingBlock) e.getEntity());
  }

  private void landCrate(FallingBlock fallingBlock, Block block) {
    String crate = blockFlareMap.get(fallingBlock);
    Player player = blockPlayerMap.get(fallingBlock);
    deregisterFallingBlock(fallingBlock);
    FlareUseListener.stopFireworks(fallingBlock);
    Integer despawnTimer = startDespawnTimer(crate, block);
    CrateClickListener.registerFallenBlock(block, crate, player, despawnTimer);
    int despawn = CrateConfig.getCrateDespawnTime(crate);
    Messages.sendLand(player, crate, despawn, block.getLocation());
    Messages.sendLandAll(player, crate, despawn, block.getLocation());
  }

  private Integer startDespawnTimer(String crate, Block block) {
    int despawnTime = CrateConfig.getCrateDespawnTime(crate);
    if (despawnTime == 0) {
      return null;
    }
    return Bukkit.getScheduler().scheduleSyncDelayedTask(SpaceFlares.getPlugin(),
        () -> CrateClickListener.despawnCrate(block), despawnTime * 20L);
  }
}
