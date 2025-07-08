package io.github.htshame.enums;

/**
 * List of available rules.
 */
public enum RuleEnum {

    /**
     * Represents 'attr-starts-with' rule.
     */
    ATTRIBUTE_STARTS_WITH("attr-starts-with", RuleTypeEnum.CHANGE_SET_RULE),
    /**
     * Represents 'attr-starts-with-conditioned' rule.
     */
    ATTRIBUTE_STARTS_WITH_CONDITIONED("attr-starts-with-conditioned", RuleTypeEnum.CHANGE_SET_RULE),
    /**
     * Represents 'attr-not-starts-with-conditioned' rule.
     */
    ATTRIBUTE_NOT_STARTS_WITH_CONDITIONED("attr-not-starts-with-conditioned", RuleTypeEnum.CHANGE_SET_RULE),
    /**
     * Represents 'attr-ends-with' rule.
     */
    ATTRIBUTE_ENDS_WITH("attr-ends-with", RuleTypeEnum.CHANGE_SET_RULE),
    /**
     * Represents 'attr-ends-with-conditioned' rule.
     */
    ATTRIBUTE_ENDS_WITH_CONDITIONED("attr-ends-with-conditioned", RuleTypeEnum.CHANGE_SET_RULE),
    /**
     * Represents 'attr-not-ends-with-conditioned' rule.
     */
    ATTRIBUTE_NOT_ENDS_WITH_CONDITIONED("attr-not-ends-with-conditioned", RuleTypeEnum.CHANGE_SET_RULE),
    /**
     * Represents 'no-hyphens-in-attributes' rule.
     */
    NO_HYPHENS_IN_ATTRIBUTES("no-hyphens-in-attributes", RuleTypeEnum.CHANGE_SET_RULE),
    /**
     * Represents 'no-underscores-in-attributes' rule.
     */
    NO_UNDERSCORES_IN_ATTRIBUTES("no-underscores-in-attributes", RuleTypeEnum.CHANGE_SET_RULE),
    /**
     * Represents 'tag-must-exist' rule.
     */
    TAG_MUST_EXIST("tag-must-exist", RuleTypeEnum.CHANGE_SET_RULE),
    /**
     * Represents 'no-uppercase-in-attributes' rule.
     */
    NO_UPPERCASE_IN_ATTRIBUTES("no-uppercase-in-attributes", RuleTypeEnum.CHANGE_SET_RULE),
    /**
     * Represents 'no-lowercase-in-attributes' rule.
     */
    NO_LOWERCASE_IN_ATTRIBUTES("no-lowercase-in-attributes", RuleTypeEnum.CHANGE_SET_RULE),
    /**
     * Represents 'attr-must-exist-in-tag' rule.
     */
    ATTRIBUTE_MUST_EXIST_IN_TAG("attr-must-exist-in-tag", RuleTypeEnum.CHANGE_SET_RULE),
    /**
     * Represents 'changelog-file-name-must-match-regexp' rule.
     */
    CHANGELOG_FILE_NAME_MUST_MATCH_REGEXP("changelog-file-name-must-match-regexp", RuleTypeEnum.CHANGE_LOG_RULE),
    /**
     * Represents '*' - all rules.
     */
    ALL_RULES("*", RuleTypeEnum.CHANGE_SET_RULE);

    private final String rule;
    private final RuleTypeEnum type;

    /**
     * Constructor.
     *
     * @param rule - rule.
     * @param type - rule type.
     */
    RuleEnum(final String rule,
             final RuleTypeEnum type) {
        this.rule = rule;
        this.type = type;
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
     * Get type.
     *
     * @return enum type.
     */
    public RuleTypeEnum getType() {
        return type;
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
