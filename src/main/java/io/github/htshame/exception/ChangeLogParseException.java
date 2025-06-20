package io.github.htshame.exception;

/**
 * Thrown when changeLog parsing fails.
 */
public class ChangeLogParseException extends Exception {

    /**
     * Constructor.
     *
     * @param fileName - file name.
     * @param e        - exception.
     */
    public ChangeLogParseException(final String fileName,
                                   final Exception e) {
        super("Error parsing changeLog file: [" + fileName + "]", e);
    }
}
