package io.github.htshame.validator;

import io.github.htshame.change.element.ChangeLogElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.parser.rule.ChangeSetRule;
import io.github.htshame.rule.Rule;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.htshame.validator.ValidationManager.CHANGESET_PARSER_MAP;

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
        List<ChangeLogElement> changeSets = CHANGESET_PARSER_MAP.get(changeLogFormat).parseChangeSets(changeLogFile);

        for (ChangeSetRule rule : rules) {
            for (ChangeLogElement changeSet : changeSets) {
                try {
                    rule.validateChangeSet(changeSet, exclusionParser, changeLogFile.getName(), changeLogFormat);
                } catch (ValidationException e) {
                    validationErrors.add("[" + changeLogFile.getName() + "] " + e.getMessage());
                }
            }
        }
    }
}
