package com.github.htshame.parser;

import com.github.htshame.enums.RuleEnum;
import com.github.htshame.enums.RuleStructureEnum;
import com.github.htshame.exception.RuleParserException;
import com.github.htshame.rule.processor.AttrNotEndsWithProcessor;
import com.github.htshame.rule.processor.AttrNotStartsWithProcessor;
import com.github.htshame.rule.processor.NoHyphensInAttributesProcessor;
import com.github.htshame.rule.Rule;
import com.github.htshame.rule.RuleFactory;
import com.github.htshame.rule.processor.TagMustExistProcessor;
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
 *<rules>
 *    <rule type="tag-must-exist">
 *         <requiredTag>comment</requiredTag>
 *         <excludedAncestorTags>
 *             <tag>databaseChangeLog</tag>
 *             <tag>include</tag>
 *         </excludedAncestorTags>
 *     </rule>
 *
 *    <rule type="attr-not-starts-with">
 *         <tag>createIndex</tag>
 *         <targetAttribute>indexName</targetAttribute>
 *         <requiredPrefix>idx_</requiredPrefix>
 *     </rule>
 *
 *    <rule type="attr-not-ends-with">
 *         <tag>createIndex</tag>
 *         <conditionAttribute>unique</conditionAttribute>
 *         <conditionValue>true</conditionValue>
 *         <targetAttribute>indexName</targetAttribute>
 *         <requiredSuffix>_unique</requiredSuffix>
 *     </rule>
 *    <rule type="no-hyphens-in-attributes"/>
 * </rules>
 * </code></pre>
 */
public class RuleParser {

    /**
     * Map of rule processors mapped to the corresponding {@link RuleEnum}.
     */
    private final Map<RuleEnum, RuleFactory> ruleMap = Map.of(
            RuleEnum.TAG_MUST_EXIST, TagMustExistProcessor::fromXml,
            RuleEnum.ATTRIBUTE_NOT_STARTS_WITH, AttrNotStartsWithProcessor::fromXml,
            RuleEnum.ATTRIBUTE_NOT_ENDS_WITH, AttrNotEndsWithProcessor::fromXml,
            RuleEnum.NO_HYPHENS_IN_ATTRIBUTES, NoHyphensInAttributesProcessor::fromXml
    );

    /**
     * Parse rules file.
     *
     * @param rulesetFile - file with rules.
     * @return set of rules.
     * @throws RuleParserException - thrown if parsing fails.
     */
    public Set<Rule> parseRules(final File rulesetFile) throws RuleParserException {
        Set<Rule> rules = new HashSet<>();
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(rulesetFile);
            NodeList ruleNodes = document.getElementsByTagName(RuleStructureEnum.RULE_TAG.getValue());
            for (int i = 0; i < ruleNodes.getLength(); i++) {
                Element ruleElement = (Element) ruleNodes.item(i);
                RuleEnum ruleType =
                        RuleEnum.fromValue(ruleElement.getAttribute(RuleStructureEnum.NAME_ATTR.getValue()));

                RuleFactory ruleFactory = ruleMap.get(ruleType);
                if (ruleFactory == null) {
                    throw new RuleParserException("Unknown rule type: [" + ruleType + "]");
                }
                try {
                    Rule rule = ruleFactory.fromXml(ruleElement);
                    rules.add(rule);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to instantiate rule for type: " + ruleType, e);
                }
            }
        } catch (Exception e) {
            throw new RuleParserException("Error parsing ruleset", e);
        }
        return rules;
    }

}
