package io.github.htshame.rule;

import io.github.htshame.exception.ValidationException;

import java.io.File;

/**
 * Interface for changeLog rule handling.
 */
public interface ChangeLogRule extends Rule {

    /**
     * Validates the changeLog file against the rule which implements {@link ChangeLogRule}.
     *
     * @param changeLogFile - changeLog file.
     * @throws ValidationException - thrown if validation fails.
     */
    void validateChangeLog(File changeLogFile) throws ValidationException;
}
