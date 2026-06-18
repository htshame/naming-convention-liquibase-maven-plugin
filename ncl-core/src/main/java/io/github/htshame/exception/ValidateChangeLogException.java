package io.github.htshame.exception;

/**
 * Exception to indicate global validation failure.
 */
public class ValidateChangeLogException extends Exception {

    /**
     * Constructor.
     *
     * @param message - error message.
     */
    public ValidateChangeLogException(final String message) {
        super(message);
    }
}
