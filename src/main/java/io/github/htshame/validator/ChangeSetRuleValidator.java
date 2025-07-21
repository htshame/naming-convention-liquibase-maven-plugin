package io.github.htshame.validator;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.ChangeSetRule;
import io.github.htshame.rule.Rule;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.htshame.validator.ValidationManager.CHANGELOG_PARSER_MAP;

/**
 * ChangeSet rule validator.
 */
public class ChangeSetRuleValidator implements RuleValidator {

    private final Set<ChangeSetRule> rules = new HashSet<>();

    /**
     * Default constructor.
     */
    public ChangeSetRuleValidator() {

    }

    /**
     * Add rule.
     *
     * @param rule - rule.
     */
    @Override
    public void addRule(final Rule rule) {
        rules.add((ChangeSetRule) rule);
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
        List<ChangeSetElement> elements = CHANGELOG_PARSER_MAP.get(changeLogFormat).parseChangeLog(changeLogFile);

        for (ChangeSetRule rule : rules) {
            for (ChangeSetElement element : elements) {
                try {
                    rule.validateChangeSet(element, exclusionParser, changeLogFile.getName(), changeLogFormat);
                } catch (ValidationException e) {
                    validationErrors.add("[" + changeLogFile.getName() + "] " + e.getMessage());
                }
            }
        }
    }
}
