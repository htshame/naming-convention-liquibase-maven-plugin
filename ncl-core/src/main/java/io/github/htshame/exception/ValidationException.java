package io.github.htshame.exception;

import io.github.htshame.dto.RuleValidationErrorDto;

/**
 * Validation exception. Thrown in case changeLog validation fails.
 */
public class ValidationException extends Exception {

    /**
     * Rule validation error details.
     */
    private final RuleValidationErrorDto ruleValidationError;

    /**
     * Constructor.
     *
     * @param ruleValidationErrorDto - rule validation error details.
     */
    public ValidationException(final RuleValidationErrorDto ruleValidationErrorDto) {
        super(ruleValidationErrorDto.getErrorMessage());
        this.ruleValidationError = ruleValidationErrorDto;
    }

    /**
     * Get rule validation error details.
     *
     * @return rule validation error details.
     */
    public RuleValidationErrorDto getRuleValidationError() {
        return ruleValidationError;
    }
}
