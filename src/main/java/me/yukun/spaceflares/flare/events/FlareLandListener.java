package me.yukun.spaceflares.flare.events;

import me.yukun.spaceflares.flare.Flare;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDropItemEvent;

public class FlareLandListener implements Listener {

  @EventHandler
  private void flareLandEvent(EntityChangeBlockEvent e) {
    Flare flare = getFlareFromEntity(e.getEntity());
    if (flare == null) {
      return;
    }
    Block block = e.getBlock();
    flare.landCrate(block);
  }

  @EventHandler
  private void flareBreakEvent(EntityDropItemEvent e) {
    Flare flare = getFlareFromEntity(e.getEntity());
    if (flare == null) {
      return;
    }
    e.setCancelled(true);
    FallingBlock fallingBlock = (FallingBlock) e.getEntity();
    Block block = placeCrateSafely(fallingBlock);
    fallingBlock.remove();
    flare.landCrate(block);
  }

  private Flare getFlareFromEntity(Entity entity) {
    if (!(entity instanceof FallingBlock block)) {
      return null;
    }
    return Flare.getLandedFlare(block);
  }

  private Block placeCrateSafely(FallingBlock fallingBlock) {
    Location safe = getSafeLocation(fallingBlock.getLocation());
    safe.getBlock().setType(fallingBlock.getBlockData().getMaterial());
    return safe.getBlock();
  }

  private Location getSafeLocation(Location location) {
    int originalY = location.getBlockY();
    while (location.getBlock().getType() != Material.AIR) {
      if (location.getBlockY() == 320) {
        location.setY(originalY);
        location.setX(location.getBlockX() + 1);
      }
      location.setY(location.getBlockY() + 1);
    }
    return location;
  }
}
