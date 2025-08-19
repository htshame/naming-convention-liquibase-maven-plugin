package io.github.htshame.parser.rule;

import io.github.htshame.change.element.ChangeLogElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.Rule;

/**
 * Interface for changeSet rule handling.
 */
public interface ChangeSetRule extends Rule {

    /**
     * Validates the changeSet against the rule which implements {@link ChangeSetRule}.
     *
     * @param changeSetElement  - changeSet.
     * @param exclusionParser   - exclusion parser.
     * @param changeLogFileName - changeLog file name.
     * @param changeLogFormat   - changeLog format.
     * @throws ValidationException - thrown if validation fails.
     */
    void validateChangeSet(ChangeLogElement changeSetElement,
                           ExclusionParser exclusionParser,
                           String changeLogFileName,
                           ChangeLogFormatEnum changeLogFormat) throws ValidationException;

}
