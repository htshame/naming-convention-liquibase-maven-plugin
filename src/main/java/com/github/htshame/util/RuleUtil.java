package com.github.htshame.util;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Utility class for parsing the rule XML file.
 */
public final class RuleUtil {

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
}
