package com.github.htshame.exception;

/**
 * ChangeLog collection exception.
 */
public class ChangeLogCollectorException extends Exception {

    /**
     * Constructor.
     *
     * @param message - message.
     * @param e       - throwable.
     */
    public ChangeLogCollectorException(final String message,
                                       final Throwable e) {
        super(message, e);
    }
}
