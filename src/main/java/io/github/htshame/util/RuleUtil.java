package io.github.htshame.util;

import io.github.htshame.change.element.ChangeLogElement;
import io.github.htshame.dto.ChangeSetAttributeDto;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.RuleParserException;
import io.github.htshame.parser.ExclusionParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for parsing the rule XML file.
 */
public final class RuleUtil {

    /**
     * Globally excluded tags.
     */
    private static final List<String> EXCLUDED_TAGS = Arrays.asList("preConditions", "loadData");

    /**
     * Globally excluded attributes.
     */
    public static final List<String> EXCLUDED_ATTRIBUTES = Arrays.asList(
            "id", "author", "comment", "remarks");

    /**
     * Default constructor.
     */
    private RuleUtil() {

    }

    /**
     * Get the content of the given tag.
     *
     * @param element - element.
     * @param tagName - tag name.
     * @return tag value.
     */
    public static String getText(final Element element,
                                 final String tagName) {
        NodeList nodes = element.getElementsByTagName(tagName);
        return nodes.getLength() > 0
                ? nodes.item(0).getTextContent().trim()
                : null;
    }

    /**
     * Is tag excluded by ancestor tag.
     *
     * @param element - element.
     * @return <code>true</code> if excluded. <code>false</code> - if not.
     */
    public static boolean isExcludedByAncestorTag(final ChangeLogElement element) {
        return EXCLUDED_TAGS.contains(element.getName());
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
    public static boolean shouldSkipProcessingRule(final ChangeLogElement changeSetElement,
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
     * Check whether the changeLog should be processed.
     *
     * @param exclusionParser   - exclusion parser.
     * @param changeLogFileName - changeLog file name.
     * @param ruleName          - rule name.
     * @return <code>true</code> if yes. <code>false</code> if not.
     */
    public static boolean shouldSkipProcessingRule(final ExclusionParser exclusionParser,
                                                   final String changeLogFileName,
                                                   final RuleEnum ruleName) {
        return exclusionParser.isChangeLogExcluded(
                changeLogFileName,
                ruleName);
    }

    /**
     * Compose error message for changeSet.
     *
     * @param changeSetElement - changeSet element.
     * @param ruleName         - rule name.
     * @param errors           - list of errors.
     * @return error message.
     */
    public static String composeErrorMessage(final ChangeLogElement changeSetElement,
                                             final RuleEnum ruleName,
                                             final List<String> errors) {
        ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(changeSetElement);
        return String.format("ChangeSet: id=\"%s\", author=\"%s\". Rule [%s]\n    %s",
                changeSetAttributeDto.getId(),
                changeSetAttributeDto.getAuthor(),
                ruleName.getValue(),
                String.join("\n    ", errors));
    }

    /**
     * Compose error message for changeLog file.
     *
     * @param fileName - changeLog file name.
     * @param ruleName - rule name.
     * @param errors   - list of errors.
     * @return error message.
     */
    public static String composeErrorMessage(final String fileName,
                                             final RuleEnum ruleName,
                                             final List<String> errors) {
        return String.format("File: [%s]. Rule [%s]\n    %s",
                fileName,
                ruleName.getValue(),
                String.join("\n    ", errors));
    }

    /**
     * Compose error message for changeLog file.
     *
     * @param fileName - changeLog file name.
     * @param ruleName - rule name.
     * @param error    - error message.
     * @return error message.
     */
    public static String composeErrorMessage(final String fileName,
                                             final RuleEnum ruleName,
                                             final String error) {
        return String.format("File: [%s]. Rule [%s]. %s",
                fileName,
                ruleName.getValue(),
                error);
    }

    /**
     * Check whether child values should be collected or not.
     *
     * @param nodeList             - node list to check.
     * @param ruleStructureElement - rule structure element.
     * @return <code>true</code> if should. <code>false</code> - if not.
     * @throws RuleParserException - if number of parent node tags is more than 1.
     */
    public static boolean shouldCollectValuesRuleListFormat(final NodeList nodeList,
                                                            final RuleStructureEnum ruleStructureElement) {
        if (nodeList.getLength() > 1) {
            throw new RuleParserException("Too many [" + ruleStructureElement.getValue() + "] tags");
        }
        return nodeList.getLength() != 0;
    }
}
