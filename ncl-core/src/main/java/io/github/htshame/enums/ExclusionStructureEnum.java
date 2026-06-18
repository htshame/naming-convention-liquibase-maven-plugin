package io.github.htshame.enums;

/**
 * Exclusion XML tags and attributes.
 */
public enum ExclusionStructureEnum {
    /**
     * Represents 'fileName' attribute.
     */
    FILE_NAME_ATTR("fileName"),
    /**
     * Represents 'changeSetId' attribute.
     */
    CHANGESET_ID_ATTR("changeSetId"),
    /**
     * Represents 'changeSetAuthor' attribute.
     */
    CHANGESET_AUTHOR_ATTR("changeSetAuthor"),
    /**
     * Represents 'rule' attribute.
     */
    RULE_ATTR("rule");

    private final String value;

    /**
     * Constructor.
     *
     * @param value - value.
     */
    ExclusionStructureEnum(final String value) {
        this.value = value;
    }

    /**
     * Get enum value.
     *
     * @return enum value.
     */
    public String getValue() {
        return value;
    }
}
