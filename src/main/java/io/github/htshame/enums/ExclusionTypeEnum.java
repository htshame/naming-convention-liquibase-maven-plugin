package io.github.htshame.enums;

/**
 * Exclusion types.
 */
public enum ExclusionTypeEnum {
    /**
     * Exclusion applied to file.
     */
    FILE_EXCLUSION("fileExclusion"),
    /**
     * Exclusion applied to changeSet.
     */
    CHANGESET_EXCLUSION("changeSetExclusion");

    private final String type;

    /**
     * Constructor.
     *
     * @param type - type.
     */
    ExclusionTypeEnum(final String type) {
        this.type = type;
    }

    /**
     * Get enum value from type string.
     *
     * @param type - type.
     * @return enum value.
     * @throws IllegalArgumentException - if type not found.
     */
    public static ExclusionTypeEnum fromTypeName(final String type) {
        for (ExclusionTypeEnum exclusionType : values()) {
            if (exclusionType.type.equals(type)) {
                return exclusionType;
            }
        }
        throw new IllegalArgumentException("Exclusion type [" + type + "] does not exist");
    }
}
