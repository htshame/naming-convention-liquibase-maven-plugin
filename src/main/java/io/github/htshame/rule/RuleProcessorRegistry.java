package io.github.htshame.rule;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.rule.processor.AttrEndsWithConditionedProcessor;
import io.github.htshame.rule.processor.AttrEndsWithProcessor;
import io.github.htshame.rule.processor.AttrMustExistInTagProcessor;
import io.github.htshame.rule.processor.AttrNotEndsWithConditionedProcessor;
import io.github.htshame.rule.processor.AttrNotStartsWithConditionedProcessor;
import io.github.htshame.rule.processor.AttrStartsWithConditionedProcessor;
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
    private static final EnumMap<RuleEnum, RuleFactory> CHANGE_SET_RULE = new EnumMap<>(RuleEnum.class);

    static {
        CHANGE_SET_RULE.put(
                RuleEnum.TAG_MUST_EXIST, TagMustExistProcessor::instantiate);
        CHANGE_SET_RULE.put(
                RuleEnum.ATTRIBUTE_STARTS_WITH, AttrStartsWithProcessor::instantiate);
        CHANGE_SET_RULE.put(
                RuleEnum.ATTRIBUTE_STARTS_WITH_CONDITIONED, AttrStartsWithConditionedProcessor::instantiate);
        CHANGE_SET_RULE.put(
                RuleEnum.ATTRIBUTE_NOT_STARTS_WITH_CONDITIONED, AttrNotStartsWithConditionedProcessor::instantiate);
        CHANGE_SET_RULE.put(
                RuleEnum.NO_HYPHENS_IN_ATTRIBUTES, NoHyphensInAttributesProcessor::instantiate);
        CHANGE_SET_RULE.put(
                RuleEnum.ATTRIBUTE_ENDS_WITH, AttrEndsWithProcessor::instantiate);
        CHANGE_SET_RULE.put(
                RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED, AttrEndsWithConditionedProcessor::instantiate);
        CHANGE_SET_RULE.put(
                RuleEnum.ATTRIBUTE_NOT_ENDS_WITH_CONDITIONED, AttrNotEndsWithConditionedProcessor::instantiate);
        CHANGE_SET_RULE.put(
                RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES, NoUnderscoresInAttributesProcessor::instantiate);
        CHANGE_SET_RULE.put(
                RuleEnum.NO_UPPERCASE_IN_ATTRIBUTES, NoUppercaseInAttributesProcessor::instantiate);
        CHANGE_SET_RULE.put(
                RuleEnum.NO_LOWERCASE_IN_ATTRIBUTES, NoLowercaseInAttributesProcessor::instantiate);
        CHANGE_SET_RULE.put(
                RuleEnum.ATTRIBUTE_MUST_EXIST_IN_TAG, AttrMustExistInTagProcessor::instantiate);
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
        return CHANGE_SET_RULE.get(ruleEnum);
    }
}
