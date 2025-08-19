package io.github.htshame.parser.exclusion;

import io.github.htshame.enums.ExclusionStructureEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.parser.ExclusionParser;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * ChangeLog exclusion handler.
 */
public class ChangeLogExclusionHandler implements ExclusionHandler {

    /**
     * Default constructor.
     */
    public ChangeLogExclusionHandler() {

    }

    /**
     * Handle changeLog file exclusion parsing.
     *
     * @param element - element.
     * @param parser  - exclusion parser.
     */
    @Override
    public void handle(final Element element,
                       final ExclusionParser parser) {
        String fileName = element.getAttribute(ExclusionStructureEnum.FILE_NAME_ATTR.getValue()).trim();
        String rule = element.getAttribute(ExclusionStructureEnum.RULE_ATTR.getValue()).trim();

        RuleEnum ruleEnum = RuleEnum.fromValue(rule);
        Set<RuleEnum> rules = parser.getChangeLogRuleExclusions()
                .computeIfAbsent(fileName, k -> new HashSet<>());

        if (RuleEnum.ALL_RULES.equals(ruleEnum)) {
            rules.addAll(Arrays.asList(RuleEnum.values()));
        } else {
            rules.add(ruleEnum);
        }
    }
}
