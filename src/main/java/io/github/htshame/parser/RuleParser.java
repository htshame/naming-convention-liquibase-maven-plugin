package io.github.htshame.parser;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.RuleParserException;
import io.github.htshame.rule.Rule;
import io.github.htshame.rule.RuleFactory;
import io.github.htshame.rule.factory.RuleProcessorFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Parses the rules XML file.
 * <p>Example:</p>
 * <pre><code>
 * &lt;rules&gt;
 *     &lt;rule type="tag-must-exist"&gt;
 *         &lt;requiredTag&gt;comment&lt;/requiredTag&gt;
 *         &lt;excludedAncestorTags&gt;
 *             &lt;tag&gt;databaseChangeLog&lt;/tag&gt;
 *             &lt;tag&gt;include&lt;/tag&gt;
 *         &lt;/excludedAncestorTags&gt;
 *     &lt;/rule&gt;
 *
 *     &lt;rule type="attr-starts-with"&gt;
 *         &lt;tag&gt;createIndex&lt;/tag&gt;
 *         &lt;targetAttribute&gt;indexName&lt;/targetAttribute&gt;
 *         &lt;requiredPrefix&gt;idx_&lt;/requiredPrefix&gt;
 *     &lt;/rule&gt;
 *
 *     &lt;rule type="attr-ends-with-conditioned"&gt;
 *         &lt;tag&gt;createIndex&lt;/tag&gt;
 *         &lt;conditionAttribute&gt;unique&lt;/conditionAttribute&gt;
 *         &lt;conditionValue&gt;true&lt;/conditionValue&gt;
 *         &lt;targetAttribute&gt;indexName&lt;/targetAttribute&gt;
 *         &lt;requiredSuffix&gt;_unique&lt;/requiredSuffix&gt;
 *     &lt;/rule&gt;
 *
 *     &lt;rule type="no-hyphens-in-attributes"/&gt;
 * &lt;/rules&gt;
 * </code></pre>
 */
public class RuleParser {

    /**
     * Default constructor.
     */
    public RuleParser() {

    }

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

                RuleFactory ruleFactory = RuleProcessorFactory.getFactory(ruleType);
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
