package com.github.htshame.parser;

import com.github.htshame.enums.RuleEnum;
import com.github.htshame.exception.RuleParserException;
import com.github.htshame.rules.AttrNotEndsWithRule;
import com.github.htshame.rules.AttrNotStartsWithRule;
import com.github.htshame.rules.NoHyphensInAttributesRule;
import com.github.htshame.rules.Rule;
import com.github.htshame.rules.RuleFactory;
import com.github.htshame.rules.TagMustExistRule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
public class RuleParser {

    private final Map<RuleEnum, RuleFactory> RULE_MAP = Map.of(
            RuleEnum.TAG_MUST_EXIST, TagMustExistRule::fromXml,
            RuleEnum.ATTRIBUTE_NOT_STARTS_WITH, AttrNotStartsWithRule::fromXml,
            RuleEnum.ATTRIBUTE_NOT_ENDS_WITH, AttrNotEndsWithRule::fromXml,
            RuleEnum.NO_HYPHENS_IN_ATTRIBUTES, NoHyphensInAttributesRule::fromXml
    );

    public Set<Rule> parseRules(File rulesetFile) throws RuleParserException {
        Set<Rule> rules = new HashSet<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(rulesetFile);
            NodeList ruleNodes = doc.getElementsByTagName(RuleStructureEnum.RULE_TAG.getValue());
            for (int i = 0; i < ruleNodes.getLength(); i++) {
                Element ruleElem = (Element) ruleNodes.item(i);
                RuleEnum type = RuleEnum.fromValue(ruleElem.getAttribute(RuleStructureEnum.NAME_ATTR.getValue()));

                RuleFactory ruleFactory = RULE_MAP.get(type);
                if (ruleFactory == null) {
                    throw new RuleParserException("Unknown rule type: [" + type + "]");
                }
                try {
                    Rule rule = ruleFactory.fromXml(ruleElem);
                    rules.add(rule);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to instantiate rule for type: " + type, e);
                }
            }
        } catch (Exception e) {
            throw new RuleParserException("Error parsing ruleset", e);
        }
        return rules;
    }

    public static String getText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        return nodes.getLength() > 0 ? nodes.item(0).getTextContent().trim() : null;
    }

    public enum RuleStructureEnum {
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

        public String getValue() {
            return this.value;
        }
    }

}
