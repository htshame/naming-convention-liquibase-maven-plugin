package io.github.htshame.util;

import io.github.htshame.dto.ChangeSetAttributeDto;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Utility class for getting changeSet's data.
 */
public final class ChangeSetUtil {

    /**
     * ChangeSet tag name.
     */
    public static final String CHANGE_SET_TAG_NAME = "changeSet";
    private static final String ID_ATTR_NAME = "id";
    private static final String AUTHOR_ATTR_NAME = "author";

    private ChangeSetUtil() {

    }

    /**
     * Get values of <code>id</code> and <code>author</code> attributes.
     *
     * @param element - element of the changeSet.
     * @return object with changeSet attributes.
     */
    public static ChangeSetAttributeDto getAttributesFromAncestor(final Element element) {
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

    /**
     * Retrieve the value of the specified attribute from the given element.
     *
     * @param element - element.
     * @param attributeName - attribute name.
     * @return attribute value.
     */
    private static String getAttributeValue(final Element element,
                                            final String attributeName) {
        return element.getAttributes().getNamedItem(attributeName).getNodeValue();
    }
}
