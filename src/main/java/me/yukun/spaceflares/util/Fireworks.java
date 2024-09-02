package me.yukun.spaceflares.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.yukun.spaceflares.Main;
import me.yukun.spaceflares.config.FlareConfig;
import me.yukun.spaceflares.flare.FlareFireworkListener;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class Fireworks {

  public static Firework spawnFirework(Location loc, String flare) {
    Firework firework = Objects.requireNonNull(loc.getWorld()).spawn(loc, Firework.class);
    FireworkMeta meta = getFireworkMeta(firework, flare);
    firework.setFireworkMeta(meta);
    detonate(firework);
    return firework;
  }

  private static FireworkMeta getFireworkMeta(Firework firework, String flare) {
    FireworkMeta meta = firework.getFireworkMeta();
    Builder builder = FireworkEffect.builder();
    builder.trail(false);
    builder.flicker(false);
    Type type = FlareConfig.getFlareFireworkType(flare);
    builder.with(type);
    List<Color> colors = FlareConfig.getFlareFireworkColors(flare);
    builder.withColor(colors);
    FireworkEffect effect = builder.build();
    meta.addEffects(effect);
    meta.setPower(0);
    return meta;
  }

  private static void detonate(Firework firework) {
    Bukkit.getServer().getScheduler()
        .scheduleSyncDelayedTask(Main.getPlugin(), () -> {
          firework.detonate();
          FlareFireworkListener.deregisterFirework(firework);
        }, 2L);
  }

  private static final Map<String, Color> nameColorMap = new HashMap<>(17) {{
    put("AQUA", Color.AQUA);
    put("BLACK", Color.BLACK);
    put("BLUE", Color.BLUE);
    put("FUCHSIA", Color.FUCHSIA);
    put("GRAY", Color.GRAY);
    put("GREEN", Color.GREEN);
    put("LIME", Color.LIME);
    put("MAROON", Color.MAROON);
    put("NAVY", Color.NAVY);
    put("OLIVE", Color.OLIVE);
    put("ORANGE", Color.ORANGE);
    put("PURPLE", Color.PURPLE);
    put("RED", Color.RED);
    put("SILVER", Color.SILVER);
    put("TEAL", Color.TEAL);
    put("WHITE", Color.WHITE);
    put("YELLOW", Color.YELLOW);
  }};

  public static Color getColor(String name) {
    return nameColorMap.get(name);
  }

  public static Type getType(String name) {
    return Type.valueOf(name);
  }
}
