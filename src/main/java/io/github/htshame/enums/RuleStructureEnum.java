package io.github.htshame.enums;

/**
 * Rule XML contents.
 */
public enum RuleStructureEnum {
    /**
     * Represents 'rule' tag.
     */
    RULE_TAG("rule"),
    /**
     * Represents 'rule.name' attribute.
     */
    NAME_ATTR("name"),
    /**
     * Represents 'requiredTag' tag.
     */
    REQUIRED_TAG("requiredTag"),
    /**
     * Represents 'excludedTags' tags.
     */
    EXCLUDED_TAGS("excludedTags"),
    /**
     * Represents 'excludedAttrs' tags.
     */
    EXCLUDED_ATTRS("excludedAttrs"),
    /**
     * Represents 'tag' tag.
     */
    TAG_TAG("tag"),
    /**
     * Represents 'attr' tag.
     */
    ATTR_TAG("attr"),
    /**
     * Represents 'targetAttribute' tag.
     */
    TARGET_ATTRIBUTE_TAG("targetAttribute"),
    /**
     * Represents 'requiredPrefix' tag.
     */
    REQUIRED_PREFIX_TAG("requiredPrefix"),
    /**
     * Represents 'conditionAttribute' tag.
     */
    CONDITION_ATTRIBUTE_TAG("conditionAttribute"),
    /**
     * Represents 'conditionValue' tag.
     */
    CONDITION_VALUE_TAG("conditionValue"),
    /**
     * Represents 'requiredSuffix' tag.
     */
    REQUIRED_SUFFIX_TAG("requiredSuffix");

    private final String value;

    /**
     * Constructor.
     *
     * @param value - value.
     */
    RuleStructureEnum(final String value) {
        this.value = value;
    }

    /**
     * Get value.
     *
     * @return enum value.
     */
    public String getValue() {
        return this.value;
    }
}
