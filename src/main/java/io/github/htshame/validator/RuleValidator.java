package io.github.htshame.validator;

import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.Rule;

import java.io.File;
import java.util.List;

/**
 * Rule validator.
 */
public interface RuleValidator {

    /**
     * Add rule.
     *
     * @param rule - rule.
     */
    void addRule(Rule rule);

    /**
     * Process rules.
     *
     * @param changeLogFile    - changeLog file.
     * @param validationErrors - validation errors.
     * @param changeLogFormat  - changeLog format.
     * @param exclusionParser  - exclusion parser.
     * @throws ChangeLogParseException - thrown if changeLog parsing fails.
     */
    void validate(File changeLogFile,
                  List<String> validationErrors,
                  ChangeLogFormatEnum changeLogFormat,
                  ExclusionParser exclusionParser) throws ChangeLogParseException;
}
