package com.github.htshame.rules;

import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class NoHyphensInAttributesRule implements Rule {
    private static final List<String> EXCLUDED_ATTRIBUTES = Arrays.asList("id", "author");

    @Override
    public void validate(Document doc, File file) throws MojoExecutionException {
        validateElement(doc.getDocumentElement(), file);
    }

    private void validateElement(Element element, File file) throws MojoExecutionException {
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attr = (Attr) attributes.item(i);
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (!EXCLUDED_ATTRIBUTES.contains(attrName) && attrValue.contains("-")) {
                throw new MojoExecutionException("Attribute '" + attrName + "' in element <" +
                        element.getTagName() + "> in file " + file.getName() +
                        " contains hyphen in value: " + attrValue);
            }
        }

        // Traverse children
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                validateElement((Element) child, file);
            }
        }
    }
}
