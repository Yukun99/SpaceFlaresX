package me.yukun.spaceflares.crate;

import java.util.HashMap;
import java.util.Map;
import me.yukun.spaceflares.config.CrateConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.gui.RewardGUI;
import me.yukun.spaceflares.gui.event.RewardGUIEmptyEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CrateClickListener implements Listener {

  private static final Map<Block, String> blockCrateMap = new HashMap<>();
  private static final Map<Block, RewardGUI> blockGUIMap = new HashMap<>();
  private static final Map<Block, Integer> blockDespawnTaskMap = new HashMap<>();

  /**
   * Clears all landed crates and drops items on plugin disable.
   */
  public static void onDisable() {
    for (Block block : blockCrateMap.keySet()) {
      block.setType(Material.AIR);
      blockGUIMap.get(block).fastDropContents();
    }
  }

  /**
   * Clears all landed crates and drops items on plugin reload.
   */
  public static void onReload() {
    for (Block block : blockCrateMap.keySet()) {
      block.setType(Material.AIR);
      unregisterClaimedBlock(block);
    }
  }

  public static void registerFallenBlock(Block block, String flare, Player player,
      Integer despawn) {
    blockCrateMap.put(block, flare);
    RewardGUI rewardGUI = new RewardGUI(flare, player, block);
    blockGUIMap.put(block, rewardGUI);
    if (despawn != null) {
      blockDespawnTaskMap.put(block, despawn);
    }
  }

  public static void unregisterClaimedBlock(Block block) {
    blockCrateMap.remove(block);
    blockGUIMap.remove(block);
    if (blockDespawnTaskMap.containsKey(block)) {
      Bukkit.getScheduler().cancelTask(blockDespawnTaskMap.get(block));
      blockDespawnTaskMap.remove(block);
    }
  }

  public static void despawnCrate(Block block) {
    String crate = blockCrateMap.get(block);
    Player player = blockGUIMap.get(block).getPlayer();
    if (CrateConfig.getCrateDoDespawnGive(crate)) {
      blockGUIMap.get(block).giveCommandRewards(player);
      blockGUIMap.get(block).fastDropContents();
    } else {
      blockGUIMap.get(block).despawnCrate();
    }
    unregisterClaimedBlock(block);
    Messages.sendDespawnNotify(player, crate, block.getLocation());
    Messages.sendDespawnAll(player, crate, block.getLocation());
  }

  @EventHandler
  private void crateClickEvent(PlayerInteractEvent e) {
    if (!isCrateClickEvent(e)) {
      return;
    }
    e.setCancelled(true);
    Block block = e.getClickedBlock();
    String crate = blockCrateMap.get(block);
    Player player = e.getPlayer();
    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 10);
    assert block != null;
    blockGUIMap.get(block).giveCommandRewards(player);
    Messages.sendClaim(player, crate, block.getLocation());
    Messages.sendClaimAll(player, crate, block.getLocation());
    if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
      handlePunchEvent(e, crate);
    }
    if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      handleUseEvent(e, crate);
    }
  }

  @EventHandler
  private void crateEmptyEvent(RewardGUIEmptyEvent e) {
    e.getBlock().setType(Material.AIR);
  }

  private void handlePunchEvent(PlayerInteractEvent e, String crate) {
    Block block = e.getClickedBlock();
    Player player = e.getPlayer();
    if (isFastPunchEvent(e, crate)) {
      blockGUIMap.get(block).fastDropContents(player);
    } else {
      blockGUIMap.get(block).openGUI(player);
    }
  }

  private boolean isFastPunchEvent(PlayerInteractEvent e, String crate) {
    Player player = e.getPlayer();
    if (player.isSneaking()) {
      return CrateConfig.getCrateDoFastSneak(crate);
    } else {
      return CrateConfig.getCrateDoFastPunch(crate);
    }
  }

  private void handleUseEvent(PlayerInteractEvent e, String crate) {
    Block block = e.getClickedBlock();
    Player player = e.getPlayer();
    if (isFastUseEvent(e, crate)) {
      blockGUIMap.get(block).fastDropContents(player);
    } else {
      blockGUIMap.get(block).openGUI(player);
    }
  }

  private boolean isFastUseEvent(PlayerInteractEvent e, String crate) {
    Player player = e.getPlayer();
    if (player.isSneaking()) {
      return CrateConfig.getCrateDoFastSneak(crate);
    } else {
      return CrateConfig.getCrateDoFastUse(crate);
    }
  }

  private boolean isCrateClickEvent(PlayerInteractEvent e) {
    if (!e.getAction().equals(Action.LEFT_CLICK_BLOCK)
        && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      return false;
    }
    Block block = e.getClickedBlock();
    return blockCrateMap.containsKey(block);
  }
}
