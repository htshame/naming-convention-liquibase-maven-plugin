package com.github.htshame.parser;

import com.github.htshame.exception.RuleParserException;
import com.github.htshame.rules.AttrNotEndsWithRule;
import com.github.htshame.rules.AttrNotStartsWithRule;
import com.github.htshame.rules.NoHyphensInAttributesRule;
import com.github.htshame.rules.Rule;
import com.github.htshame.rules.TagMustExistRule;
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

/**
 * This class parses rules XML file.
 * E.g.:
 * <pre><code>
 * <rules>
 *     <rule type="tag-must-exist">
 *         <requiredTag>comment</requiredTag>
 *         <excludedAncestorTags>
 *             <tag>databaseChangeLog</tag>
 *             <tag>include</tag>
 *         </excludedAncestorTags>
 *     </rule>
 *
 *     <rule type="attr-not-starts-with">
 *         <tag>createIndex</tag>
 *         <targetAttribute>indexName</targetAttribute>
 *         <requiredPrefix>idx_</requiredPrefix>
 *     </rule>
 *
 *     <rule type="attr-not-ends-with">
 *         <tag>createIndex</tag>
 *         <conditionAttribute>unique</conditionAttribute>
 *         <conditionValue>true</conditionValue>
 *         <targetAttribute>indexName</targetAttribute>
 *         <requiredSuffix>_unique</requiredSuffix>
 *     </rule>
 *     <rule type="no-hyphens-in-attributes"/>
 * </rules>
 * </code></pre>
 */
public final class RuleParser {

    private RuleParser() {

    }

    public static Set<Rule> parseRules(File rulesetFile) throws RuleParserException {
        Set<Rule> rules = new HashSet<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(rulesetFile);
            NodeList ruleNodes = doc.getElementsByTagName(RuleStructureEnum.RULE_TAG.getValue());
            for (int i = 0; i < ruleNodes.getLength(); i++) {
                Element ruleElem = (Element) ruleNodes.item(i);
                String type = ruleElem.getAttribute(RuleStructureEnum.NAME_ATTR.getValue());

                if (TAG_MUST_EXIST.getValue().equalsIgnoreCase(type)) {
                    String requiredChild = getText(ruleElem, RuleStructureEnum.REQUIRED_TAG.getValue());
                    Set<String> excludedParents = new HashSet<>();
                    NodeList excludedTagElements = ((Element) ruleElem
                            .getElementsByTagName(RuleStructureEnum.EXCLUDED_ANCESTOR_TAGS.getValue()).item(0))
                            .getElementsByTagName(RuleStructureEnum.TAG_TAG.getValue());
                    for (int j = 0; j < excludedTagElements.getLength(); j++) {
                        excludedParents.add(excludedTagElements.item(j).getTextContent());
                    }
                    rules.add(new TagMustExistRule(requiredChild, excludedParents));
                } else if (ATTRIBUTE_NOT_STARTS_WITH.getValue().equalsIgnoreCase(type)) {
                    rules.add(new AttrNotStartsWithRule(
                            getText(ruleElem, RuleStructureEnum.TAG_TAG.getValue()),
                            getText(ruleElem, RuleStructureEnum.TARGET_ATTRIBUTE_TAG.getValue()),
                            getText(ruleElem, RuleStructureEnum.REQUIRED_PREFIX_TAG.getValue())
                    ));
                } else if (ATTRIBUTE_NOT_ENDS_WITH.getValue().equalsIgnoreCase(type)) {
                    rules.add(new AttrNotEndsWithRule(
                            getText(ruleElem, RuleStructureEnum.TAG_TAG.getValue()),
                            getText(ruleElem, RuleStructureEnum.CONDITION_ATTRIBUTE_TAG.getValue()),
                            getText(ruleElem, RuleStructureEnum.CONDITION_VALUE_TAG.getValue()),
                            getText(ruleElem, RuleStructureEnum.TARGET_ATTRIBUTE_TAG.getValue()),
                            getText(ruleElem, RuleStructureEnum.REQUIRED_SUFFIX_TAG.getValue())));
                } else if (NO_HYPHENS_IN_ATTRIBUTES.getValue().equalsIgnoreCase(type)) {
                    rules.add(new NoHyphensInAttributesRule());
                } else {
                    throw new RuleParserException("Unknown rule type: [" + type + "]");
                }
            }
        } catch (Exception e) {
            throw new RuleParserException("Error parsing ruleset", e);
        }
        return rules;
    }

    private static String getText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        return nodes.getLength() > 0 ? nodes.item(0).getTextContent().trim() : null;
    }

    private enum RuleStructureEnum {
        RULE_TAG("rule"),
        NAME_ATTR("name"),
        REQUIRED_TAG("requiredTag"),
        EXCLUDED_ANCESTOR_TAGS("excludedAncestorTags"),
        TAG_TAG("tag"),
        TARGET_ATTRIBUTE_TAG("targetAttribute"),
        REQUIRED_PREFIX_TAG("requiredPrefix"),
        CONDITION_ATTRIBUTE_TAG("conditionAttribute"),
        CONDITION_VALUE_TAG("conditionValue"),
        REQUIRED_SUFFIX_TAG("requiredSuffix");

        private final String value;

        RuleStructureEnum(final String value) {
            this.value = value;
        }

        private String getValue() {
            return this.value;
        }
    }

}
