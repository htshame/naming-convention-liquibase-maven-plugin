package io.github.htshame.parser;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.enums.RuleTypeEnum;
import io.github.htshame.exception.RuleInstantiationException;
import io.github.htshame.exception.RuleParserException;
import io.github.htshame.parser.rule.RuleFactory;
import io.github.htshame.parser.rule.RuleProcessorRegistry;
import io.github.htshame.rule.Rule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;

import static io.github.htshame.util.XmlUtil.newXmlDocumentBuilder;

/**
 * Parses the rules XML file.
 * <p>Example:</p>
 * <pre><code>
 * &lt;rules&gt;
 *     &lt;rule type="tag-must-exist"&gt;
 *         &lt;requiredTag&gt;comment&lt;/requiredTag&gt;
 *         &lt;requiredForChildTag&gt;
 *             &lt;tag&gt;rollback&lt;/tag&gt;
 *         &lt;/requiredForChildTag&gt;
 *     &lt;/rule&gt;
 *
 *     &lt;rule type="attr-starts-with"&gt;
 *         &lt;tag&gt;createIndex&lt;/tag&gt;
 *         &lt;targetAttr&gt;indexName&lt;/targetAttr&gt;
 *         &lt;requiredPrefix&gt;idx_&lt;/requiredPrefix&gt;
 *     &lt;/rule&gt;
 *
 *     &lt;rule type="attr-ends-with-conditioned"&gt;
 *         &lt;tag&gt;createIndex&lt;/tag&gt;
 *         &lt;conditionAttr&gt;unique&lt;/conditionAttr&gt;
 *         &lt;conditionValue&gt;true&lt;/conditionValue&gt;
 *         &lt;targetAttr&gt;indexName&lt;/targetAttr&gt;
 *         &lt;requiredSuffix&gt;_unique&lt;/requiredSuffix&gt;
 *     &lt;/rule&gt;
 *
 *     &lt;rule type="no-hyphens-in-attributes"/&gt;
 * &lt;/rules&gt;
 * </code></pre>
 */
public final class RuleParser {

    /**
     * All-rule map.
     */
    private static final EnumMap<RuleTypeEnum, Function<RuleEnum, RuleFactory<? extends Rule>>> RULE_MAP =
            new EnumMap<>(RuleTypeEnum.class);

    static {
        RULE_MAP.put(RuleTypeEnum.CHANGE_SET_RULE, RuleProcessorRegistry::getChangeSetRuleFactory);
        RULE_MAP.put(RuleTypeEnum.CHANGE_LOG_FILE_RULE, RuleProcessorRegistry::getChangeLogFileRuleFactory);
        RULE_MAP.put(RuleTypeEnum.CHANGE_LOG_RULE, RuleProcessorRegistry::getChangeLogRuleFactory);
    }

    /**
     * Private constructor.
     */
    private RuleParser() {

    }

    /**
     * Parse rules file.
     *
     * @param rulesetFile - file with rules.
     * @return list of rules.
     * @throws RuleParserException - thrown if parsing fails.
     */
    public static List<Rule> parseRules(final File rulesetFile) {
        List<Rule> rules = new ArrayList<>();
        try {
            Document document = newXmlDocumentBuilder().parse(rulesetFile);
            NodeList ruleNodes = document.getElementsByTagName(RuleStructureEnum.RULE.getValue());
            for (int i = 0; i < ruleNodes.getLength(); i++) {
                Element ruleElement = (Element) ruleNodes.item(i);
                RuleEnum ruleType = RuleEnum.fromValue(
                        ruleElement.getAttribute(RuleStructureEnum.NAME_ATTR.getValue()));
                try {
                    rules.add(RULE_MAP.get(ruleType.getType()).apply(ruleType).instantiate(ruleElement));
                } catch (Exception e) {
                    throw new RuleInstantiationException(
                            "Failed to instantiate rule for type: [" + ruleType.getValue() + "]", e);
                }
            }
        } catch (Exception e) {
            throw new RuleParserException("Error parsing ruleset XML file. Message: " + e.getMessage(), e);
        }
        return rules;
    }
}
