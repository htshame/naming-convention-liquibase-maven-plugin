package io.github.htshame.rule;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.rule.processor.AttrEndsWithConditionedProcessor;
import io.github.htshame.rule.processor.AttrEndsWithProcessor;
import io.github.htshame.rule.processor.AttrStartsWithProcessor;
import io.github.htshame.rule.processor.NoHyphensInAttributesProcessor;
import io.github.htshame.rule.processor.NoLowercaseInAttributesProcessor;
import io.github.htshame.rule.processor.NoUnderscoresInAttributesProcessor;
import io.github.htshame.rule.processor.NoUppercaseInAttributesProcessor;
import io.github.htshame.rule.processor.TagMustExistProcessor;

import java.util.EnumMap;

/**
 * Rule processor registry.
 */
public final class RuleProcessorRegistry {

    /**
     * Map of rule processors mapped to the corresponding {@link RuleEnum}.
     */
    private static final EnumMap<RuleEnum, RuleFactory> RULE_MAP = new EnumMap<>(RuleEnum.class);

    static {
        RULE_MAP.put(RuleEnum.TAG_MUST_EXIST, TagMustExistProcessor::instantiate);
        RULE_MAP.put(RuleEnum.ATTRIBUTE_STARTS_WITH, AttrStartsWithProcessor::instantiate);
        RULE_MAP.put(RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED, AttrEndsWithConditionedProcessor::instantiate);
        RULE_MAP.put(RuleEnum.NO_HYPHENS_IN_ATTRIBUTES, NoHyphensInAttributesProcessor::instantiate);
        RULE_MAP.put(RuleEnum.ATTRIBUTE_ENDS_WITH, AttrEndsWithProcessor::instantiate);
        RULE_MAP.put(RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES, NoUnderscoresInAttributesProcessor::instantiate);
        RULE_MAP.put(RuleEnum.NO_UPPERCASE_IN_ATTRIBUTES, NoUppercaseInAttributesProcessor::instantiate);
        RULE_MAP.put(RuleEnum.NO_LOWERCASE_IN_ATTRIBUTES, NoLowercaseInAttributesProcessor::instantiate);
    }

    private RuleProcessorRegistry() {
    }

    /**
     * Get factory by rule.
     *
     * @param ruleEnum - rule.
     * @return corresponding processor.
     */
    public static RuleFactory getFactory(final RuleEnum ruleEnum) {
        return RULE_MAP.get(ruleEnum);
    }
}
