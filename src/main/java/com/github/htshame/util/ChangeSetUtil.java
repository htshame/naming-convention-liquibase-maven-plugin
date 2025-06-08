package com.github.htshame.util;

import com.github.htshame.dto.ChangeSetAttributeDto;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ChangeSetUtil {

    private static final String CHANGE_SET_TAG_NAME = "changeSet";
    private static final String ID_ATTR_NAME = "id";
    private static final String AUTHOR_ATTR_NAME = "author";


    public static ChangeSetAttributeDto getAttributesFromAncestor(Element element) {
        if (CHANGE_SET_TAG_NAME.equals(element.getTagName())) {
            return new ChangeSetAttributeDto(
                    getAttributeValue(element, ID_ATTR_NAME),
                    getAttributeValue(element, AUTHOR_ATTR_NAME));
        }
        Node current = element.getParentNode();
        while (current != null && current.getNodeType() == Node.ELEMENT_NODE) {
            Element parentElement = (Element) current;
            if (CHANGE_SET_TAG_NAME.equals(parentElement.getTagName())) {
                return new ChangeSetAttributeDto(
                        getAttributeValue(parentElement, ID_ATTR_NAME),
                        getAttributeValue(parentElement, AUTHOR_ATTR_NAME));
            }
            current = current.getParentNode();
        }
        return new ChangeSetAttributeDto("", "");
    }

    private static String getAttributeValue(Element element, String attributeName) {
        return element.getAttributes().getNamedItem(attributeName).getNodeValue();
    }
}
