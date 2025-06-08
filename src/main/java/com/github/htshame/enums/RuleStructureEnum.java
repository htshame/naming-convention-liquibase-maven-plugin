package com.github.htshame.enums;

/**
 * Rule XML contents.
 */
public enum RuleStructureEnum {
    RULE_TAG("rule"),
    NAME_ATTR("name"),
    REQUIRED_TAG("requiredTag"),
    EXCLUDED_ANCESTOR_TAGS("excludedAncestorTags"),
    TAG_TAG("tag"),
    TARGET_ATTRIBUTE_TAG("targetAttribute"),
    REQUIRED_PREFIX_TAG("requiredPrefix"),
    CONDITION_ATTRIBUTE_TAG("conditionAttribute"),
    CONDITION_VALUE_TAG("conditionValue"),
    REQUIRED_SUFFIX_TAG("requiredSuffix");

    private final String value;

    RuleStructureEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
