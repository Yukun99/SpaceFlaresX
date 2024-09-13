package me.yukun.spaceflares.integration.hologram;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.yukun.spaceflares.flare.Flare;
import net.Zrips.CMILib.Container.CMILocation;
import org.bukkit.Location;

public class CMIHologramsSupport {

  private static final Map<Flare, CMIHologram> holograms = new HashMap<>();

  public static void addHologram(Flare flare, Location location, List<String> label) {
    if (label.isEmpty()) {
      return;
    }
    CMILocation cmiLocation = new CMILocation(location);
    CMIHologram hologram = new CMIHologram(makeUUID(location), cmiLocation);
    hologram.setLines(label);
    CMI.getInstance().getHologramManager().addHologram(hologram);
    hologram.update();
    holograms.put(flare, hologram);
  }

  public static void removeHologram(Flare flare) {
    holograms.get(flare).remove();
  }

  private static String makeUUID(Location location) {
    return "SpaceFlares" + location.hashCode();
  }
}
