package io.github.htshame.exception;

/**
 * Exception thrown when plugin fails to fetch config data from provided URLs.
 */
public class ConfigApiGatewayException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message - error message.
     */
    public ConfigApiGatewayException(final String message) {
        super(message);
    }
}
