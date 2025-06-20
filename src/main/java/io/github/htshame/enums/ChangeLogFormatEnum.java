package io.github.htshame.enums;

/**
 * Enum that represents changeLog file extension.
 */
public enum ChangeLogFormatEnum {

    /**
     * XML format extension.
     */
    XML("xml");

    private final String format;

    /**
     * Constructor.
     *
     * @param format - format.
     */
    ChangeLogFormatEnum(final String format) {
        this.format = format;
    }

    /**
     * Get value.
     *
     * @return enum value.
     */
    public String getValue() {
        return format;
    }

    /**
     * Get enum from string.
     *
     * @param value - enum string value.
     * @return enum.
     */
    public static ChangeLogFormatEnum fromValue(final String value) {
        for (ChangeLogFormatEnum changeLogFormatEnum : ChangeLogFormatEnum.values()) {
            if (changeLogFormatEnum.getValue().equals(value)) {
                return changeLogFormatEnum;
            }
        }
        throw new IllegalArgumentException("No enum constant found for value: " + value);
    }
}
