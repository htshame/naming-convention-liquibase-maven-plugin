package io.github.htshame.exception;

/**
 * This exception is thrown when rule instantiation fails.
 */
public class RuleInstantiationException extends Exception {

    /**
     * Constructor.
     *
     * @param message - message.
     * @param error   - error.
     */
    public RuleInstantiationException(final String message,
                                      final Throwable error) {
        super(message, error);
    }
}
