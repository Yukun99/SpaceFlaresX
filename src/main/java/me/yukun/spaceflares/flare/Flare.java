package me.yukun.spaceflares.flare;

import static me.yukun.spaceflares.util.InventoryHandler.tryAddItems;
import static me.yukun.spaceflares.util.TextFormatter.applyColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.yukun.spaceflares.SpaceFlares;
import me.yukun.spaceflares.config.Config;
import me.yukun.spaceflares.config.CrateConfig;
import me.yukun.spaceflares.config.FlareConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.flare.events.FlareFireworkListener;
import me.yukun.spaceflares.integration.hologram.HologramSupportManager;
import me.yukun.spaceflares.util.Fireworks;
import me.yukun.spaceflares.util.InventoryHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Flare {

  protected final String type;
  private final Player player;

  protected static final Map<FallingBlock, Flare> fallingBlockMap = new HashMap<>();
  protected FallingBlock fallingBlock = null;

  protected static final Map<Block, Flare> blockMap = new HashMap<>();
  protected Block block = null;

  protected static final Map<Inventory, Flare> guiMap = new HashMap<>();
  protected Inventory itemRewards;
  protected List<String> cmdRewards = new ArrayList<>();

  protected int fireworkTimer;
  protected int despawnTimer;

  /**
   * Constructor for a Flare instance.
   * <p>Immediately spawns flare when constructed.</p>
   *
   * @param type   Type of flare to summon.
   * @param player Player who summoned flare.
   */
  public Flare(String type, Player player) {
    this.type = type;
    this.player = player;
    consumeFlare();
    summonFlare();
  }

  /**
   * Constructor for a Flare instance.
   * <p>Immediately spawns flare when constructed.</p>
   *
   * @param type     Type of flare to summon.
   * @param player   Player who summoned flare.
   * @param location Location to summon flare at.
   */
  public Flare(String type, Player player, Location location) {
    this.type = type;
    this.player = player;
    summonFlare(location);
  }

  protected Flare(String type, Location location) {
    this.type = type;
    this.player = null;
    summonFlare(location);
  }

  /**
   * Clears all falling flares and refunds flare items on plugin disable.
   * <p>Clears all landed crates and drops items on plugin disable.</p>
   */
  public static void onDisable() {
    clearAllFlares();
  }

  /**
   * Clears all falling flares and refunds flare items on plugin reload.
   * <p>Clears all landed crates and drops items on plugin disable.</p>
   */
  public static void reload() {
    clearAllFlares();
  }

  /**
   * Gets Flare instance corresponding to specified falling block.
   *
   * @param fallingBlock Falling block to get flare instance for.
   * @return Flare instance corresponding to specified falling block.
   */
  public static Flare getLandedFlare(FallingBlock fallingBlock) {
    return fallingBlockMap.get(fallingBlock);
  }

  /**
   * Gets Flare instance corresponding to specified clicked block.
   *
   * @param block Clicked block to get flare instance for.
   * @return Flare instance corresponding to specified falling block.
   */
  public static Flare getClickedFlare(Block block) {
    return blockMap.get(block);
  }

  /**
   * Gets Flare instance corresponding to specified closed inventory.
   *
   * @param inventory Closed inventory to get flare instance for.
   * @return Flare instance corresponding to specified closed inventory.
   */
  public static Flare getClosedFlare(Inventory inventory) {
    return guiMap.get(inventory);
  }

  /**
   * Consumes a single flare item currently held by player.
   */
  private void consumeFlare() {
    ItemStack main = player.getInventory().getItemInMainHand();
    ItemStack off = player.getInventory().getItemInOffHand();
    if (FlareConfig.getFlareFromItem(main) != null) {
      main.setAmount(main.getAmount() - 1);
    } else {
      off.setAmount(off.getAmount() - 1);
    }
  }

  /**
   * Summons flare for player.
   */
  private void summonFlare() {
    Location location = FlareConfig.getFlareSpawnLocation(type, player);
    summonFlare(location);
  }

  /**
   * Summons flare for player at specified location. Workflow:
   * <p>1. Summon falling block and set block type.</p>
   * <p>2. Store falling block and start events.</p>
   * <p>3. Send summoned messages.</p>
   *
   * @param location Location to summon flare at.
   */
  protected void summonFlare(Location location) {
    BlockData flareBlockData = CrateConfig.getCrateBlock(type);
    FallingBlock flareBlock = Objects.requireNonNull(location.getWorld())
        .spawnFallingBlock(location, flareBlockData);

    this.fallingBlock = flareBlock;
    fallingBlockMap.put(flareBlock, this);
    startFireworks();

    Messages.sendSummon(player, type, location);
    Messages.sendSummonAll(player, type, location);
  }

  /**
   * Registers flare as landed. Workflow:
   * <p>1. Gracefully remove falling block and stop events.</p>
   * <p>2. Store fallen block and start events.</p>
   * <p>3. Set rewards.</p>
   * <p>4. Send landed messages.</p>
   *
   * @param block Block that is a result of the flare landing.
   */
  public void landCrate(Block block) {
    fallingBlockMap.remove(this.fallingBlock);
    this.fallingBlock = null;
    stopFireworks();
    this.block = block;
    blockMap.put(block, this);

    HologramSupportManager.addHologram(
        this,
        CrateConfig.getCrateFlareHologramLocation(type, block.getLocation()),
        CrateConfig.getCrateFlareHologramLabel(type));

    startDespawnTimer();

    setRewards();

    Messages.sendLand(player, type, block.getLocation());
    Messages.sendLandAll(player, type, block.getLocation());
  }

  /**
   * Sets rewards for landed crate.
   */
  protected void setRewards() {
    int size = Config.getRedeemSize();
    String name = applyColor(Config.getRedeemName());
    this.itemRewards = Bukkit.createInventory(player, size, name);
    guiMap.put(itemRewards, this);

    for (ItemStack reward : CrateConfig.getCrateItemRewards(type)) {
      itemRewards.addItem(reward);
    }
    cmdRewards = CrateConfig.getCrateCommandRewards(type);
  }

  /**
   * Rewards player who interacts with crate.
   *
   * @param player Player who clicked on crate.
   * @param action Action performed on crate.
   */
  public void rewardPlayer(Player player, Action action) {
    if (isFastClaim(player, action)) {
      fastOpenCrate(player);
    } else {
      openCrate(player);
    }
    endCrate();
  }

  /**
   * Rewards player who closes crate.
   *
   * @param player Player who closed crate.
   */
  public void rewardClosedPlayer(Player player) {
    if (CrateConfig.getCrateDoFastExit(type)) {
      fastOpenCrate(player);
    }
    endCrate();
  }

  /**
   * Checks if player did a fast claim.
   *
   * @param player Player who clicked on crate.
   * @param action Action performed on crate.
   * @return True if player did fast claim, False otherwise.
   */
  private boolean isFastClaim(Player player, Action action) {
    if (player.isSneaking() && CrateConfig.getCrateDoFastSneak(type)) {
      return true;
    }
    if (action.equals(Action.LEFT_CLICK_BLOCK) && CrateConfig.getCrateDoFastPunch(type)) {
      return true;
    }
    return action.equals(Action.RIGHT_CLICK_BLOCK) && CrateConfig.getCrateDoFastUse(type);
  }

  /**
   * Opens crate normally for specified player.
   *
   * @param player Player to open crate for.
   */
  protected void openCrate(Player player) {
    giveCommandRewards(player);
    player.openInventory(itemRewards);

    Messages.sendClaim(player, type, block.getLocation());
    Messages.sendClaimAll(player, type, block.getLocation());
    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 10);
  }

  /**
   * Fast opens crate for specified player.
   *
   * @param player Player to fast open crate for.
   */
  protected void fastOpenCrate(Player player) {
    giveCommandRewards(player);
    if (CrateConfig.getCrateDoFastDrop(type)) {
      dropItemRewards();
    } else {
      giveItemRewards(player);
    }

    Messages.sendClaim(player, type, block.getLocation());
    Messages.sendClaimAll(player, type, block.getLocation());
    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 10);
  }

  /**
   * Despawns crate when claiming time is up. Workflow:
   * <p>1. Give rewards if enabled.</p>
   * <p>2. Gracefully remove crate from world.</p>
   */
  protected void despawnCrate() {
    if (CrateConfig.getCrateDoDespawnGive(type)) {
      giveCommandRewards(player);
      giveItemRewards(player);
    }
    endCrate();
    Messages.sendDespawnNotify(player, type, block.getLocation());
    Messages.sendDespawnAll(player, type, block.getLocation());
  }

  /**
   * Gives command rewards to player.
   * <p>Does nothing if command rewards have already been claimed.</p>
   *
   * @param player Player to give command rewards to.
   */
  protected void giveCommandRewards(Player player) {
    if (cmdRewards.isEmpty()) {
      return;
    }
    for (String command : cmdRewards) {
      String reward = Messages.replacePlayer(command, player);
      Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), reward);
    }
    cmdRewards.clear();
  }

  /**
   * Gives item rewards to player directly.
   *
   * @param player Player to give item rewards to.
   */
  protected void giveItemRewards(Player player) {
    if (itemRewards.isEmpty()) {
      return;
    }
    for (ItemStack item : itemRewards.getContents()) {
      if (item == null) {
        continue;
      }
      ItemStack remain = tryAddItems(player, item);
      if (remain == null) {
        break;
      }
      block.getWorld().dropItemNaturally(block.getLocation(), remain);
    }
    itemRewards.clear();
  }

  /**
   * Drops item rewards on ground.
   */
  protected void dropItemRewards() {
    if (itemRewards.isEmpty()) {
      return;
    }
    for (ItemStack item : itemRewards.getContents()) {
      if (item == null) {
        continue;
      }
      block.getWorld().dropItemNaturally(block.getLocation(), item);
    }
    itemRewards.clear();
  }

  /**
   * Ends crate gracefully. Workflow:
   * <p>1. End despawn timer.</p>
   * <p>2. Remove rewards, close reward GUIs.</p>
   * <p>3. Send despawn messages.</p>
   * <p>4. Deregister crate block.</p>
   */
  public void endCrate() {
    clearCrate();
    blockMap.remove(block);
  }

  public void clearCrate() {
    Bukkit.getScheduler().cancelTask(despawnTimer);

    guiMap.remove(itemRewards);
    itemRewards.clear();
    List<HumanEntity> viewers = new ArrayList<>(itemRewards.getViewers());
    for (HumanEntity player : viewers) {
      player.closeInventory();
    }

    block.setType(Material.AIR);
    HologramSupportManager.deleteHologram(this);
  }

  /**
   * Starts firework spawning for falling flare.
   */
  protected void startFireworks() {
    fireworkTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(SpaceFlares.getPlugin(), () -> {
      Location loc = fallingBlock.getLocation();
      Firework firework = Fireworks.spawnFirework(loc, type);
      FlareFireworkListener.registerFirework(firework);
    }, 0, 5);
  }

  /**
   * Stops firework spawning for fallen crate.
   */
  protected void stopFireworks() {
    Bukkit.getScheduler().cancelTask(fireworkTimer);
  }

  /**
   * Starts despawn timer for landed crate.
   */
  private void startDespawnTimer() {
    int despawnTime = CrateConfig.getCrateDespawnTime(type);
    if (despawnTime == 0) {
      return;
    }
    despawnTimer = Bukkit.getScheduler()
        .scheduleSyncDelayedTask(SpaceFlares.getPlugin(), this::despawnCrate, despawnTime * 20L);
  }

  /**
   * Stops despawn timer for claimed or despawn crate.
   */
  protected void stopDespawnTimer() {
    Bukkit.getScheduler().cancelTask(despawnTimer);
  }

  /**
   * Removes all active flares and refunds flare items.
   */
  private static void clearAllFlares() {
    for (Flare flare : fallingBlockMap.values()) {
      flare.endFlare();
    }
    fallingBlockMap.clear();

    for (Flare flare : blockMap.values()) {
      flare.stopDespawnTimer();
      if (flare.player != null) {
        flare.fastOpenCrate(flare.player);
      }
      flare.clearCrate();
    }
    blockMap.clear();
  }

  /**
   * Ends flare and refunds flare item to summoner.
   */
  protected void endFlare() {
    if (fallingBlock == null) {
      return;
    }

    fallingBlock.remove();
    stopFireworks();

    InventoryHandler.giveFlare(player, type, 1);
  }
}