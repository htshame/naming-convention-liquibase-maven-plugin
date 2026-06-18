package io.github.htshame.exception;

/**
 * Rules file parser exception.
 */
public class RuleParserException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message - message.
     * @param e       - throwable.
     */
    public RuleParserException(final String message,
                               final Throwable e) {
        super(message, e);
    }

    /**
     * Constructor.
     *
     * @param message - message.
     */
    public RuleParserException(final String message) {
        super(message);
    }
}
