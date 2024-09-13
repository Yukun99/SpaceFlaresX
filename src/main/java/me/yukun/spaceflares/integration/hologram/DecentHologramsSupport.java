package me.yukun.spaceflares.integration.hologram;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.yukun.spaceflares.flare.Flare;
import org.bukkit.Location;

public class DecentHologramsSupport {

  private static final Map<Flare, Hologram> holograms = new HashMap<>();

  public static void addHologram(Flare flare, Location location, List<String> label) {
    if (label.isEmpty()) {
      return;
    }
    Hologram hologram = DHAPI.createHologram(makeUUID(location), location);
    DHAPI.setHologramLines(hologram, label);
    holograms.put(flare, hologram);
  }

  public static void removeHologram(Flare flare) {
    holograms.get(flare).destroy();
  }

  private static String makeUUID(Location location) {
    return "SpaceFlares" + location.hashCode();
  }
}
