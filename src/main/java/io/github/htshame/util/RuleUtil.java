package io.github.htshame.util;

import io.github.htshame.dto.ChangeSetAttributeDto;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.util.parser.ExclusionParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for parsing the rule XML file.
 */
public final class RuleUtil {

    private static final List<String> EXCLUDED_TAG = Arrays.asList("preConditions", "loadData");

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
        return nodes.getLength() > 0 ? nodes.item(0).getTextContent().trim() : null;
    }

    /**
     * Is tag excluded by ancestor tag.
     *
     * @param element - element.
     * @return <code>true</code> if excluded. <code>false</code> - if not.
     */
    public static boolean isExcludedByAncestorTag(final Element element) {
        Node current = element;
        while (current != null && current.getNodeType() == Node.ELEMENT_NODE) {
            if (EXCLUDED_TAG.contains(((Element) current).getTagName())) {
                return true;
            }
            current = current.getParentNode();
        }
        return false;
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
    public static boolean shouldSkipProcessingRule(final Element changeSetElement,
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
    public static String composeErrorMessage(final Element changeSetElement,
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
