package io.github.htshame.validator;

import io.github.htshame.change.element.ChangeLogElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.parser.rule.ChangeLogRule;
import io.github.htshame.rule.Rule;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.htshame.validator.ValidationManager.CHANGESET_PARSER_MAP;

/**
 * ChangeSet rule validator.
 */
public class ChangeLogRuleValidator implements RuleValidator {

    private final Set<ChangeLogRule> rules = new HashSet<>();

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
        rules.add((ChangeLogRule) rule);
    }

    /**
     * Process changeSet rules.
     *
     * @param changeLogFile    - changeLog file.
     * @param validationErrors - validation errors.
     * @param changeLogFormat  - changeLog format.
     * @param exclusionParser  - exclusion parser.
     * @throws ChangeLogParseException - thrown if changeLog parsing fails.
     */
    @Override
    public void validate(final File changeLogFile,
                         final List<String> validationErrors,
                         final ChangeLogFormatEnum changeLogFormat,
                         final ExclusionParser exclusionParser) throws ChangeLogParseException {
        List<ChangeLogElement> changeSets = CHANGESET_PARSER_MAP.get(changeLogFormat)
                .parseNonChangeSets(changeLogFile);

        for (ChangeLogRule rule : rules) {
            for (ChangeLogElement changeSet : changeSets) {
                try {
                    rule.validateChangeLog(changeSet, exclusionParser, changeLogFile.getName(), changeLogFormat);
                } catch (ValidationException e) {
                    validationErrors.add("[" + changeLogFile.getName() + "] " + e.getMessage());
                }
            }
        }
    }
}
