package me.yukun.spaceflares.integration.hologram;

import java.util.List;
import me.yukun.spaceflares.flare.Flare;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class HologramSupportManager {

  public static boolean hasCMIHolograms() {
    return Bukkit.getPluginManager().isPluginEnabled("CMI");
  }

  public static boolean hasDecentHolograms() {
    return Bukkit.getPluginManager().isPluginEnabled("DecentHolograms");
  }

  public static void addHologram(Flare flare, Location location, List<String> label) {
    if (!hasCMIHolograms() && !hasDecentHolograms()) {
      return;
    }
    if (hasCMIHolograms()) {
      CMIHologramsSupport.addHologram(flare, location, label);
    }
    if (hasDecentHolograms()) {
      DecentHologramsSupport.addHologram(flare, location, label);
    }
  }

  public static void deleteHologram(Flare flare) {
    if (!hasCMIHolograms() && !hasDecentHolograms()) {
      return;
    }

    if (hasCMIHolograms()) {
      CMIHologramsSupport.removeHologram(flare);
    }
    if (hasDecentHolograms()) {
      DecentHologramsSupport.removeHologram(flare);
    }
  }
}
