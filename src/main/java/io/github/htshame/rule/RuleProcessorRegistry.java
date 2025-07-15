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
import io.github.htshame.rule.processor.NoSpacesInAttributesProcessor;
import io.github.htshame.rule.processor.NoUnderscoresInAttributesProcessor;
import io.github.htshame.rule.processor.NoUppercaseInAttributesProcessor;
import io.github.htshame.rule.processor.TagMustExistProcessor;
import io.github.htshame.rule.processor.changelog.ChangeLogFileLinesLimitProcessor;
import io.github.htshame.rule.processor.changelog.ChangeLogFileMustMatchRegexpProcessor;

import java.util.EnumMap;

/**
 * Rule processor registry.
 */
public final class RuleProcessorRegistry {

    /**
     * Map of changeSet rule processors, each mapped to the corresponding {@link RuleEnum}.
     */
    private static final EnumMap<RuleEnum, RuleFactory<ChangeSetRule>> CHANGE_SET_RULE = new EnumMap<>(RuleEnum.class);

    /**
     * Map of changeLog rule processors, each mapped to the corresponding {@link RuleEnum}.
     */
    private static final EnumMap<RuleEnum, RuleFactory<ChangeLogRule>> CHANGE_LOG_RULE = new EnumMap<>(RuleEnum.class);

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
        CHANGE_SET_RULE.put(
                RuleEnum.NO_SPACES_IN_ATTRIBUTES, NoSpacesInAttributesProcessor::instantiate);

        CHANGE_LOG_RULE.put(
                RuleEnum.CHANGELOG_FILE_NAME_MUST_MATCH_REGEXP, ChangeLogFileMustMatchRegexpProcessor::instantiate);
        CHANGE_LOG_RULE.put(
                RuleEnum.CHANGELOG_FILE_LINES_LIMIT, ChangeLogFileLinesLimitProcessor::instantiate);
    }

    private RuleProcessorRegistry() {

    }

    /**
     * Get changeSet rule factory by rule.
     *
     * @param ruleEnum - rule.
     * @return corresponding processor.
     */
    public static RuleFactory<ChangeSetRule> getChangeSetRuleFactory(final RuleEnum ruleEnum) {
        return CHANGE_SET_RULE.get(ruleEnum);
    }

    /**
     * Get changeLog rule factory by rule.
     *
     * @param ruleEnum - rule.
     * @return corresponding processor.
     */
    public static RuleFactory<ChangeLogRule> getChangeLogRuleFactory(final RuleEnum ruleEnum) {
        return CHANGE_LOG_RULE.get(ruleEnum);
    }
}
