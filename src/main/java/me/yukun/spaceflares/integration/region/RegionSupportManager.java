package me.yukun.spaceflares.integration.region;

import java.util.ArrayList;
import java.util.List;
import me.yukun.spaceflares.config.FlareConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RegionSupportManager {

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public static boolean hasWorldGuard() {
    return Bukkit.getPluginManager().isPluginEnabled("WorldGuard");
  }

  public static boolean hasSaberFactions() {
    return Bukkit.getPluginManager().isPluginEnabled("Factions");
  }

  public static boolean isRegion(String region) {
    if (!hasWorldGuard()) {
      return true;
    }
    return WorldGuardSupport.isRegion(region);
  }

  /**
   * Checks the spawning boundaries around specified player for valid region settings.
   * <p>We do this so that players can only spawn flares when they are well in range.</p>
   *
   * @param player Player whose location spawning boundaries are centered around.
   * @param flare  Type of flare to check spawning boundaries for.
   * @return Whether spawning boundaries around specified player are valid.
   */
  public static boolean canSpawnFlare(Player player, String flare) {
    if (!hasSaberFactions() && !hasWorldGuard()) {
      return true;
    }
    Location location = player.getLocation();
    if (hasWorldGuard() && isInRegion(location, flare) && hasPvPFlag(location, flare)
        && hasNoBuildFlag(location, flare)) {
      return true;
    }
    return hasSaberFactions() && isInWarzone(location, flare);
  }

  /**
   * Checks the spawning boundaries around specified location for enabled PvP flag.
   * <p>We do this so that players can only spawn flares when they are well in range.</p>
   *
   * @param location Location where spawning boundaries are centered around.
   * @param flare    Type of flare to check spawning boundaries for.
   * @return Whether spawning boundaries around specified location has PvP flag enabled.
   */
  private static boolean hasPvPFlag(Location location, String flare) {
    if (!FlareConfig.getFlareRegionWGDoPvPFlag(flare)) {
      return true;
    }
    if (!hasWorldGuard()) {
      return true;
    }
    for (Location deflected : getBounds(location, flare)) {
      if (!hasPvPFlag(deflected)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks the spawning boundaries around specified location for disabled Build flag.
   * <p>We do this so that players can only spawn flares when they are well in range.</p>
   *
   * @param location Location where spawning boundaries are centered around.
   * @param flare    Type of flare to check spawning boundaries for.
   * @return Whether spawning boundaries around specified location has Build flag disabled.
   */
  private static boolean hasNoBuildFlag(Location location, String flare) {
    if (!FlareConfig.getFlareRegionWGDoNoBuild(flare)) {
      return true;
    }
    if (!hasWorldGuard()) {
      return true;
    }
    for (Location deflected : getBounds(location, flare)) {
      if (!hasNoBuildFlag(deflected)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks the spawning boundaries around specified location for being in a valid region.
   * <p>We do this so that players can only spawn flares when they are well in range.</p>
   *
   * @param location Location where spawning boundaries are centered around.
   * @param flare    Type of flare to check spawning boundaries for.
   * @return Whether spawning boundaries around specified location are in a valid region.
   */
  private static boolean isInRegion(Location location, String flare) {
    if (FlareConfig.getFlareRegionWGList(flare).isEmpty()) {
      return true;
    }
    if (!hasWorldGuard()) {
      return true;
    }
    for (Location deflected : getBounds(location, flare)) {
      if (!WorldGuardSupport.isInRegion(deflected, flare)) {
        return false;
      }
    }
    return true;
  }

  private static boolean isInWarzone(Location location, String flare) {
    if (!FlareConfig.getFlareRegionUseWarzone(flare)) {
      return true;
    }
    if (!hasSaberFactions()) {
      return true;
    }
    for (Location deflected : getBounds(location, flare)) {
      if (!SaberFactionsSupport.isInWarzone(deflected)) {
        return false;
      }
    }
    return true;
  }

  private static List<Location> getBounds(Location middle, String flare) {
    List<Location> horizontals = getHorizontalBounds(middle, flare);
    return getTopBounds(horizontals, flare);
  }

  private static List<Location> getHorizontalBounds(Location middle, String flare) {
    List<Location> locations = new ArrayList<>();
    locations.add(middle);
    int deflection = FlareConfig.getFlareRandomRadius(flare);
    if (deflection != 0) {
      locations.add(middle.clone().add(deflection, 0, 0));
      locations.add(middle.clone().add(-deflection, 0, 0));
      locations.add(middle.clone().add(deflection, 0, 0));
      locations.add(middle.clone().add(deflection, 0, 0));
    }
    return locations;
  }

  private static List<Location> getTopBounds(List<Location> horizontals, String flare) {
    List<Location> locations = new ArrayList<>();
    int elevation = FlareConfig.getFlareFallHeight(flare);
    if (elevation != 0) {
      for (Location location : horizontals) {
        locations.add(location.clone().add(0, elevation, 0));
      }
    }
    return locations;
  }

  private static boolean hasPvPFlag(Location location) {
    return WorldGuardSupport.hasPvPFlag(location);
  }

  private static boolean hasNoBuildFlag(Location location) {
    return WorldGuardSupport.hasNoBuildFlag(location);
  }
}
