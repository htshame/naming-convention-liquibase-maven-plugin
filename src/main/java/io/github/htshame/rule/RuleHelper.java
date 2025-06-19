package io.github.htshame.rule;

import io.github.htshame.dto.ChangeSetAttributeDto;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.util.ChangeSetUtil;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Rule helper.
 */
public class RuleHelper {

    /**
     * Default constructor.
     */
    public RuleHelper() {

    }

    /**
     * Check whether the changeSet should be processed.
     *
     * @param changeSetElement  - changeSet element.
     * @param exclusionParser   - exclusion parser.
     * @param changeLogFileName - changeLog file name.
     * @param ruleName          - rule name.
     * @return <code>true</code> if yes. <code>false</code> if not.
     */
    public boolean shouldSkipProcessingRule(final Element changeSetElement,
                                            final ExclusionParser exclusionParser,
                                            final String changeLogFileName,
                                            final RuleEnum ruleName) {
        ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(changeSetElement);
        return exclusionParser.isChangeSetExcluded(
                changeLogFileName,
                changeSetAttributeDto.getId(),
                changeSetAttributeDto.getAuthor(),
                ruleName);
    }

    /**
     * Compose error message.
     *
     * @param changeSetElement - changeSet element.
     * @param ruleName         - rule name.
     * @param errors           - list of errors.
     * @return error message.
     */
    public String composeErrorMessage(final Element changeSetElement,
                                      final RuleEnum ruleName,
                                      final List<String> errors) {
        ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(changeSetElement);
        return String.format("ChangeSet: id=\"%s\", author=\"%s\". Rule [%s]\n%s",
                changeSetAttributeDto.getId(),
                changeSetAttributeDto.getAuthor(),
                ruleName.getValue(),
                String.join("\n", errors));
    }
}
