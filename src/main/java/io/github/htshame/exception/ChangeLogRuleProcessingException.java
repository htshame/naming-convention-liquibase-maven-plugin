package io.github.htshame.exception;

/**
 * ChangeLog file rule processor exception.
 */
public class ChangeLogRuleProcessingException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message - message.
     * @param e       - throwable.
     */
    public ChangeLogRuleProcessingException(final String message,
                                            final Throwable e) {
        super(message, e);
    }

}
