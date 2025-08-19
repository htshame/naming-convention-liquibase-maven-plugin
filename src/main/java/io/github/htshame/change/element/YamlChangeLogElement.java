package io.github.htshame.change.element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * YAML changeLog element representation.
 */
public class YamlChangeLogElement implements ChangeLogElement {

    private final String name;
    private final Map<String, String> properties;
    private final List<ChangeLogElement> children;
    private final String value;

    /**
     * Constructor.
     *
     * @param name       - element name.
     * @param properties - element properties.
     * @param children   - children.
     * @param value      - value.
     */
    public YamlChangeLogElement(final String name,
                                final Map<String, String> properties,
                                final List<ChangeLogElement> children,
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
    public List<ChangeLogElement> getChildren() {
        return children;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public List<ChangeLogElement> findElementsByName(final ChangeLogElement root,
                                                     final String searchName) {
        List<ChangeLogElement> result = new ArrayList<>();
        if (root.getName().equals(searchName)) {
            result.add(root);
        }
        for (ChangeLogElement child : root.getChildren()) {
            result.addAll(findElementsByName(child, searchName));
        }
        return result;
    }
}
