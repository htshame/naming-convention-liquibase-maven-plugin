package io.github.htshame.rule.processor.changelog;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ChangeLogRuleProcessingException;
import io.github.htshame.exception.RuleParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.rule.ChangeLogFileRule;
import io.github.htshame.util.RuleUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Business logic for the <code>no-trailing-spaces-in-changelog</code> rule.
 * <p>
 * Checks that the changeLog file has no trailing spaces or trailing tabs.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="no-trailing-spaces-in-changelog"&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that the changeLog file does not contain trailing spaces or trailing tabs.</p>
 */
public class NoTrailingSpacesInChangeLogProcessor implements ChangeLogFileRule {

    private static final String TRAILING_SPACES_REGEXP = ".*\\s$";

    /**
     * Constructor.
     */
    public NoTrailingSpacesInChangeLogProcessor() {

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
        List<String> linesWithTrailingSpaces = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(changeLogFile))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (line.matches(TRAILING_SPACES_REGEXP)) {
                    linesWithTrailingSpaces.add(fileName + ":" + lineNumber + " has trailing spaces or trailing tabs");
                }
                lineNumber++;
            }
        } catch (IOException e) {
            throw new ChangeLogRuleProcessingException("Failed to process changeLog file [" + fileName + "]", e);
        }

        if (!linesWithTrailingSpaces.isEmpty()) {
            throw new ValidationException(RuleUtil.composeErrorMessage(fileName, getName(), linesWithTrailingSpaces));
        }
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link NoTrailingSpacesInChangeLogProcessor}.
     */
    public static NoTrailingSpacesInChangeLogProcessor instantiate(final Element element) {
        NodeList nodeList = element.getChildNodes();
        if (nodeList.getLength() > 1) {
            throw new RuleParserException("Rule [" + RuleEnum.NO_TRAILING_SPACES_IN_CHANGELOG.name() + "]"
                    + " configuration should not contain child tags");
        }
        return new NoTrailingSpacesInChangeLogProcessor();
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.NO_TRAILING_SPACES_IN_CHANGELOG;
    }
}
