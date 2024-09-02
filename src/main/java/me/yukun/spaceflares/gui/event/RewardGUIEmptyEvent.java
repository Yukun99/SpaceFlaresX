package me.yukun.spaceflares.gui.event;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RewardGUIEmptyEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  Block block;

  public RewardGUIEmptyEvent(Block block) {
    this.block = block;
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  @SuppressWarnings("unused")
  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  public Block getBlock() {
    return block;
  }
}
