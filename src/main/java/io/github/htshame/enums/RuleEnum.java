package io.github.htshame.enums;

/**
 * List of available rules.
 */
public enum RuleEnum {

    /**
     * Represents 'attr-starts-with' rule.
     */
    ATTRIBUTE_STARTS_WITH("attr-starts-with"),
    /**
     * Represents 'attr-starts-with-conditioned' rule.
     */
    ATTRIBUTE_STARTS_WITH_CONDITIONED("attr-starts-with-conditioned"),
    /**
     * Represents 'attr-ends-with' rule.
     */
    ATTRIBUTE_ENDS_WITH("attr-ends-with"),
    /**
     * Represents 'attr-ends-with-conditioned' rule.
     */
    ATTRIBUTE_ENDS_WITH_CONDITIONED("attr-ends-with-conditioned"),
    /**
     * Represents 'no-hyphens-in-attributes' rule.
     */
    NO_HYPHENS_IN_ATTRIBUTES("no-hyphens-in-attributes"),
    /**
     * Represents 'no-underscores-in-attributes' rule.
     */
    NO_UNDERSCORES_IN_ATTRIBUTES("no-underscores-in-attributes"),
    /**
     * Represents 'tag-must-exist' rule.
     */
    TAG_MUST_EXIST("tag-must-exist"),
    /**
     * Represents 'no-uppercase-in-attributes' rule.
     */
    NO_UPPERCASE_IN_ATTRIBUTES("no-uppercase-in-attributes"),
    /**
     * Represents 'no-lowercase-in-attributes' rule.
     */
    NO_LOWERCASE_IN_ATTRIBUTES("no-lowercase-in-attributes"),
    /**
     * Represents 'attr-must-exist-in-tag' rule.
     */
    ATTRIBUTE_MUST_EXIST_IN_TAG("attr-must-exist-in-tag"),
    /**
     * Represents '*' - all rules.
     */
    ALL_RULES("*");

    private final String rule;

    /**
     * Constructor.
     *
     * @param rule - rule.
     */
    RuleEnum(final String rule) {
        this.rule = rule;
    }

    /**
     * Get value.
     *
     * @return enum value.
     */
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
        for (RuleEnum ruleEnum : RuleEnum.values()) {
            if (ruleEnum.getValue().equals(value)) {
                return ruleEnum;
            }
        }
        throw new IllegalArgumentException("No enum constant found for value: " + value);
    }
}
