package me.yukun.spaceflares.config;

public enum ConfigTypeEnum {
  CONFIG("Config.yml"),
  MESSAGES("Messages.yml"),
  REDEEMS("Redeems.yml"),
  FLARES("Flare config files"),
  CRATES("Crate config files");

  private final String filename;

  ConfigTypeEnum(String filename) {
    this.filename = filename;
  }

  public String toString() {
    return this.filename;
  }
}
