package io.github.htshame.change.set;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * XML changeSet representation.
 */
public class XmlChangeSetElement implements ChangeSetElement {

    private final Element element;

    /**
     * Constructor.
     *
     * @param element - XML element containing the changeSet.
     */
    public XmlChangeSetElement(final Element element) {
        this.element = element;
    }

    /**
     * Get element name.
     *
     * @return element name.
     */
    @Override
    public String getName() {
        return element.getTagName();
    }

    /**
     * Whether element has a provided property.
     *
     * @param name - property name.
     * @return <code>true</code> if contains, <code>false</code> - if not.
     */
    @Override
    public boolean hasProperty(final String name) {
        return element.hasAttribute(name);
    }

    /**
     * Get element value by name.
     *
     * @param name - element name.
     * @return element value.
     */
    @Override
    public String getPropertyValue(final String name) {
        return element.getAttribute(name);
    }

    /**
     * Get child elements.
     *
     * @return list of child elements.
     */
    @Override
    public List<ChangeSetElement> getChildren() {
        NodeList childNodes = element.getChildNodes();
        List<ChangeSetElement> children = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node n = childNodes.item(i);
            if (n instanceof Element) {
                Element e = (Element) n;
                children.add(new XmlChangeSetElement(e));
            }
        }
        return children;
    }

    /**
     * Get parent element.
     *
     * @return parent element.
     */
    @Override
    public ChangeSetElement getParent() {
        Node parent = element.getParentNode();
        return (parent instanceof Element) ? new XmlChangeSetElement((Element) parent) : null;
    }

    /**
     * Get properties.
     *
     * @return property-value map.
     */
    @Override
    public Map<String, String> getProperties() {
        NamedNodeMap attrs = element.getAttributes();
        Map<String, String> result = new LinkedHashMap<>();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr) attrs.item(i);
            result.put(attr.getName(), attr.getValue());
        }
        return result;
    }

    /**
     * Find elements by name.
     *
     * @param root - root object.
     * @param name - element name.
     * @return list of elements with the provided name.
     */
    @Override
    public List<ChangeSetElement> findElementsByName(final ChangeSetElement root,
                                                     final String name) {
        List<ChangeSetElement> result = new ArrayList<>();
        traverse(root, name, result);
        return result;
    }

    private void traverse(final ChangeSetElement changeSetElement,
                          final String name,
                          final List<ChangeSetElement> result) {
        if (name.equals(changeSetElement.getName())) {
            result.add(changeSetElement);
        }
        for (ChangeSetElement child : changeSetElement.getChildren()) {
            traverse(child, name, result);
        }
    }

    /**
     * Get value.
     *
     * @return value.
     */
    @Override
    public String getValue() {
        Node firstChild = element.getFirstChild();
        if (firstChild == null) {
            return "";
        }
        return firstChild.getNodeValue();
    }
}
