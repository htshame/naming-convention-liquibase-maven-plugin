package io.github.htshame.change.set;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON changeSet representation.
 */
public class JsonChangeSetElement implements ChangeSetElement {

    private final String name;
    private final JsonNode node;

    /**
     * Constructor.
     *
     * @param name - node name.
     * @param node - json node.
     */
    public JsonChangeSetElement(final String name,
                                final JsonNode node) {
        this.name = name;
        this.node = node;
    }

    /**
     * Get property name.
     *
     * @return property name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Whether element has a provided property.
     *
     * @param propertyName - property name.
     * @return <code>true</code> if contains, <code>false</code> - if not.
     */
    @Override
    public boolean hasProperty(final String propertyName) {
        return node.has(propertyName) && node.get(propertyName).isValueNode();
    }

    /**
     * Get property value by name.
     *
     * @param propertyName - property name.
     * @return property value.
     */
    @Override
    public String getPropertyValue(final String propertyName) {
        JsonNode propNode = node.get(propertyName);
        if (propNode != null && propNode.isValueNode()) {
            return propNode.asText();
        }
        return null;
    }

    /**
     * Get node value.
     *
     * @return node value.
     */
    @Override
    public String getValue() {
        if (node.isValueNode()) {
            return node.asText();
        }
        return null;
    }

    /**
     * Get child elements.
     *
     * @return list of child elements.
     */
    @Override
    public List<ChangeSetElement> getChildren() {
        List<ChangeSetElement> children = new ArrayList<>();

        if (node.isObject()) {
            for (Map.Entry<String, JsonNode> entry : node.properties()) {
                children.add(new JsonChangeSetElement(entry.getKey(), entry.getValue()));
            }
        } else if (node.isArray()) {
            for (JsonNode arrayElement : node) {
                if (arrayElement.isObject()) {
                    for (Map.Entry<String, JsonNode> entry : arrayElement.properties()) {
                        children.add(new JsonChangeSetElement(entry.getKey(), entry.getValue()));
                    }
                } else {
                    // fallback: preserve current behavior for non-object array elements
                    children.add(new JsonChangeSetElement(name, arrayElement));
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
        Map<String, String> props = new LinkedHashMap<>();
        if (node.isObject()) {
            for (Map.Entry<String, JsonNode> entry : node.properties()) {
                JsonNode val = entry.getValue();
                if (val.isValueNode()) {
                    props.put(entry.getKey(), val.asText());
                }
            }
        }
        return props;
    }

    /**
     * Find property by name.
     *
     * @param root - root object.
     * @param propertyName - property name.
     * @return list of properties with the provided name.
     */
    @Override
    public List<ChangeSetElement> findElementsByName(final ChangeSetElement root,
                                                     final String propertyName) {
        List<ChangeSetElement> result = new ArrayList<>();
        if (root.getName().equals(propertyName)) {
            result.add(root);
        }
        for (ChangeSetElement child : root.getChildren()) {
            result.addAll(findElementsByName(child, propertyName));
        }
        return result;
    }
}
