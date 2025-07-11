package io.github.htshame.exception;

/**
 * This exception is thrown when rule instantiation fails.
 */
public class RuleInstantiationException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param error - error.
     */
    public RuleInstantiationException(final Throwable error) {
        super(error);
    }
}
