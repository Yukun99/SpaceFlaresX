package me.yukun.spaceflares.config.validator;

import me.yukun.spaceflares.config.ConfigTypeEnum;
import me.yukun.spaceflares.config.FieldTypeEnum;

public class ValidationException extends Exception {

  public ValidationException(String errorMessage) {
    super(errorMessage);
  }

  public static String getErrorMessage(ConfigTypeEnum configType, FieldTypeEnum fieldType,
      String item) {
    return "Error in " + configType + ": " + fieldType + " not found at " + item + ".";
  }

  public static String getPlaceholderErrorMessage(ConfigTypeEnum configType,
      FieldTypeEnum fieldType, String item, String placeholder) {
    return "Error in " + configType + ": Invalid placeholder " + placeholder + " found in "
        + fieldType + " at " + item + ".";
  }
}
