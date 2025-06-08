package com.github.htshame.exception;

/**
 * Validation exception. Thrown in case changeLog validation fails.
 */
public class ValidationException extends Exception {

    /**
     * Constructor.
     *
     * @param message - message.
     */
    public ValidationException(final String message) {
        super(message);
    }
}
