package com.github.htshame.rules;

import com.github.htshame.enums.RuleEnum;
import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;

public class AttrNotStartsWithRule implements Rule {

    private final String tag;
    private final String attribute;
    private final String prefix;

    public AttrNotStartsWithRule(String tag, String attribute, String prefix) {
        this.tag = tag;
        this.attribute = attribute;
        this.prefix = prefix;
    }

    @Override
    public RuleEnum getName() {
        return RuleEnum.ATTRIBUTE_NOT_STARTS_WITH;
    }

    @Override
    public void validate(Document doc, File file) throws MojoExecutionException {
        NodeList nodes = doc.getElementsByTagName(tag);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element elem = (Element) nodes.item(i);
            if (elem.hasAttribute(attribute) && !elem.getAttribute(attribute).startsWith(prefix)) {
                throw new MojoExecutionException(file.getName() + ": <" + tag + " " + attribute +
                        "=\"" + elem.getAttribute(attribute) + "\"> must start with \"" + prefix + "\"");
            }
        }
    }
}
