package com.github.htshame.rules;

import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;

public class AttrNotEndsWithRule implements Rule {

    private final String tag;
    private final String conditionAttribute;
    private final String conditionValue;
    private final String targetAttribute;
    private final String requiredSuffix;

    public AttrNotEndsWithRule(String tag,
                               String conditionAttribute,
                               String conditionValue,
                               String targetAttribute,
                               String requiredSuffix) {
        this.tag = tag;
        this.conditionAttribute = conditionAttribute;
        this.conditionValue = conditionValue;
        this.targetAttribute = targetAttribute;
        this.requiredSuffix = requiredSuffix;
    }

    @Override
    public void validate(Document doc, File file) throws MojoExecutionException {
        NodeList elements = doc.getElementsByTagName(tag);
        for (int i = 0; i < elements.getLength(); i++) {
            Element elem = (Element) elements.item(i);
            if (conditionValue.equals(elem.getAttribute(conditionAttribute))) {
                String actualValue = elem.getAttribute(targetAttribute);
                if (!actualValue.endsWith(requiredSuffix)) {
                    throw new MojoExecutionException("[" + file.getName() + "] <" + tag +
                            "> with " + conditionAttribute + "=\"" + conditionValue +
                            "\" must have " + targetAttribute + " ending with \"" + requiredSuffix +
                            "\", but found: \"" + actualValue + "\"");
                }
            }
        }
    }
}
