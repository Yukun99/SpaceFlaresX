package me.yukun.spaceflares.config.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.yukun.spaceflares.config.ConfigTypeEnum;
import me.yukun.spaceflares.config.FieldTypeEnum;
import org.bukkit.configuration.file.FileConfiguration;

public class MessagesValidator implements IValidator {

  private final List<String> SECTIONS = new ArrayList<>(4) {{
    add("Placeholder");
    add("Despawn");
    add("Envoy");
    add("Envoy.Edit");
  }};

  private final Map<String, FieldTypeEnum> FIELDS = new HashMap<>(43) {{
    put("Prefix", FieldTypeEnum.STRING);
    put("Placeholder.Loc", FieldTypeEnum.STRING);
    put("Placeholder.Player", FieldTypeEnum.STRING);
    put("Placeholder.ETime", FieldTypeEnum.STRING);
    put("Give", FieldTypeEnum.STRING);
    put("GiveFull", FieldTypeEnum.STRING);
    put("Receive", FieldTypeEnum.STRING);
    put("ReceiveFull", FieldTypeEnum.STRING);
    put("NoSummon", FieldTypeEnum.STRING);
    put("Summon", FieldTypeEnum.STRINGLIST);
    put("SummonAll", FieldTypeEnum.STRINGLIST);
    put("Land", FieldTypeEnum.STRINGLIST);
    put("LandAll", FieldTypeEnum.STRINGLIST);
    put("Claim", FieldTypeEnum.STRINGLIST);
    put("ClaimAll", FieldTypeEnum.STRINGLIST);
    put("Despawn.Notify", FieldTypeEnum.STRING);
    put("Despawn.Items", FieldTypeEnum.STRING);
    put("DespawnAll", FieldTypeEnum.STRINGLIST);
    put("Redeem", FieldTypeEnum.STRING);
    put("RedeemFull", FieldTypeEnum.STRING);
    put("Envoy.NoSummon", FieldTypeEnum.STRING);
    put("Envoy.Summon", FieldTypeEnum.STRINGLIST);
    put("Envoy.Start", FieldTypeEnum.STRINGLIST);
    put("Envoy.Claim", FieldTypeEnum.STRING);
    put("Envoy.ClaimAll", FieldTypeEnum.STRING);
    put("Envoy.End", FieldTypeEnum.STRING);
    put("Envoy.Remain", FieldTypeEnum.STRING);
    put("Envoy.Cooldown", FieldTypeEnum.STRING);
    put("Envoy.NoCooldown", FieldTypeEnum.STRING);
    put("Envoy.NoExist", FieldTypeEnum.STRING);
    put("Envoy.List", FieldTypeEnum.STRING);
    put("Envoy.Give", FieldTypeEnum.STRING);
    put("Envoy.GiveFull", FieldTypeEnum.STRING);
    put("Envoy.Receive", FieldTypeEnum.STRING);
    put("Envoy.ReceiveFull", FieldTypeEnum.STRING);
    put("Envoy.Redeem", FieldTypeEnum.STRING);
    put("Envoy.Edit.Editing", FieldTypeEnum.STRING);
    put("Envoy.Edit.Start", FieldTypeEnum.STRING);
    put("Envoy.Edit.Stop", FieldTypeEnum.STRING);
    put("Envoy.Edit.Save", FieldTypeEnum.STRING);
    put("Envoy.Edit.NoSave", FieldTypeEnum.STRING);
    put("Envoy.Edit.Delete", FieldTypeEnum.STRING);
    put("Envoy.Edit.NoEnd", FieldTypeEnum.STRING);
  }};

  private final Map<String, FieldTypeEnum> PLAYER_PLACEHOLDER_FIELDS = new HashMap<>(8) {{
    put("Give", FieldTypeEnum.STRING);
    put("GiveFull", FieldTypeEnum.STRING);
    put("SummonAll", FieldTypeEnum.STRINGLIST);
    put("LandAll", FieldTypeEnum.STRINGLIST);
    put("ClaimAll", FieldTypeEnum.STRINGLIST);
    put("Envoy.ClaimAll", FieldTypeEnum.STRING);
    put("Envoy.Give", FieldTypeEnum.STRING);
    put("Envoy.GiveFull", FieldTypeEnum.STRING);
  }};

