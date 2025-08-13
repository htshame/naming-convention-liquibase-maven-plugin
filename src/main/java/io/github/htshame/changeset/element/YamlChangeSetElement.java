package io.github.htshame.changeset.element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * YAML changeSet representation.
 */
public class YamlChangeSetElement implements ChangeSetElement {

    private final String name;
    private final Map<String, String> properties;
    private final List<ChangeSetElement> children;
    private final String value;

    /**
     * Constructor.
     *
     * @param name       - element name.
     * @param properties - element properties.
     * @param children   - children.
     * @param value      - value.
     */
    public YamlChangeSetElement(final String name,
                                final Map<String, String> properties,
                                final List<ChangeSetElement> children,
                                final String value) {
        this.name = name;
        this.properties = properties != null ? properties : new LinkedHashMap<>();
        this.children = children != null ? children : new ArrayList<>();
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasProperty(final String propertyName) {
        return properties.containsKey(propertyName);
    }

    @Override
    public String getPropertyValue(final String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public List<ChangeSetElement> getChildren() {
        return children;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public List<ChangeSetElement> findElementsByName(final ChangeSetElement root,
                                                     final String searchName) {
        List<ChangeSetElement> result = new ArrayList<>();
        if (root.getName().equals(searchName)) {
            result.add(root);
        }
        for (ChangeSetElement child : root.getChildren()) {
            result.addAll(findElementsByName(child, searchName));
        }
        return result;
    }
}
