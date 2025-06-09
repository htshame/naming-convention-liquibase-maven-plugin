package com.github.htshame.enums;

/**
 * List of available rules.
 */
public enum RuleEnum {

    ATTRIBUTE_STARTS_WITH("attr-starts-with"),
    ATTRIBUTE_ENDS_WITH("attr-ends-with-conditioned"),
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
