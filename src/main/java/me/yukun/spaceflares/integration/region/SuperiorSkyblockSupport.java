package me.yukun.spaceflares.integration.region;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SuperiorSkyblockSupport {

  private SuperiorSkyblockSupport() {}

  /**
   * Checks if a location is within an island that spawning player is a member of.
   * @param player   Player who spawned flare.
   * @param location Location to check.
   * @return True if player is within an island that they are a member of, False otherwise.
   */
  protected static boolean isInIsland(Player player, Location location) {
    Island island = SuperiorSkyblockAPI.getIslandAt(location);
    if (island == null) return false;
    return island.getCoopPlayers().contains(SuperiorSkyblockAPI.getPlayer(player));
  }
}
