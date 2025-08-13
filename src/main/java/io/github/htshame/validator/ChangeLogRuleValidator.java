package io.github.htshame.validator;

import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.parser.rule.ChangeLogFileRule;
import io.github.htshame.rule.Rule;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ChangeLog rule validator.
 */
public class ChangeLogRuleValidator implements RuleValidator {

    private final Set<ChangeLogFileRule> rules = new HashSet<>();

    /**
     * Default constructor.
     */
    public ChangeLogRuleValidator() {

    }

    /**
     * Add rule.
     *
     * @param rule - rule.
     */
    @Override
    public void addRule(final Rule rule) {
        rules.add((ChangeLogFileRule) rule);
    }

    /**
     * Process changeLog rules.
     *
     * @param changeLogFile    - changeLog file.
     * @param validationErrors - validation errors.
     * @param changeLogFormat  - changeLog format.
     * @param exclusionParser  - exclusion parser.
     */
    @Override
    public void validate(final File changeLogFile,
                         final List<String> validationErrors,
                         final ChangeLogFormatEnum changeLogFormat,
                         final ExclusionParser exclusionParser) {
        for (ChangeLogFileRule rule : rules) {
            try {
                rule.validateChangeLogFile(changeLogFile);
            } catch (ValidationException e) {
                validationErrors.add("[" + changeLogFile.getName() + "] " + e.getMessage());
            }
        }
    }
}
