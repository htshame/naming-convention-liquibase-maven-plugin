package io.github.htshame.rule.processor.changelog;

import io.github.htshame.change.element.ChangeLogElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.parser.rule.ChangeLogRule;
import org.w3c.dom.Element;

import java.util.Objects;

import static io.github.htshame.util.ErrorMessageUtil.getChangeLogErrorMessage;
import static io.github.htshame.util.ErrorMessageUtil.validationErrorMessage;
import static io.github.htshame.util.RuleUtil.getText;

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
public class TagMustNotExistInChangeLogProcessor implements ChangeLogRule {

    private final String forbiddenTag;
    private final String targetFileName;

    /**
     * Constructor.
     *
     * @param forbiddenTag   - forbidden tag.
     * @param targetFileName - target file name.
     */
    public TagMustNotExistInChangeLogProcessor(final String forbiddenTag,
                                               final String targetFileName) {
        this.forbiddenTag = Objects.requireNonNull(
                forbiddenTag, validationErrorMessage(getName(), RuleStructureEnum.TAG));
        this.targetFileName = Objects.requireNonNull(
                targetFileName, validationErrorMessage(getName(), RuleStructureEnum.TARGET_FILE_NAME));
    }

    /**
     * Validate.
     *
     * @param changeLogElements - changeLog file.
     * @throws ValidationException - if validation fails.
     */
    @Override
    public void validateChangeLog(final ChangeLogElement changeLogElements,
                                  final ExclusionParser exclusionParser,
                                  final String fileName,
                                  final ChangeLogFormatEnum changeLogFormat) throws ValidationException {
        if (targetFileName.equals(fileName) && forbiddenTag.equals(changeLogElements.getName())) {
            Object[] messageArguments = {
                    forbiddenTag
            };
            String errorMessage = getChangeLogErrorMessage(
                    getName(),
                    changeLogFormat,
                    messageArguments);
            throw new ValidationException(errorMessage);
        }
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link TagMustNotExistInChangeLogProcessor}.
     */
    public static TagMustNotExistInChangeLogProcessor instantiate(final Element element) {
        return new TagMustNotExistInChangeLogProcessor(
                getText(element, RuleStructureEnum.TAG.getValue()),
                getText(element, RuleStructureEnum.TARGET_FILE_NAME.getValue()));
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.TAG_MUST_NOT_EXIST_IN_CHANGELOG;
    }

}
