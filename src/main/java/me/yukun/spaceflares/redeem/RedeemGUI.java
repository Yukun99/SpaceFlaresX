package me.yukun.spaceflares.redeem;

import static me.yukun.spaceflares.util.InventoryHandler.tryAddItems;
import static me.yukun.spaceflares.util.TextFormatter.applyColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.yukun.spaceflares.config.Config;
import me.yukun.spaceflares.config.EnvoyConfig;
import me.yukun.spaceflares.config.FlareConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.config.Redeems;
import me.yukun.spaceflares.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RedeemGUI {

  private static final Map<Inventory, RedeemGUI> guis = new HashMap<>();

  private final Player player;
  private final boolean isFlare;
  private final Inventory gui;
  private List<Pair<String, Integer>> redeems;
  private int page;
  private int last_page;

  public RedeemGUI(Player player, boolean isFlare) {
    // save fields
    this.player = player;
    this.isFlare = isFlare;

    // create inventory and contents
    int size = Config.getRedeemSize();
    String name = applyColor(Config.getRedeemName());
    this.gui = Bukkit.createInventory(player, size, name);
    this.redeems = convertRedeems();

    // create page trackers
    last_page = !redeems.isEmpty() ? redeems.size() / size + 1 : 0;
    page = last_page > 0 ? 1 : 0;

    // store GUI
    guis.put(gui, this);

    // open GUI
    openGUI();
  }

  /**
   * Closes GUIs and unsaves them on plugin disable.
   */
  public static void onDisable() {
    clearAllRedeemGUIs();
  }

  /**
   * Closes GUIs and unsaves them on plugin reload.
   */
  public static void reload() {
    clearAllRedeemGUIs();
  }

  private static void clearAllRedeemGUIs() {
    for (RedeemGUI gui : guis.values()) {
      gui.closeGUI();
    }
    guis.clear();
  }

  private void closeGUI() {
    player.closeInventory();
  }

  private void openGUI() {
    player.closeInventory();
    gui.setContents(getGUIItems());
    setGUITurnPageItems();
    player.openInventory(gui);
  }

  public void refreshGUI() {
    gui.setContents(getGUIItems());
    setGUITurnPageItems();
  }

  private ItemStack[] getGUIItems() {
    List<ItemStack> items = new ArrayList<>();
    ItemStack[] result = new ItemStack[gui.getSize()];
    // No redeems, return empty list.
    if (page == 0) {
      return items.toArray(result);
    }
    int size = Config.getRedeemSize();
    int begin = (size - 2) * (page - 1);
    int end = Math.min(redeems.size(), begin + size - 2);
    for (Pair<String, Integer> redeem : redeems.subList(begin, end)) {
      String type = redeem.key();
      ItemStack redeemItem =
          isFlare ? FlareConfig.getFlareItem(type) : EnvoyConfig.getEnvoyFlareItem(type);
      redeemItem.setAmount(redeem.value());
      items.add(redeemItem);
    }
    return items.toArray(result);
  }

  private void setGUITurnPageItems() {
    gui.setItem(getPreviousPageSlot(), Config.getPreviousPageItem());
    gui.setItem(getNextPageSlot(), Config.getNextPageItem());
  }

  public static int getPreviousPageSlot() {
    return Config.getRedeemSize() - 2;
  }

  public static int getNextPageSlot() {
    return Config.getRedeemSize() - 1;
  }

  public void reconvertRedeems() {
    redeems = convertRedeems();
    int size = Config.getRedeemSize();
    last_page = !redeems.isEmpty() ? redeems.size() / size + 1 : 0;
  }

  private List<Pair<String, Integer>> convertRedeems() {
    List<Pair<String, Integer>> redeems = new ArrayList<>();
    Map<String, Integer> configRedeems =
        isFlare ? Redeems.getFlareRedeems(player) : Redeems.getEnvoyFlareRedeems(player);
    for (String type : configRedeems.keySet()) {
      int amount = configRedeems.get(type);
      while (amount > 0) {
        int saved = Math.min(amount, 64);
        Pair<String, Integer> redeem = new Pair<>(type, amount);
        redeems.add(redeem);
        amount -= saved;
      }
    }
    return redeems;
  }

  public void nextPage() {
    if (page == last_page) {
      return;
    }
    page++;
    refreshGUI();
  }

  public void prevPage() {
    if (page == 0 || page == 1) {
      return;
    }
    page--;
    refreshGUI();
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public static boolean isRedeemGUIClickEvent(InventoryClickEvent e) {
    return e.getClickedInventory() != null && guis.containsKey(e.getClickedInventory());
  }

  public static boolean isPageTurnClickEvent(InventoryClickEvent e) {
    RedeemGUI clicked = guis.get(e.getClickedInventory());
    int slot = e.getSlot();
    return slot == clicked.gui.getSize() - 1 || slot == clicked.gui.getSize() - 2;
  }

  public static boolean isRedeemGUIMoveItemEvent(InventoryMoveItemEvent e) {
    return guis.containsKey(e.getDestination());
  }

  public static RedeemGUI getClickedRedeemGUI(Inventory inventory) {
    return guis.get(inventory);
  }

  public void redeemFlare(ItemStack given) {
    ItemStack remain = tryAddItems(player, given);
    String type;
    if (isFlare) {
      type = FlareConfig.getFlareFromItem(given);
    } else {
      type = EnvoyConfig.getEnvoyFlareFromItem(given);
    }
    if (isFlare) {
      Messages.sendRedeem(player, type, given.getAmount());
    } else {
      Messages.sendEnvoyRedeem(player, type, given.getAmount());
    }
    if (remain != null) {
      Messages.sendRedeemFull(player);
    }
    removeRedeemedFlares(type, given, remain);
  }

  private void removeRedeemedFlares(String type, ItemStack given, ItemStack remain) {
    int reduceAmount =
        remain == null ? given.getAmount() : given.getAmount() - remain.getAmount();
    if (isFlare) {
      Redeems.removeFlareRedeems(player, type, reduceAmount);
    } else {
      Redeems.removeEnvoyFlareRedeems(player, type, reduceAmount);
    }
  }
}
