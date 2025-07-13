package io.github.htshame.rule.processor.changelog;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.rule.ChangeLogRule;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.github.htshame.util.ErrorMessageUtil.validationErrorMessage;
import static io.github.htshame.util.RuleUtil.getText;

/**
 * Business logic for the <code>changelog-file-lines-limit</code> rule.
 * <p>
 * Checks that changeLog file length is less or equals than value provided in <code>linesLimit</code>.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="changelog-file-lines-limit"&gt;
 *     &lt;linesLimit&gt;1000&lt;/linesLimit&gt;
 *     &lt;excludedFileNames&gt;
 *         &lt;fileName&gt;changelog_04.xml&lt;/fileName&gt;
 *         &lt;fileName&gt;changelog_06.xml&lt;/fileName&gt;
 *     &lt;/excludedFileNames&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that the changeLog file length is not longer than specified in <code>linesLimit</code>,
 * excluding changeLog files provided in <code>excludedFileNames</code>.
 */
public class ChangeLogFileLinesLimitProcessor implements ChangeLogRule {

    private final Integer linesLimit;
    private final Set<String> excludedFileNames;

    /**
     * Constructor.
     *
     * @param linesLimit        - file name regexp.
     * @param excludedFileNames - excluded file names.
     */
    public ChangeLogFileLinesLimitProcessor(final String linesLimit,
                                            final Set<String> excludedFileNames) {
        Objects.requireNonNull(linesLimit, validationErrorMessage(getName(), RuleStructureEnum.LINES_LIMIT));
        this.linesLimit = Integer.valueOf(linesLimit);
        this.excludedFileNames = excludedFileNames;
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
        try {
            long lines = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(changeLogFile))) {
                while (reader.readLine() != null) {
                    lines++;
                }
            }
            if (!excludedFileNames.contains(fileName) && lines > linesLimit) {
                String errorMessage = String.format("File [%s] has [%s] lines, longer than [%s] lines max. Rule [%s]",
                        fileName,
                        lines,
                        linesLimit,
                        getName().getValue());
                throw new ValidationException(errorMessage);
            }
        } catch (IOException e) {
            String errorMessage = String.format("Unable to read file [%s]. Rule [%s]",
                    fileName,
                    getName().getValue());
            throw new ValidationException(errorMessage);
        }
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link ChangeLogFileLinesLimitProcessor}.
     */
    public static ChangeLogFileLinesLimitProcessor instantiate(final Element element) {
        Set<String> excludedFileNames = new HashSet<>();
        NodeList excludedAttrs = element
                .getElementsByTagName(RuleStructureEnum.EXCLUDED_FILE_NAMES.getValue());
        if (excludedAttrs.getLength() != 0) {
            NodeList excludedAttrElements = ((Element) excludedAttrs.item(0))
                    .getElementsByTagName(RuleStructureEnum.FILE_NAME.getValue());
            for (int i = 0; i < excludedAttrElements.getLength(); i++) {
                excludedFileNames.add(excludedAttrElements.item(i).getTextContent());
            }
        }
        return new ChangeLogFileLinesLimitProcessor(
                getText(element, RuleStructureEnum.LINES_LIMIT.getValue()),
                excludedFileNames);
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.CHANGELOG_FILE_LINES_LIMIT;
    }

}
