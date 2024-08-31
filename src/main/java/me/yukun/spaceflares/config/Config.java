package me.yukun.spaceflares.config;

import static me.yukun.spaceflares.util.TextFormatter.applyColor;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Config {

  private static FileConfiguration config;
  private static ItemStack nextPageItem;
  private static ItemStack previousPageItem;

  protected static void setup(FileConfiguration fileConfiguration) {
    config = fileConfiguration;
    setupNextPageItem();
    setupPreviousPageItem();
  }

  private static void setupNextPageItem() {
    String materialName = config.getString("Next.Item");
    assert materialName != null;
    Material material = Material.getMaterial(materialName);
    assert material != null;
    nextPageItem = new ItemStack(material);

    ItemMeta itemMeta = nextPageItem.getItemMeta();
    assert itemMeta != null;

    String name = applyColor(config.getString("Next.Name"));
    List<String> lore = new ArrayList<>();
    for (String line : config.getStringList("Next.Lore")) {
      lore.add(applyColor(line));
    }
    itemMeta.setDisplayName(name);
    itemMeta.setLore(lore);
    nextPageItem.setItemMeta(itemMeta);
  }

  private static void setupPreviousPageItem() {
    String materialName = config.getString("Previous.Item");
    assert materialName != null;
    Material material = Material.getMaterial(materialName);
    assert material != null;
    previousPageItem = new ItemStack(material);

    ItemMeta itemMeta = previousPageItem.getItemMeta();
    assert itemMeta != null;

    String name = applyColor(config.getString("Previous.Name"));
    List<String> lore = new ArrayList<>();
    for (String line : config.getStringList("Previous.Lore")) {
      lore.add(applyColor(line));
    }
    itemMeta.setDisplayName(name);
    itemMeta.setLore(lore);
    previousPageItem.setItemMeta(itemMeta);
  }

  public static boolean hasCommandPermissions(CommandSender sender) {
    if (sender.hasPermission("spaceflares.*")) {
      return true;
    }
    return sender.hasPermission("spaceflares.admin");
  }

  public static int getRedeemSize() {
    return config.getInt("Redeem.Size");
  }

  public static String getRedeemName() {
    return config.getString("Redeem.Name");
  }

  public static ItemStack getNextPageItem() {
    return nextPageItem;
  }

  public static ItemStack getPreviousPageItem() {
    return previousPageItem;
  }
}
