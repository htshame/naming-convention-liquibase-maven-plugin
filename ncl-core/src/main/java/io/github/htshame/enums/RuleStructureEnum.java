package io.github.htshame.enums;

/**
 * Rule XML contents.
 */
public enum RuleStructureEnum {
    /**
     * Represents 'rule' tag.
     */
    RULE("rule"),
    /**
     * Represents 'rule.name' attribute.
     */
    NAME_ATTR("name"),
    /**
     * Represents 'requiredTag' tag.
     */
    REQUIRED_TAG("requiredTag"),
    /**
     * Represents 'requiredForChildTags' tags.
     */
    REQUIRED_FOR_CHILD_TAGS("requiredForChildTags"),
    /**
     * Represents 'excludedAttrs' tags.
     */
    EXCLUDED_ATTRS("excludedAttrs"),
    /**
     * Represents 'tag' tag.
     */
    TAG("tag"),
    /**
     * Represents 'attr' tag.
     */
    ATTR("attr"),
    /**
     * Represents 'targetAttr' tag.
     */
    TARGET_ATTR("targetAttr"),
    /**
     * Represents 'requiredPrefix' tag.
     */
    REQUIRED_PREFIX("requiredPrefix"),
    /**
     * Represents 'forbiddenPrefix' tag.
     */
    FORBIDDEN_PREFIX("forbiddenPrefix"),
    /**
     * Represents 'conditionAttr' tag.
     */
    CONDITION_ATTR("conditionAttr"),
    /**
     * Represents 'conditionValue' tag.
     */
    CONDITION_VALUE("conditionValue"),
    /**
     * Represents 'requiredSuffix' tag.
     */
    REQUIRED_SUFFIX("requiredSuffix"),
    /**
     * Represents 'forbiddenSuffix' tag.
     */
    FORBIDDEN_SUFFIX("forbiddenSuffix"),
    /**
     * Represents 'requiredAttr' tag.
     */
    REQUIRED_ATTR("requiredAttr"),
    /**
     * Represents 'fileNameRegexp' tag.
     */
    FILE_NAME_REGEXP("fileNameRegexp"),
    /**
     * Represents 'excludedFileNames' tag.
     */
    EXCLUDED_FILE_NAMES("excludedFileNames"),
    /**
     * Represents 'fileName' tag.
     */
    FILE_NAME("fileName"),
    /**
     * Represents 'targetFileName' tag.
     */
    TARGET_FILE_NAME("targetFileName"),
    /**
     * Represents 'linesLimit' tag.
     */
    LINES_LIMIT("linesLimit");

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
