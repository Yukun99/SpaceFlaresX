package me.yukun.spaceflares.integration;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import org.bukkit.Location;

public class FactionsSupport {

  protected static boolean isInWarzone(Location location) {
    FLocation fLocation = FLocation.wrap(location);
    Faction faction = Board.getInstance().getFactionAt(fLocation);
    return faction.isWarZone();
  }
}