  private final Map<String, FieldTypeEnum> TIER_PLACEHOLDER_FIELDS = new HashMap<>(22) {{
    put("Give", FieldTypeEnum.STRING);
    put("Receive", FieldTypeEnum.STRING);
    put("Summon", FieldTypeEnum.STRINGLIST);
    put("SummonAll", FieldTypeEnum.STRINGLIST);
    put("Land", FieldTypeEnum.STRINGLIST);
    put("LandAll", FieldTypeEnum.STRINGLIST);
    put("Claim", FieldTypeEnum.STRINGLIST);
    put("ClaimAll", FieldTypeEnum.STRINGLIST);
    put("Despawn.Notify", FieldTypeEnum.STRING);
    put("Redeem", FieldTypeEnum.STRING);
    put("Envoy.Summon", FieldTypeEnum.STRINGLIST);
    put("Envoy.Start", FieldTypeEnum.STRINGLIST);
    put("Envoy.Claim", FieldTypeEnum.STRING);
    put("Envoy.ClaimAll", FieldTypeEnum.STRING);
    put("Envoy.End", FieldTypeEnum.STRING);
    put("Envoy.Cooldown", FieldTypeEnum.STRING);
    put("Envoy.NoCooldown", FieldTypeEnum.STRING);
    put("Envoy.Give", FieldTypeEnum.STRING);
    put("Envoy.Receive", FieldTypeEnum.STRING);
    put("Envoy.Redeem", FieldTypeEnum.STRING);
    put("Envoy.Edit.Start", FieldTypeEnum.STRING);
    put("Envoy.Edit.Stop", FieldTypeEnum.STRING);
  }};

  private final Map<String, FieldTypeEnum> AMOUNT_PLACEHOLDER_FIELDS = new HashMap<>(10) {{
    put("Give", FieldTypeEnum.STRING);
    put("GiveFull", FieldTypeEnum.STRING);
    put("Receive", FieldTypeEnum.STRING);
    put("ReceiveFull", FieldTypeEnum.STRING);
    put("Redeem", FieldTypeEnum.STRING);
    put("Envoy.Give", FieldTypeEnum.STRING);
    put("Envoy.GiveFull", FieldTypeEnum.STRING);
    put("Envoy.Receive", FieldTypeEnum.STRING);
    put("Envoy.ReceiveFull", FieldTypeEnum.STRING);
    put("Envoy.Redeem", FieldTypeEnum.STRING);
  }};

  private final Map<String, FieldTypeEnum> TIME_PLACEHOLDER_FIELDS = new HashMap<>(2) {{
    put("Land", FieldTypeEnum.STRINGLIST);
    put("LandAll", FieldTypeEnum.STRINGLIST);
  }};

  private final Map<String, FieldTypeEnum> LOC_PLACEHOLDER_FIELDS = new HashMap<>(9) {{
    put("Summon", FieldTypeEnum.STRINGLIST);
    put("SummonAll", FieldTypeEnum.STRINGLIST);
    put("Land", FieldTypeEnum.STRINGLIST);
    put("LandAll", FieldTypeEnum.STRINGLIST);
    put("Claim", FieldTypeEnum.STRINGLIST);
    put("ClaimAll", FieldTypeEnum.STRINGLIST);
    put("Despawn.Notify", FieldTypeEnum.STRING);
    put("DespawnAll", FieldTypeEnum.STRINGLIST);
    put("Envoy.ClaimAll", FieldTypeEnum.STRING);
  }};

