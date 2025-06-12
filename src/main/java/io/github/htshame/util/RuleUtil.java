package io.github.htshame.util;

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
     * Is tag excluded by ancestor.
     *
     * @param element - element.
     * @return <code>true</code> if excluded. <code>false</code> - if not.
     */
    public static boolean isExcludedByAncestor(final Element element) {
        Node current = element;
        while (current != null && current.getNodeType() == Node.ELEMENT_NODE) {
            if (EXCLUDED_TAG.contains(((Element) current).getTagName())) {
                return true;
            }
            current = current.getParentNode();
        }
        return false;
    }
}
