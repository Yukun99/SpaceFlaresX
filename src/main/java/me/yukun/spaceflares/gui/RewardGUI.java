package me.yukun.spaceflares.gui;

import static me.yukun.spaceflares.util.TextFormatter.applyColor;
import static me.yukun.spaceflares.util.InventoryHandler.tryAddItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.yukun.spaceflares.config.Config;
import me.yukun.spaceflares.config.CrateConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.crate.CrateClickListener;
import me.yukun.spaceflares.gui.event.RewardGUIEmptyEvent;
import me.yukun.spaceflares.gui.handler.RewardGUIListener;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RewardGUI {

  private static final Map<Inventory, RewardGUI> guis = new HashMap<>();

  private final String crate;
  private final Block block;
  private final Player player;
  private final Inventory gui;
  private List<String> commandRewards = new ArrayList<>();
  private boolean isCommandClaimed = false;

  public RewardGUI(String crate, Player player, Block block) {
    // log fields
    this.crate = crate;
    this.block = block;
    this.player = player;

    // create inventory and contents
    int size = Config.getRedeemSize();
    String name = applyColor(Config.getRedeemName());
    this.gui = Bukkit.createInventory(player, size, name);
    setRewards();

    // store GUI
    guis.put(this.gui, this);

    // Register GUI for empty / closing listening
    RewardGUIEmptyEvent emptyEvent = new RewardGUIEmptyEvent(block);
    RewardGUIListener.registerGUIEvent(this.gui, emptyEvent);
  }

  private void setRewards() {
    for (ItemStack reward : CrateConfig.getCrateItemRewards(crate)) {
      gui.addItem(reward);
    }
    commandRewards = CrateConfig.getCrateCommandRewards(crate);
  }

  public void openGUI(Player player) {
    player.closeInventory();
    player.openInventory(gui);
  }

  public static void rewardGUIFastDropContents(Inventory gui, Player player) {
    guis.get(gui).fastDropContents(player);
  }

  public void fastDropContents(Player player) {
    if (CrateConfig.getCrateDoFastDrop(crate)) {
      fastDropItems();
    } else {
      fastGiveItems(player);
    }
    CrateClickListener.unregisterClaimedBlock(block);
    RewardGUIListener.deregisterGUIEvent(gui);
  }

  public void fastDropContents() {
    fastDropContents(player);
  }

  private void fastDropItems() {
    for (ItemStack item : gui.getContents()) {
      if (item == null) {
        continue;
      }
      block.getWorld().dropItemNaturally(block.getLocation(), item);
    }
    gui.clear();
  }

  private void fastGiveItems(Player player) {
    for (ItemStack item : gui.getContents()) {
      if (item == null) {
        continue;
      }
      ItemStack remain = tryAddItems(player, item);
      if (remain == null) {
        break;
      }
      block.getWorld().dropItemNaturally(block.getLocation(), remain);
    }
    gui.clear();
  }

  public static String getCrateTier(Inventory gui) {
    return guis.get(gui).crate;
  }

  public void despawnCrate() {
    RewardGUIListener.deregisterGUIEvent(gui);
  }

  public Player getPlayer() {
    return player;
  }

  public void giveCommandRewards(Player player) {
    if (isCommandClaimed) {
      return;
    }
    isCommandClaimed = true;
    for (String command : commandRewards) {
      String reward = Messages.replacePlayer(command, player);
      Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), reward);
    }
  }
}
