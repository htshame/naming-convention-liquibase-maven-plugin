package io.github.htshame.rule.factory;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.rule.RuleFactory;
import io.github.htshame.rule.processor.AttrEndsWithConditionedProcessor;
import io.github.htshame.rule.processor.AttrEndsWithProcessor;
import io.github.htshame.rule.processor.AttrStartsWithProcessor;
import io.github.htshame.rule.processor.NoHyphensInAttributesProcessor;
import io.github.htshame.rule.processor.NoUnderscoresInAttributesProcessor;
import io.github.htshame.rule.processor.TagMustExistProcessor;

import java.util.Map;

/**
 * Rule processor factory.
 */
public final class RuleProcessorFactory {

    /**
     * Map of rule processors mapped to the corresponding {@link RuleEnum}.
     */
    private static final Map<RuleEnum, RuleFactory> RULE_MAP = Map.of(
            RuleEnum.TAG_MUST_EXIST, TagMustExistProcessor::fromXml,
            RuleEnum.ATTRIBUTE_STARTS_WITH, AttrStartsWithProcessor::fromXml,
            RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED, AttrEndsWithConditionedProcessor::fromXml,
            RuleEnum.NO_HYPHENS_IN_ATTRIBUTES, NoHyphensInAttributesProcessor::fromXml,
            RuleEnum.ATTRIBUTE_ENDS_WITH, AttrEndsWithProcessor::fromXml,
            RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES, NoUnderscoresInAttributesProcessor::fromXml
    );

    private RuleProcessorFactory() {
    }

    /**
     * Get factory by rule.
     *
     * @param ruleEnum - rule.
     * @return corresponding processor.
     */
    public static RuleFactory getFactory(final RuleEnum ruleEnum) {
        RuleFactory factory = RULE_MAP.get(ruleEnum);
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for rule: " + ruleEnum);
        }
        return factory;
    }
}
