package io.github.htshame.util;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.dto.ChangeSetAttributeDto;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.util.parser.ExclusionParser;
import org.w3c.dom.Element;
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
    public static boolean isExcludedByAncestorTag(final ChangeSetElement element) {
        return EXCLUDED_TAG.contains(element.getName());
//        ChangeSetElement current = element;
//        while (current != null) {
//            if (EXCLUDED_TAG.contains(current.getName())) {
//                return true;
//            }
//            current = current.getParent();
//        }
//        return false;
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
    public static boolean shouldSkipProcessingRule(final ChangeSetElement changeSetElement,
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
    public static String composeErrorMessage(final ChangeSetElement changeSetElement,
                                             final RuleEnum ruleName,
                                             final List<String> errors) {
        ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(changeSetElement);
        return String.format("ChangeSet: id=\"%s\", author=\"%s\". Rule [%s]\n    %s",
                changeSetAttributeDto.getId(),
                changeSetAttributeDto.getAuthor(),
                ruleName.getValue(),
                String.join("\n    ", errors));
    }
}
