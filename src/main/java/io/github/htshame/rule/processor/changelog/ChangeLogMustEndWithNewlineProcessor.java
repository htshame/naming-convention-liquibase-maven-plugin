package io.github.htshame.rule.processor.changelog;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ChangeLogRuleProcessingException;
import io.github.htshame.exception.RuleParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.rule.ChangeLogRule;
import io.github.htshame.util.RuleUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Business logic for the <code>changelog-must-end-with-newline</code> rule.
 * <p>
 * Checks that the changeLog file ends with a newline.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="changelog-must-end-with-newline"&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that the changeLog file ends with a newline.</p>
 */
public class ChangeLogMustEndWithNewlineProcessor implements ChangeLogRule {

    /**
     * Constructor.
     */
    public ChangeLogMustEndWithNewlineProcessor() {

    }

    /**
     * Validate.
     *
     * @param changeLogFile - changeLog file.
     * @throws ValidationException - if validation fails.
     */
    @Override
    public void validateChangeLog(final File changeLogFile) throws ValidationException {
        String fileName = changeLogFile.getName();
        boolean doesFileEndWithNewLine;
        try {
            doesFileEndWithNewLine = endsWithNewline(changeLogFile);
        } catch (IOException e) {
            throw new ChangeLogRuleProcessingException("Failed to process changeLog file [" + fileName + "]", e);
        }

        if (!doesFileEndWithNewLine) {
            throw new ValidationException(
                    RuleUtil.composeErrorMessage(
                            fileName,
                            getName(),
                            "File " + fileName + " does not end with a newline")
            );
        }
    }

    /**
     * Check that file ends with a new line.
     *
     * @param file - file.
     * @return <code>true</code> if ends, <code>false</code> - if not.
     * @throws IOException - in case something's wrong with the file.
     */
    private static boolean endsWithNewline(final File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("File: " + file.getName() + " not found");
        }
        if (file.length() == 0) {
            return false;
        }

        boolean endsWithNewline = false;

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            long length = randomAccessFile.length();
            randomAccessFile.seek(length - 1);
            int lastByte = randomAccessFile.read();

            if (lastByte == '\n') {
                endsWithNewline = true;
            } else if (lastByte == '\r' && length > 1) {
                randomAccessFile.seek(length - 2);
                int secondLastByte = randomAccessFile.read();
                endsWithNewline = (secondLastByte == '\n');
            }
        }

        return endsWithNewline;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link ChangeLogMustEndWithNewlineProcessor}.
     */
    public static ChangeLogMustEndWithNewlineProcessor instantiate(final Element element) {
        NodeList nodeList = element.getChildNodes();
        if (nodeList.getLength() > 1) {
            throw new RuleParserException("Rule [" + RuleEnum.CHANGELOG_MUST_END_WITH_NEWLINE.name() + "]"
                    + " configuration should not contain child tags");
        }
        return new ChangeLogMustEndWithNewlineProcessor();
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.CHANGELOG_MUST_END_WITH_NEWLINE;
    }
}
