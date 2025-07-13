package io.github.htshame.util;

import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;

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
        attrEndsWithConditionedMap.put(
                ChangeLogFormatEnum.YML,
                "Key [%s] with %s=\"%s\" must have [%s] ending with [%s], but found: [%s]");
        attrEndsWithConditionedMap.put(
                ChangeLogFormatEnum.JSON,
                "Object [%s] with %s=\"%s\" must have property [%s] ending with [%s], but found: [%s]");

        EnumMap<ChangeLogFormatEnum, String> attrNotEndsWithConditionedMap = new EnumMap<>(ChangeLogFormatEnum.class);
        attrNotEndsWithConditionedMap.put(
                ChangeLogFormatEnum.XML,
                "Tag <%s> with %s=\"%s\" must not have [%s] ending with [%s], but found: [%s]");
        attrNotEndsWithConditionedMap.put(
                ChangeLogFormatEnum.YAML,
                "Key [%s] with %s=\"%s\" must not have [%s] ending with [%s], but found: [%s]");
        attrNotEndsWithConditionedMap.put(
                ChangeLogFormatEnum.YML,
                "Key [%s] with %s=\"%s\" must not have [%s] ending with [%s], but found: [%s]");
        attrNotEndsWithConditionedMap.put(
                ChangeLogFormatEnum.JSON,
                "Object [%s] with %s=\"%s\" must not have property [%s] ending with [%s], but found: [%s]");

        EnumMap<ChangeLogFormatEnum, String> attrStartsWithConditionedMap = new EnumMap<>(ChangeLogFormatEnum.class);
        attrStartsWithConditionedMap.put(
                ChangeLogFormatEnum.XML,
                "Tag <%s> with %s=\"%s\" must have [%s] starting with [%s], but found: [%s]");
        attrStartsWithConditionedMap.put(
                ChangeLogFormatEnum.YAML,
                "Key [%s] with %s=\"%s\" must have [%s] starting with [%s], but found: [%s]");
        attrStartsWithConditionedMap.put(
                ChangeLogFormatEnum.YML,
                "Key [%s] with %s=\"%s\" must have [%s] starting with [%s], but found: [%s]");
        attrStartsWithConditionedMap.put(
                ChangeLogFormatEnum.JSON,
                "Object [%s] with %s=\"%s\" must have property [%s] starting with [%s], but found: [%s]");

        EnumMap<ChangeLogFormatEnum, String> attrNotStartsWithConditionedMap = new EnumMap<>(ChangeLogFormatEnum.class);
        attrNotStartsWithConditionedMap.put(
                ChangeLogFormatEnum.XML,
                "Tag <%s> with %s=\"%s\" must not have [%s] starting with [%s], but found: [%s]");
        attrNotStartsWithConditionedMap.put(
                ChangeLogFormatEnum.YAML,
                "Key [%s] with %s=\"%s\" must not have [%s] starting with [%s], but found: [%s]");
        attrNotStartsWithConditionedMap.put(
                ChangeLogFormatEnum.YML,
                "Key [%s] with %s=\"%s\" must not have [%s] starting with [%s], but found: [%s]");
        attrNotStartsWithConditionedMap.put(
                ChangeLogFormatEnum.JSON,
                "Object [%s] with %s=\"%s\" must not have property [%s] starting with [%s], but found: [%s]");

        EnumMap<ChangeLogFormatEnum, String> attrEndsWithMap = new EnumMap<>(ChangeLogFormatEnum.class);
        attrEndsWithMap.put(
                ChangeLogFormatEnum.XML,
                "Tag <%s> must have [%s] ending with [%s], but found: [%s]");
        attrEndsWithMap.put(
                ChangeLogFormatEnum.YAML,
                "Key [%s] must have [%s] ending with [%s], but found: [%s]");
        attrEndsWithMap.put(
                ChangeLogFormatEnum.YML,
                "Key [%s] must have [%s] ending with [%s], but found: [%s]");
        attrEndsWithMap.put(
                ChangeLogFormatEnum.JSON,
                "Object [%s] must have property [%s] ending with [%s], but found: [%s]");

        EnumMap<ChangeLogFormatEnum, String> attrStartsWithMap = new EnumMap<>(ChangeLogFormatEnum.class);
        attrStartsWithMap.put(
                ChangeLogFormatEnum.XML,
                "<%s %s=\"%s\"> must start with [%s]");
        attrStartsWithMap.put(
                ChangeLogFormatEnum.YAML,
                "Key [%s]. Property %s: %s must start with [%s]");
        attrStartsWithMap.put(
                ChangeLogFormatEnum.YML,
                "Key [%s]. Property %s: %s must start with [%s]");
        attrStartsWithMap.put(
                ChangeLogFormatEnum.JSON,
                "Object [%s]. Property %s: %s must start with [%s]");

        EnumMap<ChangeLogFormatEnum, String> noHyphensInAttributesMap = new EnumMap<>(ChangeLogFormatEnum.class);
        noHyphensInAttributesMap.put(
                ChangeLogFormatEnum.XML,
                "Attribute [%s] of tag <%s> contains hyphen in value: [%s]");
        noHyphensInAttributesMap.put(
                ChangeLogFormatEnum.YAML,
                "Property [%s] of key [%s] contains hyphen in value: [%s]");
        noHyphensInAttributesMap.put(
                ChangeLogFormatEnum.YML,
                "Property [%s] of key [%s] contains hyphen in value: [%s]");
        noHyphensInAttributesMap.put(
                ChangeLogFormatEnum.JSON,
                "Property [%s] of object [%s] contains hyphen in value: [%s]");

        EnumMap<ChangeLogFormatEnum, String> noUnderscoresInAttributesMap = new EnumMap<>(ChangeLogFormatEnum.class);
        noUnderscoresInAttributesMap.put(
                ChangeLogFormatEnum.XML,
                "Attribute [%s] of tag <%s> contains underscore in value: [%s]");
        noUnderscoresInAttributesMap.put(
                ChangeLogFormatEnum.YAML,
                "Property [%s] of key [%s] contains underscore in value: [%s]");
        noUnderscoresInAttributesMap.put(
                ChangeLogFormatEnum.YML,
                "Property [%s] of key [%s] contains underscore in value: [%s]");
        noUnderscoresInAttributesMap.put(
                ChangeLogFormatEnum.JSON,
                "Property [%s] of object [%s] contains underscore in value: [%s]");

        EnumMap<ChangeLogFormatEnum, String> tagMustExistMap = new EnumMap<>(ChangeLogFormatEnum.class);
        tagMustExistMap.put(
                ChangeLogFormatEnum.XML,
                "Tag <%s>. Required nested tag <%s> is missing or empty");
        tagMustExistMap.put(
                ChangeLogFormatEnum.YAML,
                "Key [%s]. Required nested property [%s] is missing or empty");
        tagMustExistMap.put(
                ChangeLogFormatEnum.YML,
                "Key [%s]. Required nested property [%s] is missing or empty");
        tagMustExistMap.put(
                ChangeLogFormatEnum.JSON,
                "Object [%s]. Required nested property [%s] is missing or empty");

        EnumMap<ChangeLogFormatEnum, String> noUppercaseInAttrsMap = new EnumMap<>(ChangeLogFormatEnum.class);
        noUppercaseInAttrsMap.put(
                ChangeLogFormatEnum.XML,
                "Attribute [%s] of tag <%s> contains uppercase characters in value: [%s]");
        noUppercaseInAttrsMap.put(
                ChangeLogFormatEnum.YAML,
                "Property [%s] of key [%s] contains uppercase characters in value: [%s]");
        noUppercaseInAttrsMap.put(
                ChangeLogFormatEnum.YML,
                "Property [%s] of key [%s] contains uppercase characters in value: [%s]");
        noUppercaseInAttrsMap.put(
                ChangeLogFormatEnum.JSON,
                "Property [%s] of object [%s] contains uppercase characters in value: [%s]");

        EnumMap<ChangeLogFormatEnum, String> noLowercaseInAttrsMap = new EnumMap<>(ChangeLogFormatEnum.class);
        noLowercaseInAttrsMap.put(
                ChangeLogFormatEnum.XML,
                "Attribute [%s] of tag <%s> contains lowercase characters in value: [%s]");
        noLowercaseInAttrsMap.put(
                ChangeLogFormatEnum.YAML,
                "Property [%s] of key [%s] contains lowercase characters in value: [%s]");
        noLowercaseInAttrsMap.put(
                ChangeLogFormatEnum.YML,
                "Property [%s] of key [%s] contains lowercase characters in value: [%s]");
        noLowercaseInAttrsMap.put(
                ChangeLogFormatEnum.JSON,
                "Property [%s] of object [%s] contains lowercase characters in value: [%s]");

        EnumMap<ChangeLogFormatEnum, String> noSpacesInAttrsMap = new EnumMap<>(ChangeLogFormatEnum.class);
        noSpacesInAttrsMap.put(
                ChangeLogFormatEnum.XML,
                "Tag <%s>. Attribute [%s] contains whitespace");
        noSpacesInAttrsMap.put(
                ChangeLogFormatEnum.YAML,
                "Key <%s>. Property [%s] contains whitespace");
        noSpacesInAttrsMap.put(
                ChangeLogFormatEnum.YML,
                "Key <%s>. Property [%s] contains whitespace");
        noSpacesInAttrsMap.put(
                ChangeLogFormatEnum.JSON,
                "Object <%s>. Property [%s] contains whitespace");

        EnumMap<ChangeLogFormatEnum, String> attrMustExistInTagMap = new EnumMap<>(ChangeLogFormatEnum.class);
        attrMustExistInTagMap.put(
                ChangeLogFormatEnum.XML,
                "Tag <%s>. Attribute [%s] is missing or empty");
        attrMustExistInTagMap.put(
                ChangeLogFormatEnum.YAML,
                "Key <%s>. Property [%s] is missing or empty");
        attrMustExistInTagMap.put(
                ChangeLogFormatEnum.YML,
                "Key <%s>. Property [%s] is missing or empty");
        attrMustExistInTagMap.put(
                ChangeLogFormatEnum.JSON,
                "Object <%s>. Property [%s] is missing or empty");

        RULE_MESSAGE_MAP.put(RuleEnum.ATTRIBUTE_ENDS_WITH, attrEndsWithMap);
        RULE_MESSAGE_MAP.put(RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED, attrEndsWithConditionedMap);
        RULE_MESSAGE_MAP.put(RuleEnum.ATTRIBUTE_NOT_ENDS_WITH_CONDITIONED, attrNotEndsWithConditionedMap);
        RULE_MESSAGE_MAP.put(RuleEnum.ATTRIBUTE_STARTS_WITH, attrStartsWithMap);
        RULE_MESSAGE_MAP.put(RuleEnum.ATTRIBUTE_STARTS_WITH_CONDITIONED, attrStartsWithConditionedMap);
        RULE_MESSAGE_MAP.put(RuleEnum.ATTRIBUTE_NOT_STARTS_WITH_CONDITIONED, attrNotStartsWithConditionedMap);
        RULE_MESSAGE_MAP.put(RuleEnum.NO_HYPHENS_IN_ATTRIBUTES, noHyphensInAttributesMap);
        RULE_MESSAGE_MAP.put(RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES, noUnderscoresInAttributesMap);
        RULE_MESSAGE_MAP.put(RuleEnum.NO_UPPERCASE_IN_ATTRIBUTES, noUppercaseInAttrsMap);
        RULE_MESSAGE_MAP.put(RuleEnum.NO_LOWERCASE_IN_ATTRIBUTES, noLowercaseInAttrsMap);
        RULE_MESSAGE_MAP.put(RuleEnum.NO_SPACES_IN_ATTRIBUTES, noSpacesInAttrsMap);
        RULE_MESSAGE_MAP.put(RuleEnum.TAG_MUST_EXIST, tagMustExistMap);
        RULE_MESSAGE_MAP.put(RuleEnum.ATTRIBUTE_MUST_EXIST_IN_TAG, attrMustExistInTagMap);
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

    /**
     * Compose validation error message.
     *
     * @param rule          - rule name.
     * @param ruleStructure - rule tag.
     * @return error message.
     */
    public static String validationErrorMessage(final RuleEnum rule,
                                                final RuleStructureEnum ruleStructure) {
        return String.format("Rule '%s'. <%s> must not be null", rule.getValue(), ruleStructure.getValue());
    }
}
