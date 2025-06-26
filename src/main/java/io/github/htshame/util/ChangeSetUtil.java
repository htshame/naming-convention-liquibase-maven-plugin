package io.github.htshame.util;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.dto.ChangeSetAttributeDto;

/**
 * Utility class for getting changeSet's data.
 */
public final class ChangeSetUtil {

    /**
     * ChangeSet name.
     */
    public static final String CHANGE_SET_TAG_NAME = "changeSet";
    /**
     * Database changeLog name.
     */
    public static final String DATABASE_CHANGELOG_NAME = "databaseChangeLog";
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
    public static ChangeSetAttributeDto getAttributesFromAncestor(final ChangeSetElement element) {
        if (CHANGE_SET_TAG_NAME.equals(element.getName())) {
            return new ChangeSetAttributeDto(
                    getAttributeValue(element, ID_ATTR_NAME),
                    getAttributeValue(element, AUTHOR_ATTR_NAME));
        }
//        ChangeSetElement current = element.getParent();
//        while (current != null && current.getNodeType() == Node.ELEMENT_NODE) {
//            Element parentElement = (Element) current;
//            if (CHANGE_SET_TAG_NAME.equals(parentElement.getTagName())) {
//                return new ChangeSetAttributeDto(
//                        getAttributeValue(parentElement, ID_ATTR_NAME),
//                        getAttributeValue(parentElement, AUTHOR_ATTR_NAME));
//            }
//            current = current.getParent();
//        }
        return new ChangeSetAttributeDto("", "");
    }

    /**
     * Retrieve the value of the specified attribute from the given element.
     *
     * @param element - element.
     * @param attributeName - attribute name.
     * @return attribute value.
     */
    private static String getAttributeValue(final ChangeSetElement element,
                                            final String attributeName) {
        return element.getProperties().get(attributeName);
    }
}
