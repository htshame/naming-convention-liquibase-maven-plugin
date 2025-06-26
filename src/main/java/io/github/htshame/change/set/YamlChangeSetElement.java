package io.github.htshame.change.set;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlChangeSetElement implements ChangeSetElement {

    private final JsonNode yamlJsonNode;
    private final String name;

    public YamlChangeSetElement(JsonNode yamlJsonNode,
                                String name) {
        this.yamlJsonNode = yamlJsonNode;
        this.name = name;
    }

    @Override
    public String getName() {
        return name != null ? name : "(root)";
    }

    @Override
    public boolean hasProperty(String propertyName) {
        return yamlJsonNode.has(propertyName);
    }

    @Override
    public String getPropertyValue(String propertyName) {
        JsonNode prop = yamlJsonNode.get(propertyName);
        return (prop != null && prop.isValueNode()) ? prop.asText() : null;
    }

    @Override
    public List<ChangeSetElement> getChildren() {
        List<ChangeSetElement> children = new ArrayList<>();

        if (yamlJsonNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = yamlJsonNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                JsonNode value = entry.getValue();

                // Wrap only objects or arrays
                if (value.isObject() || value.isArray()) {
                    children.add(new YamlChangeSetElement(value, entry.getKey()));
                }
            }
        } else if (yamlJsonNode.isArray()) {
            for (JsonNode item : yamlJsonNode) {
                if (item.isObject()) {
                    Iterator<String> fieldNames = item.fieldNames();
                    if (fieldNames.hasNext()) {
                        String childName = fieldNames.next();
                        JsonNode childValue = item.get(childName);
                        children.add(new YamlChangeSetElement(childValue, childName));
                    }
                }
            }
        }

        return children;
    }

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> result = new LinkedHashMap<>();
        if (yamlJsonNode.isObject()) {
            yamlJsonNode.fields().forEachRemaining(e -> {
                if (e.getValue().isValueNode()) {
                    result.put(e.getKey(), e.getValue().asText());
                }
            });
        }
        return result;
    }

    @Override
    public String getValue() {
        if (yamlJsonNode.isValueNode()) {
            return yamlJsonNode.asText();
        }
        return "";
    }

    @Override
    public List<ChangeSetElement> findElementsByName(ChangeSetElement root, String name) {
        List<ChangeSetElement> result = new ArrayList<>();
        traverse(root, name, result);
        return result;
    }

    private void traverse(ChangeSetElement element, String name, List<ChangeSetElement> result) {
        if (name.equals(element.getName())) {
            result.add(element);
        }
        for (ChangeSetElement child : element.getChildren()) {
            traverse(child, name, result);
        }
    }
//
//    @Override
//    public ChangeSetElement getParent() {
//        return null;
//    }
}
