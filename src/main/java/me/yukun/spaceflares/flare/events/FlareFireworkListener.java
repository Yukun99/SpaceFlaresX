package me.yukun.spaceflares.flare.events;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FlareFireworkListener implements Listener {

  private static final Set<Firework> fireworks = new HashSet<>();

  public static void registerFirework(Firework firework) {
    fireworks.add(firework);
  }

  public static void deregisterFirework(Firework firework) {
    fireworks.remove(firework);
  }

  @EventHandler
  private void flareFireworkDamageEvent(EntityDamageByEntityEvent e) {
    if (!isFlareFireworkDamageEvent(e)) {
      return;
    }
    e.setCancelled(true);
  }

  private boolean isFlareFireworkDamageEvent(EntityDamageByEntityEvent e) {
    if (!(e.getDamager() instanceof Firework firework)) {
      return false;
    }
    return fireworks.contains(firework);
  }
}
