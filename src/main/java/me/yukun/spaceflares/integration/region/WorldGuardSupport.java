package me.yukun.spaceflares.integration.region;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import java.util.List;
import java.util.Objects;
import me.yukun.spaceflares.config.FlareConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldGuardSupport {

  protected static boolean isRegion(String region) {
    WorldGuard wg = WorldGuard.getInstance();
    RegionContainer container = wg.getPlatform().getRegionContainer();
    for (World world : Bukkit.getWorlds()) {
      BukkitWorld bukkitWorld = new BukkitWorld(world);
      if (Objects.requireNonNull(container.get(bukkitWorld)).getRegion(region) != null) {
        return true;
      }
    }
    return false;
  }

  protected static boolean hasPvPFlag(Location location) {
    RegionManager manager = getRegionManager(location);
    if (manager == null) {
      return true;
    }
    ApplicableRegionSet set = getApplicableRegions(location, manager);
    State state = set.queryState(null, Flags.PVP);
    if (state == null) {
      return true;
    }
    return state.equals(State.ALLOW);
  }

  protected static boolean hasNoBuildFlag(Location location) {
    RegionManager manager = getRegionManager(location);
    if (manager == null) {
      return true;
    }
    ApplicableRegionSet set = getApplicableRegions(location, manager);
    State state = set.queryState(null, Flags.BLOCK_BREAK);
    if (state == null) {
      return true;
    }
    return state.equals(State.DENY);
  }

  private static RegionManager getRegionManager(Location location) {
    WorldGuard wg = WorldGuard.getInstance();
    RegionContainer container = wg.getPlatform().getRegionContainer();
    BukkitWorld world = new BukkitWorld(location.getWorld());
    return container.get(world);
  }

  private static ApplicableRegionSet getApplicableRegions(Location location,
      RegionManager manager) {
    BlockVector3 v = BlockVector3.at(location.getX(), location.getY(), location.getZ());
    return manager.getApplicableRegions(v);
  }

  protected static boolean isInRegion(Location location, String flare) {
    List<String> regions = FlareConfig.getFlareRegionWGList(flare);
    RegionManager manager = getRegionManager(location);
    for (ProtectedRegion set : getApplicableRegions(location, manager)) {
      if (set == null) {
        continue;
      }
      if (regions.contains(set.getId())) {
        return true;
      }
    }
    return false;
  }
}
