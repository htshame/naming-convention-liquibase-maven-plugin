package com.github.htshame.parser;

import com.github.htshame.rules.AttrNotEndsWithRule;
import com.github.htshame.rules.AttrNotStartsWithRule;
import com.github.htshame.rules.NoHyphensInAttributesRule;
import com.github.htshame.rules.Rule;
import com.github.htshame.rules.TagMustExistRule;
import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static com.github.htshame.enums.RuleEnum.ATTRIBUTE_NOT_ENDS_WITH;
import static com.github.htshame.enums.RuleEnum.ATTRIBUTE_NOT_STARTS_WITH;
import static com.github.htshame.enums.RuleEnum.NO_HYPHENS_IN_ATTRIBUTES;
import static com.github.htshame.enums.RuleEnum.TAG_MUST_EXIST;

public class RuleParser {

    public Set<Rule> loadRules(File rulesetFile) throws MojoExecutionException {
        Set<Rule> rules = new HashSet<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(rulesetFile);
            NodeList ruleNodes = doc.getElementsByTagName("rule");
            for (int i = 0; i < ruleNodes.getLength(); i++) {
                Element ruleElem = (Element) ruleNodes.item(i);
                String type = ruleElem.getAttribute("type");

                if (TAG_MUST_EXIST.getValue().equalsIgnoreCase(type)) {
                    String requiredChild = getText(ruleElem, "requiredTag");
                    Set<String> excludedParents = new HashSet<>();
                    NodeList excludedTagElements = ((Element) ruleElem.getElementsByTagName("excludedAncestorTags").item(0)).getElementsByTagName("tag");
                    for (int j = 0; j < excludedTagElements.getLength(); j++) {
                        excludedParents.add(excludedTagElements.item(j).getTextContent());
                    }
                    rules.add(new TagMustExistRule(requiredChild, excludedParents));
                } else if (ATTRIBUTE_NOT_STARTS_WITH.getValue().equalsIgnoreCase(type)) {
                    rules.add(new AttrNotStartsWithRule(
                            getText(ruleElem, "tag"),
                            getText(ruleElem, "targetAttribute"),
                            getText(ruleElem, "requiredPrefix")
                    ));
                } else if (ATTRIBUTE_NOT_ENDS_WITH.getValue().equalsIgnoreCase(type)) {
                    rules.add(new AttrNotEndsWithRule(
                            getText(ruleElem, "tag"),
                            getText(ruleElem, "conditionAttribute"),
                            getText(ruleElem, "conditionValue"),
                            getText(ruleElem, "targetAttribute"),
                            getText(ruleElem, "requiredSuffix")));
                } else if (NO_HYPHENS_IN_ATTRIBUTES.getValue().equalsIgnoreCase(type)) {
                    rules.add(new NoHyphensInAttributesRule());
                } else {
                    throw new MojoExecutionException("Unknown rule type: " + type);
                }
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Error parsing ruleset", e);
        }
        return rules;
    }

    private String getText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        return nodes.getLength() > 0 ? nodes.item(0).getTextContent().trim() : null;
    }

}
