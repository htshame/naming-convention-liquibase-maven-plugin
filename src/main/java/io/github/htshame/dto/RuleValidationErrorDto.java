package io.github.htshame.dto;

import io.github.htshame.enums.RuleEnum;

/**
 * DTO to store validation error data.
 */
public class RuleValidationErrorDto {

    private RuleEnum rule;

    private String changeLogFileName;

    private String changeSetId;

    private String changeSetAuthor;

    private String errorMessage;

    private String genericMessage;

    /**
     * Edge-case constructor when changeLog parsing fails.
     *
     * @param genericMessage - generic message.
     */
    public RuleValidationErrorDto(final String genericMessage) {
        this.genericMessage = genericMessage;
    }

    /**
     * Constructor.
     *
     * @param rule              - validation rule.
     * @param errorMessage      - human-readable error message.
     * @param changeLogFileName - changeLog file name.
     */
    public RuleValidationErrorDto(
            final RuleEnum rule,
            final String errorMessage,
            final String changeLogFileName) {
        this.rule = rule;
        this.errorMessage = errorMessage;
        this.changeLogFileName = changeLogFileName;
    }

    /**
     * Constructor.
     *
     * @param rule              - validation rule.
     * @param changeSetId       - changeSet ID.
     * @param changeSetAuthor   - changeSet author.
     * @param changeLogFileName - changeLog file name.
     * @param errorMessage      - human-readable error message.
     */
    public RuleValidationErrorDto(final RuleEnum rule,
                                  final String changeSetId,
                                  final String changeSetAuthor,
                                  final String changeLogFileName,
                                  final String errorMessage) {
        this.rule = rule;
        this.changeSetId = changeSetId;
        this.changeSetAuthor = changeSetAuthor;
        this.changeLogFileName = changeLogFileName;
        this.errorMessage = errorMessage;
    }

    /**
     * Get rule.
     *
     * @return rule.
     */
    public RuleEnum getRule() {
        return rule;
    }

    /**
     * Set rule.
     *
     * @param rule - rule.
     */
    public void setRule(final RuleEnum rule) {
        this.rule = rule;
    }

    /**
     * Get changeLog file name.
     *
     * @return changeLog file name.
     */
    public String getChangeLogFileName() {
        return changeLogFileName;
    }

    /**
     * Set changeLog file name.
     *
     * @param changeLogFileName - changeLog file name.
     */
    public void setChangeLogFileName(final String changeLogFileName) {
        this.changeLogFileName = changeLogFileName;
    }

    /**
     * Get changeSet ID.
     *
     * @return changeSet ID.
     */
    public String getChangeSetId() {
        return changeSetId;
    }

    /**
     * Set changeSet id.
     *
     * @param changeSetId - changeSet ID.
     */
    public void setChangeSetId(final String changeSetId) {
        this.changeSetId = changeSetId;
    }

    /**
     * Get changeSet author.
     *
     * @return changeSet author.
     */
    public String getChangeSetAuthor() {
        return changeSetAuthor;
    }

    /**
     * Set changeSet author.
     *
     * @param changeSetAuthor - changeSet author.
     */
    public void setChangeSetAuthor(final String changeSetAuthor) {
        this.changeSetAuthor = changeSetAuthor;
    }

    /**
     * Get human-readable error message.
     *
     * @return human-readable error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set human-readable error message.
     *
     * @param errorMessage - human-readable error message.
     */
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Get generic message.
     *
     * @return generic message.
     */
    public String getGenericMessage() {
        return genericMessage;
    }

    /**
     * Set generic message.
     *
     * @param genericMessage - generic message.
     */
    public void setGenericMessage(final String genericMessage) {
        this.genericMessage = genericMessage;
    }
}
