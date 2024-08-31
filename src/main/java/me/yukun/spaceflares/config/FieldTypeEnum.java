package me.yukun.spaceflares.config;

public enum FieldTypeEnum {
  STRING("String value"),
  MATERIAL("Material name"),
  INTEGER("Integer value"),
  BOOLEAN("Boolean value"),
  STRINGLIST("String list"),
  SECTION("Section"),
  REWARD("Reward type");

  private final String name;

  FieldTypeEnum(String name) {
    this.name = name;
  }

  public String toString() {
    return this.name;
  }
}
