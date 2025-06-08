package com.github.htshame.util;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class RuleUtil {

    private RuleUtil() {

    }

    public static String getText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        return nodes.getLength() > 0 ? nodes.item(0).getTextContent().trim() : null;
    }


}
