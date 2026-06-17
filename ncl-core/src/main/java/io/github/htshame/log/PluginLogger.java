package io.github.htshame.log;

/**
 * Logger implementation.
 */
public interface PluginLogger {

    /**
     * Info.
     *
     * @param message - info message.
     */
    void info(String message);

    /**
     * Warn.
     *
     * @param message - warn message.
     */
    void warn(String message);

    /**
     * Error.
     *
     * @param message - error message.
     */
    void error(String message);

    /**
     * Error.
     *
     * @param message - error message.
     * @param e       - exception.
     */
    void error(String message, Exception e);
}