  private final Map<String, FieldTypeEnum> REMAIN_PLACEHOLDER_FIELDS = new HashMap<>(5) {{
    put("Envoy.Summon", FieldTypeEnum.STRINGLIST);
    put("Envoy.Start", FieldTypeEnum.STRINGLIST);
    put("Envoy.Claim", FieldTypeEnum.STRING);
    put("Envoy.ClaimAll", FieldTypeEnum.STRING);
    put("Envoy.Remain", FieldTypeEnum.STRING);
  }};

  private final Map<String, FieldTypeEnum> E_TIME_PLACEHOLDER_FIELDS = new HashMap<>(5) {{
    put("Envoy.Summon", FieldTypeEnum.STRINGLIST);
    put("Envoy.Start", FieldTypeEnum.STRINGLIST);
    put("Envoy.End", FieldTypeEnum.STRING);
    put("Envoy.Remain", FieldTypeEnum.STRING);
    put("Envoy.Cooldown", FieldTypeEnum.STRING);
  }};

  public void validate(FileConfiguration messages) throws ValidationException {
    validateSections(messages);
    validateFields(messages);
  }

  private void validateSections(FileConfiguration messages) throws ValidationException {
    for (String section : SECTIONS) {
      if (!messages.isConfigurationSection(section)) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.MESSAGES, FieldTypeEnum.SECTION,
                section));
      }
    }
  }

  private void validateFields(FileConfiguration messages) throws ValidationException {
    for (String field : FIELDS.keySet()) {
      validatePlayerField(messages, field);
      validateTierField(messages, field);
      validateAmountField(messages, field);
      validateTimeField(messages, field);
      validateLocField(messages, field);
      validateRemainField(messages, field);
      validateETimeField(messages, field);
    }
  }

  private void validatePlayerField(FileConfiguration messages, String field)
      throws ValidationException {
    if (PLAYER_PLACEHOLDER_FIELDS.containsKey(field)) {
      return;
    }
    if (PLAYER_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRING) {
      validatePlayerStringField(messages, field);
    }
    if (PLAYER_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRINGLIST) {
      validatePlayerStringListField(messages, field);
    }
  }

  private void validatePlayerStringField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%player%";
    if (Objects.requireNonNull(messages.getString(field)).contains(placeholder)) {
      throw new ValidationException(
          ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
              FieldTypeEnum.STRING, field, placeholder));
    }
  }

  private void validatePlayerStringListField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%player%";
    for (String line : messages.getStringList(field)) {
      if (line.contains(placeholder)) {
        throw new ValidationException(
            ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
                FieldTypeEnum.STRINGLIST, field, placeholder));
      }
    }
  }

  private void validateTierField(FileConfiguration messages, String field)
      throws ValidationException {
    if (TIER_PLACEHOLDER_FIELDS.containsKey(field)) {
      return;
    }
    if (TIER_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRING) {
      validateTierStringField(messages, field);
    }
    if (TIER_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRINGLIST) {
      validateTierStringListField(messages, field);
    }
  }

  private void validateTierStringField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%tier%";
    if (Objects.requireNonNull(messages.getString(field)).contains(placeholder)) {
      throw new ValidationException(
          ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
              FieldTypeEnum.STRING, field, placeholder));
    }
  }

  private void validateTierStringListField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%tier%";
    for (String line : messages.getStringList(field)) {
      if (line.contains(placeholder)) {
        throw new ValidationException(
            ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
                FieldTypeEnum.STRINGLIST, field, placeholder));
      }
    }
  }

  private void validateAmountField(FileConfiguration messages, String field)
      throws ValidationException {
    if (AMOUNT_PLACEHOLDER_FIELDS.containsKey(field)) {
      return;
    }
    if (AMOUNT_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRING) {
      validateAmountStringField(messages, field);
    }
    if (AMOUNT_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRINGLIST) {
      validateAmountStringListField(messages, field);
    }
  }

  private void validateAmountStringField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%amount%";
    if (Objects.requireNonNull(messages.getString(field)).contains(placeholder)) {
      throw new ValidationException(
          ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
              FieldTypeEnum.STRING, field, placeholder));
    }
  }

  private void validateAmountStringListField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%amount%";
    for (String line : messages.getStringList(field)) {
      if (line.contains(placeholder)) {
        throw new ValidationException(
            ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
                FieldTypeEnum.STRINGLIST, field, placeholder));
      }
    }
  }

  private void validateTimeField(FileConfiguration messages, String field)
      throws ValidationException {
    if (TIME_PLACEHOLDER_FIELDS.containsKey(field)) {
      return;
    }
    if (TIME_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRING) {
      validateTimeStringField(messages, field);
    }
    if (TIME_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRINGLIST) {
      validateTimeStringListField(messages, field);
    }
  }

  private void validateTimeStringField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%time%";
    if (Objects.requireNonNull(messages.getString(field)).contains(placeholder)) {
      throw new ValidationException(
          ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
              FieldTypeEnum.STRING, field, placeholder));
    }
  }

  private void validateTimeStringListField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%time%";
    for (String line : messages.getStringList(field)) {
      if (line.contains(placeholder)) {
        throw new ValidationException(
            ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
                FieldTypeEnum.STRINGLIST, field, placeholder));
      }
    }
  }

  private void validateLocField(FileConfiguration messages, String field)
      throws ValidationException {
    if (LOC_PLACEHOLDER_FIELDS.containsKey(field)) {
      return;
    }
    if (LOC_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRING) {
      validateLocStringField(messages, field);
    }
    if (LOC_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRINGLIST) {
      validateLocStringListField(messages, field);
    }
  }

  private void validateLocStringField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%loc%";
    if (Objects.requireNonNull(messages.getString(field)).contains(placeholder)) {
      throw new ValidationException(
          ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
              FieldTypeEnum.STRING, field, placeholder));
    }
  }

  private void validateLocStringListField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%loc%";
    for (String line : messages.getStringList(field)) {
      if (line.contains(placeholder)) {
        throw new ValidationException(
            ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
                FieldTypeEnum.STRINGLIST, field, placeholder));
      }
    }
  }

  private void validateRemainField(FileConfiguration messages, String field)
      throws ValidationException {
    if (REMAIN_PLACEHOLDER_FIELDS.containsKey(field)) {
      return;
    }
    if (REMAIN_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRING) {
      validateRemainStringField(messages, field);
    }
    if (REMAIN_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRINGLIST) {
      validateRemainStringListField(messages, field);
    }
  }

  private void validateRemainStringField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%remain%";
    if (Objects.requireNonNull(messages.getString(field)).contains(placeholder)) {
      throw new ValidationException(
          ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
              FieldTypeEnum.STRING, field, placeholder));
    }
  }

  private void validateRemainStringListField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%remain%";
    for (String line : messages.getStringList(field)) {
      if (line.contains(placeholder)) {
        throw new ValidationException(
            ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
                FieldTypeEnum.STRINGLIST, field, placeholder));
      }
    }
  }

  private void validateETimeField(FileConfiguration messages, String field)
      throws ValidationException {
    if (E_TIME_PLACEHOLDER_FIELDS.containsKey(field)) {
      return;
    }
    if (E_TIME_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRING) {
      validateETimeStringField(messages, field);
    }
    if (E_TIME_PLACEHOLDER_FIELDS.get(field) == FieldTypeEnum.STRINGLIST) {
      validateETimeStringListField(messages, field);
    }
  }

  private void validateETimeStringField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%e_time%";
    if (Objects.requireNonNull(messages.getString(field)).contains(placeholder)) {
      throw new ValidationException(
          ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
              FieldTypeEnum.STRING, field, placeholder));
    }
  }

  private void validateETimeStringListField(FileConfiguration messages, String field)
      throws ValidationException {
    String placeholder = "%e_time%";
    for (String line : messages.getStringList(field)) {
      if (line.contains(placeholder)) {
        throw new ValidationException(
            ValidationException.getPlaceholderErrorMessage(ConfigTypeEnum.MESSAGES,
                FieldTypeEnum.STRINGLIST, field, placeholder));
      }
    }
  }
}
