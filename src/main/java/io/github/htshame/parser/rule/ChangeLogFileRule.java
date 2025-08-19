package io.github.htshame.parser.rule;

import io.github.htshame.exception.ValidationException;
import io.github.htshame.rule.Rule;

import java.io.File;

/**
 * Interface for changeLog file rule handling.
 */
public interface ChangeLogFileRule extends Rule {

    /**
     * Validates the changeLog file against the rule which implements {@link ChangeLogFileRule}.
     *
     * @param changeLogFile - changeLog file.
     * @throws ValidationException - thrown if validation fails.
     */
    void validateChangeLogFile(File changeLogFile) throws ValidationException;
}
