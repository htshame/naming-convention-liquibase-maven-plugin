package io.github.htshame.parser.rule;

import io.github.htshame.change.element.ChangeLogElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.Rule;

/**
 * Interface for changeLog rule handling.
 */
public interface ChangeLogRule extends Rule {

    /**
     * Validates the changeLog against the rule which implements {@link ChangeLogRule}.
     *
     * @param changeLogElements - changeLog elements excluding changeSets.
     * @param exclusionParser   - exclusion parser.
     * @param changeLogFileName - changeLog file name.
     * @param changeLogFormat   - changeLog format.
     * @throws ValidationException - thrown if validation fails.
     */
    void validateChangeLog(ChangeLogElement changeLogElements,
                           ExclusionParser exclusionParser,
                           String changeLogFileName,
                           ChangeLogFormatEnum changeLogFormat) throws ValidationException;
}
