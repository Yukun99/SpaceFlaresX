package me.yukun.spaceflares.envoy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.yukun.spaceflares.config.CrateConfig;
import me.yukun.spaceflares.config.Messages;
import me.yukun.spaceflares.flare.Flare;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class EnvoyFlare extends Flare {

  private final Envoy envoy;

  public EnvoyFlare(String type, Location location, Envoy envoy) {
    super(type, location);
    this.envoy = envoy;
  }

  @Override
  protected void summonFlare(Location location) {
    BlockData flareBlockData = CrateConfig.getCrateBlock(type);
    FallingBlock flareBlock = Objects.requireNonNull(location.getWorld())
        .spawnFallingBlock(location, flareBlockData);

    this.fallingBlock = flareBlock;
    fallingBlockMap.put(flareBlock, this);
    startFireworks();
  }

  @Override
  public void landCrate(Block block) {
    fallingBlockMap.remove(this.fallingBlock);
    this.fallingBlock = null;
    stopFireworks();
    this.block = block;
    blockMap.put(block, this);

    setRewards();
  }

  @Override
  protected void openCrate(Player player) {
    fastOpenCrate(player);
  }

  @Override
  protected void fastOpenCrate(Player player) {
    giveCommandRewards(player);
    if (CrateConfig.getCrateDoFastDrop(type)) {
      dropItemRewards();
    } else {
      giveItemRewards(player);
    }

    int remain = envoy.getRemain();
    Messages.sendEnvoyClaim(player, type, remain);
    Messages.sendEnvoyClaimAll(player, type, remain, block.getLocation());
    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 10);
  }

  @Override
  public void endCrate() {
    clearCrate();
    blockMap.remove(block);
    envoy.deregisterFlare(this);
  }

  @Override
  public void clearCrate() {
    if (itemRewards == null) {
      return;
    }

    guiMap.remove(itemRewards);
    itemRewards.clear();
    List<HumanEntity> viewers = new ArrayList<>(itemRewards.getViewers());
    for (HumanEntity player : viewers) {
      player.closeInventory();
    }

    block.setType(Material.AIR);
  }

  @Override
  protected void stopDespawnTimer() {
  }

  @Override
  protected void endFlare() {
    if (fallingBlock == null) {
      return;
    }

    this.fallingBlock.remove();
    stopFireworks();
  }

  protected void deregisterFlare() {
    fallingBlockMap.remove(fallingBlock);
  }
}
