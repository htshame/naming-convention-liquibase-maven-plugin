package com.github.htshame.enums;

/**
 * List of available rules.
 */
public enum RuleEnum {

    ATTRIBUTE_NOT_STARTS_WITH("attr-not-starts-with"),
    ATTRIBUTE_NOT_ENDS_WITH("attr-not-ends-with"),
    NO_HYPHENS_IN_ATTRIBUTES("no-hyphens-in-attributes"),
    TAG_MUST_EXIST("tag-must-exist");

    private final String rule;

    /**
     * Constructor.
     *
     * @param rule - rule.
     */
    RuleEnum(final String rule) {
        this.rule = rule;
    }

    public String getValue() {
        return rule;
    }

    /**
     * Get enum from string.
     *
     * @param value - enum string value.
     * @return enum.
     */
    public static RuleEnum fromValue(final String value) {
        for (RuleEnum constant : RuleEnum.values()) {
            if (constant.getValue().equals(value)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("No enum constant found for value: " + value);
    }
}
