package io.github.htshame.validator;

import io.github.htshame.enums.RuleTypeEnum;
import io.github.htshame.rule.Rule;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Set;

/**
 * Rule validator factory.
 */
public final class RuleValidatorFactory {

    /**
     * Private constructor.
     */
    private RuleValidatorFactory() {

    }

    /**
     * Instantiate rule validators.
     *
     * @param rules - rules.
     * @return collection of rule validators.
     */
    public static Collection<RuleValidator> instantiate(final Set<Rule> rules) {

        EnumMap<RuleTypeEnum, RuleValidator> validatorMap = new EnumMap<>(RuleTypeEnum.class);
        validatorMap.put(RuleTypeEnum.CHANGE_LOG_FILE_RULE, new ChangeLogRuleValidator());
        validatorMap.put(RuleTypeEnum.CHANGE_SET_RULE, new ChangeSetRuleValidator());

        for (Rule rule : rules) {
            validatorMap.get(rule.getName().getType()).addRule(rule);
        }

        return validatorMap.values();
    }
}
