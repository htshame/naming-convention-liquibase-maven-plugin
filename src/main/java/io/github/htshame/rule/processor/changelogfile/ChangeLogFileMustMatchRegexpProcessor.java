package io.github.htshame.rule.processor.changelogfile;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.rule.ChangeLogFileRule;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.github.htshame.util.ErrorMessageUtil.getChangeLogFileErrorMessage;
import static io.github.htshame.util.ErrorMessageUtil.validationErrorMessage;
import static io.github.htshame.util.RuleUtil.getText;
import static io.github.htshame.util.RuleUtil.shouldCollectValuesRuleListFormat;

/**
 * Business logic for the <code>changelog-file-name-must-match-regexp</code> rule.
 * <p>
 * Checks that the changeLog file matches the provided regexp.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="changelog-file-name-must-match-regexp"&gt;
 *     &lt;fileNameRegexp&gt;^changeLog_\d+\.(xml|json|ya?ml)$&lt;/fileNameRegexp&gt;
 *     &lt;excludedFileNames&gt;
 *         &lt;fileName&gt;changelog-master.xml&lt;/fileName&gt;
 *         &lt;fileName&gt;changeLog22.xml&lt;/fileName&gt;
 *     &lt;/excludedFileNames&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that the changeLog file matches the provided regexp, excluding file names provided in
 * <code>excludedFileNames</code>.</p>
 */
public class ChangeLogFileMustMatchRegexpProcessor implements ChangeLogFileRule {

    private final String fileNameRegexp;
    private final Set<String> excludedFileNames;

    /**
     * Constructor.
     *
     * @param fileNameRegexp    - file name regexp.
     * @param excludedFileNames - excluded file names.
     */
    public ChangeLogFileMustMatchRegexpProcessor(final String fileNameRegexp,
                                                 final Set<String> excludedFileNames) {
        this.fileNameRegexp = Objects.requireNonNull(
                fileNameRegexp, validationErrorMessage(getName(), RuleStructureEnum.FILE_NAME_REGEXP));
        this.excludedFileNames = excludedFileNames;
    }

    /**
     * Validate.
     *
     * @param changeLogFile - changeLog file.
     * @throws ValidationException - if validation fails.
     */
    @Override
    public void validateChangeLogFile(final File changeLogFile) throws ValidationException {
        String fileName = changeLogFile.getName();
        if (!excludedFileNames.contains(fileName) && !fileName.matches(fileNameRegexp)) {
            Object[] messageArguments = {
                    fileName,
                    fileNameRegexp,
                    getName().getValue()
            };
            String errorMessage = getChangeLogFileErrorMessage(
                    getName(),
                    messageArguments);
            throw new ValidationException(errorMessage);
        }
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link ChangeLogFileMustMatchRegexpProcessor}.
     */
    public static ChangeLogFileMustMatchRegexpProcessor instantiate(final Element element) {
        NodeList excludedFiles = element
                .getElementsByTagName(RuleStructureEnum.EXCLUDED_FILE_NAMES.getValue());
        Set<String> excludedFileNames = new HashSet<>();
        if (shouldCollectValuesRuleListFormat(excludedFiles, RuleStructureEnum.EXCLUDED_FILE_NAMES)) {
            NodeList excludedFileElement = ((Element) excludedFiles.item(0))
                    .getElementsByTagName(RuleStructureEnum.FILE_NAME.getValue());
            for (int i = 0; i < excludedFileElement.getLength(); i++) {
                excludedFileNames.add(excludedFileElement.item(i).getTextContent());
            }
        }
        return new ChangeLogFileMustMatchRegexpProcessor(
                getText(element, RuleStructureEnum.FILE_NAME_REGEXP.getValue()),
                excludedFileNames);
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.CHANGELOG_FILE_NAME_MUST_MATCH_REGEXP;
    }

}
