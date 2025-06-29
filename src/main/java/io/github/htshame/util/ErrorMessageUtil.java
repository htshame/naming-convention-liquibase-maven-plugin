package io.github.htshame.util;

import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;

import java.util.EnumMap;

/**
 * Util class for getting the appropriate error message, depending on the rule and changeLog format type.
 */
public final class ErrorMessageUtil {

    private static final EnumMap<RuleEnum, EnumMap<ChangeLogFormatEnum, String>> RULE_MESSAGE_MAP =
            new EnumMap<>(RuleEnum.class);

    private ErrorMessageUtil() {

    }

    static {
        EnumMap<ChangeLogFormatEnum, String> attrEndsWithConditionedMap = new EnumMap<>(ChangeLogFormatEnum.class);
        attrEndsWithConditionedMap.put(
                ChangeLogFormatEnum.XML,
                "Tag <%s> with %s=\"%s\" must have [%s] ending with [%s], but found: [%s]");
        attrEndsWithConditionedMap.put(
                ChangeLogFormatEnum.YAML,
                "Key [%s] with %s=\"%s\" must have [%s] ending with [%s], but found: [%s]");

        EnumMap<ChangeLogFormatEnum, String> attrEndsWithMap = new EnumMap<>(ChangeLogFormatEnum.class);
        attrEndsWithMap.put(
                ChangeLogFormatEnum.XML,
                "Tag <%s> must have [%s] ending with [%s], but found: [%s]");
        attrEndsWithMap.put(
                ChangeLogFormatEnum.YAML,
                "Key [%s] must have [%s] ending with [%s], but found: [%s]");

        EnumMap<ChangeLogFormatEnum, String> attrStartsWithMap = new EnumMap<>(ChangeLogFormatEnum.class);
        attrStartsWithMap.put(
                ChangeLogFormatEnum.XML,
                "<%s %s=\"%s\"> must start with [%s]");
        attrStartsWithMap.put(
                ChangeLogFormatEnum.YAML,
                "Key [%s]. Property %s: %s must start with [%s]");

        EnumMap<ChangeLogFormatEnum, String> noHyphensInAttributesMap = new EnumMap<>(ChangeLogFormatEnum.class);
        noHyphensInAttributesMap.put(
                ChangeLogFormatEnum.XML,
                "Attribute [%s] of tag <%s> contains hyphen in value: [%s]");
        noHyphensInAttributesMap.put(
                ChangeLogFormatEnum.YAML,
                "Property [%s] of key [%s] contains hyphen in value: [%s]");

        EnumMap<ChangeLogFormatEnum, String> noUnderscoresInAttributesMap = new EnumMap<>(ChangeLogFormatEnum.class);
        noUnderscoresInAttributesMap.put(
                ChangeLogFormatEnum.XML,
                "Attribute [%s] of tag <%s> contains underscore in value: [%s]");
        noUnderscoresInAttributesMap.put(
                ChangeLogFormatEnum.YAML,
                "Property [%s] of key [%s] contains underscore in value: [%s]");

        EnumMap<ChangeLogFormatEnum, String> tagMustExistMap = new EnumMap<>(ChangeLogFormatEnum.class);
        tagMustExistMap.put(
                ChangeLogFormatEnum.XML,
                "Tag <%s>. Required nested tag <%s> is missing or empty");
        tagMustExistMap.put(
                ChangeLogFormatEnum.YAML,
                "Key [%s]. Required nested property [%s] is missing or empty");

        EnumMap<ChangeLogFormatEnum, String> noUppercaseInAttrsMap = new EnumMap<>(ChangeLogFormatEnum.class);
        noUppercaseInAttrsMap.put(
                ChangeLogFormatEnum.XML,
                "Attribute [%s] of tag <%s> contains uppercase characters in value: [%s]");
        noUppercaseInAttrsMap.put(
                ChangeLogFormatEnum.YAML,
                "Property [%s] of key [%s] contains uppercase characters in value: [%s]");

        EnumMap<ChangeLogFormatEnum, String> noLowercaseInAttrsMap = new EnumMap<>(ChangeLogFormatEnum.class);
        noLowercaseInAttrsMap.put(
                ChangeLogFormatEnum.XML,
                "Attribute [%s] of tag <%s> contains lowercase characters in value: [%s]");
        noLowercaseInAttrsMap.put(
                ChangeLogFormatEnum.YAML,
                "Property [%s] of key [%s] contains lowercase characters in value: [%s]");

        RULE_MESSAGE_MAP.put(RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED, attrEndsWithConditionedMap);
        RULE_MESSAGE_MAP.put(RuleEnum.ATTRIBUTE_ENDS_WITH, attrEndsWithMap);
        RULE_MESSAGE_MAP.put(RuleEnum.ATTRIBUTE_STARTS_WITH, attrStartsWithMap);
        RULE_MESSAGE_MAP.put(RuleEnum.NO_HYPHENS_IN_ATTRIBUTES, noHyphensInAttributesMap);
        RULE_MESSAGE_MAP.put(RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES, noUnderscoresInAttributesMap);
        RULE_MESSAGE_MAP.put(RuleEnum.TAG_MUST_EXIST, tagMustExistMap);
        RULE_MESSAGE_MAP.put(RuleEnum.NO_UPPERCASE_IN_ATTRIBUTES, noUppercaseInAttrsMap);
        RULE_MESSAGE_MAP.put(RuleEnum.NO_LOWERCASE_IN_ATTRIBUTES, noLowercaseInAttrsMap);
    }

    /**
     * Get message.
     *
     * @param ruleEnum            - rule.
     * @param changeLogFormatEnum - changeLog format.
     * @return error message.
     */
    public static String getMessage(final RuleEnum ruleEnum,
                                    final ChangeLogFormatEnum changeLogFormatEnum) {
        return RULE_MESSAGE_MAP.get(ruleEnum).get(changeLogFormatEnum);
    }
}
