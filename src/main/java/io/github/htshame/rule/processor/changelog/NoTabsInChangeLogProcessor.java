package io.github.htshame.rule.processor.changelog;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.RuleParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.rule.ChangeLogRule;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import static io.github.htshame.util.ErrorMessageUtil.getErrorMessage;

/**
 * Business logic for the <code>no-tabs-in-changelog</code> rule.
 * <p>
 * Checks that the changeLog file matches the provided regexp.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="no-tabs-in-changelog"&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that the changeLog file does not contain tabs.</p>
 */
public class NoTabsInChangeLogProcessor implements ChangeLogRule {

    private static final String TAB_CHARACTER = "\t";

    /**
     * Constructor.
     */
    public NoTabsInChangeLogProcessor() {

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
            try (Stream<String> linesStream = Files.lines(changeLogFile.toPath())) {
                if (linesStream.anyMatch(line -> line.contains(TAB_CHARACTER))) {
                    Object[] messageArguments = {fileName, getName().getValue()};
                    String errorMessage = getErrorMessage(
                            getName(),
                            messageArguments);
                    throw new ValidationException(errorMessage);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link NoTabsInChangeLogProcessor}.
     */
    public static NoTabsInChangeLogProcessor instantiate(final Element element) {
        NodeList nodeList = element.getChildNodes();
        if (nodeList.getLength() > 1) {
            throw new RuleParserException("Rule [" + RuleEnum.NO_TABS_IN_CHANGELOG.name() + "]"
                    + " configuration should not contain child tags");
        }
        return new NoTabsInChangeLogProcessor();
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.NO_TABS_IN_CHANGELOG;
    }
}
