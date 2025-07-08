package io.github.htshame.rule;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;

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
    void validateChangeSet(ChangeSetElement changeSetElement,
                           ExclusionParser exclusionParser,
                           String changeLogFileName,
                           ChangeLogFormatEnum changeLogFormat) throws ValidationException;

}
