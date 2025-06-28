package io.github.htshame.rule;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.util.parser.ExclusionParser;

/**
 * Interface for rule handling.
 */
public interface Rule {

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    RuleEnum getName();

    /**
     * Validates the changeSet against the rule which implements {@link Rule}.
     *
     * @param changeSetElement  - changeSet.
     * @param exclusionParser   - exclusion parser.
     * @param changeLogFileName - changeLog file name.
     * @param changeLogFormat   - changeLog format.
     * @throws ValidationException - thrown if validation fails.
     */
    void validate(ChangeSetElement changeSetElement,
                  ExclusionParser exclusionParser,
                  String changeLogFileName,
                  ChangeLogFormatEnum changeLogFormat) throws ValidationException;

}
