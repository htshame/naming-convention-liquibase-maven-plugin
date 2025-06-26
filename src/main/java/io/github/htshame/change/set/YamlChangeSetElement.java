package io.github.htshame.change.set;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * YAML changeSet representation.
 */
public class YamlChangeSetElement implements ChangeSetElement {

    private final JsonNode yamlJsonNode;

    /**
     * Constructor.
     *
     * @param yamlNode - yaml node.
     */
    public YamlChangeSetElement(final JsonNode yamlNode) {
        this.yamlJsonNode = yamlNode;
    }

    /**
     * Get element name.
     *
     * @return element name.
     */
    @Override
    public String getName() {
        return yamlJsonNode.textValue();
    }

    /**
     * Whether element has a provided property.
     *
     * @param name - property name.
     * @return <code>true</code> if contains, <code>false</code> - if not.
     */
    @Override
    public boolean hasProperty(final String name) {
        return yamlJsonNode.has(name);
    }

    /**
     * Whether element has a provided property.
     *
     * @param name - property name.
     * @return <code>true</code> if contains, <code>false</code> - if not.
     */
    @Override
    public String getPropertyValue(final String name) {
        JsonNode prop = yamlJsonNode.get(name);
        return (prop != null && prop.isValueNode()) ? prop.asText() : null;
    }

    /**
     * Get child elements.
     *
     * @return list of child elements.
     */
    @Override
    public List<ChangeSetElement> getChildren() {
        List<ChangeSetElement> children = new ArrayList<>();

        if (yamlJsonNode.isObject()) {
            Set<Map.Entry<String, JsonNode>> fields = yamlJsonNode.properties();
            for (Map.Entry<String, JsonNode> entry : fields) {
                JsonNode value = entry.getValue();

                if (value.isObject() || value.isArray()) {
                    children.add(new YamlChangeSetElement(value));
                }
            }
        } else if (yamlJsonNode.isArray()) {
            for (JsonNode item : yamlJsonNode) {
                if (item.isObject()) {
                    Iterator<String> fieldNames = item.fieldNames();
                    if (fieldNames.hasNext()) {
                        String childName = fieldNames.next();
                        JsonNode childValue = item.get(childName);
                        children.add(new YamlChangeSetElement(childValue));
                    }
                }
            }
        }

        return children;
    }

    /**
     * Get properties.
     *
     * @return property-value map.
     */
    @Override
    public Map<String, String> getProperties() {
        Map<String, String> result = new LinkedHashMap<>();
        if (!yamlJsonNode.isObject()) {
            return result;
        }
        for (Map.Entry<String, JsonNode> entry : yamlJsonNode.properties()) {
            if (entry.getValue().isValueNode()) {
                result.put(entry.getKey(), entry.getValue().asText());
            }
        }
        return result;
    }

    /**
     * Get value.
     *
     * @return value.
     */
    @Override
    public String getValue() {
        if (yamlJsonNode.isValueNode()) {
            return yamlJsonNode.asText();
        }
        return "";
    }

    /**
     * Find elements by name.
     *
     * @param changeSetElement - root object.
     * @param name             - element name.
     * @return list of elements with the provided name.
     */
    @Override
    public List<ChangeSetElement> findElementsByName(final ChangeSetElement changeSetElement,
                                                     final String name) {
        List<ChangeSetElement> result = new ArrayList<>();
        traverse(changeSetElement, name, result);
        return result;
    }

    private void traverse(final ChangeSetElement element,
                          final String name,
                          final List<ChangeSetElement> result) {
        if (name.equals(element.getName())) {
            result.add(element);
        }
        for (ChangeSetElement child : element.getChildren()) {
            traverse(child, name, result);
        }
    }
}
