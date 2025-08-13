package io.github.htshame.changeset.element;

import java.util.List;
import java.util.Map;

/**
 * Abstract changeSet representation.
 */
public interface ChangeSetElement {

    /**
     * Get element name.
     *
     * @return element name.
     */
    String getName();

    /**
     * Whether element has a provided property.
     *
     * @param name - property name.
     * @return <code>true</code> if contains, <code>false</code> - if not.
     */
    boolean hasProperty(String name);

    /**
     * Get element value by name.
     *
     * @param name - element name.
     * @return element value.
     */
    String getPropertyValue(String name);

    /**
     * Get value.
     *
     * @return value.
     */
    String getValue();

    /**
     * Get child elements.
     *
     * @return list of child elements.
     */
    List<ChangeSetElement> getChildren();

    /**
     * Get properties.
     *
     * @return property-value map.
     */
    Map<String, String> getProperties();

    /**
     * Find elements by name.
     *
     * @param changeSetElement - root object.
     * @param name             - element name.
     * @return list of elements with the provided name.
     */
    List<ChangeSetElement> findElementsByName(ChangeSetElement changeSetElement, String name);
}
