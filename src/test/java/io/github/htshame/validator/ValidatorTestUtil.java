package io.github.htshame.validator;

import io.github.htshame.changeset.parser.ChangeSetParser;
import io.github.htshame.enums.ChangeLogFormatEnum;

import static io.github.htshame.validator.ValidationManager.CHANGELOG_PARSER_MAP;

/**
 * Util class for {@link ValidationManager} testing.
 */
public final class ValidatorTestUtil {

    /**
     * Private constructor.
     */
    private ValidatorTestUtil() {

    }

    /**
     * Get parser by the changeLog file format.
     *
     * @param changeLogFormat - changeLog file format.
     * @return changeLog parser.
     */
    public static ChangeSetParser getParser(final ChangeLogFormatEnum changeLogFormat) {
        return CHANGELOG_PARSER_MAP.get(changeLogFormat);
    }
}
