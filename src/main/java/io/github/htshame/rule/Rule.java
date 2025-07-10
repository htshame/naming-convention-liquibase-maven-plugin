package io.github.htshame.rule;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleTypeEnum;

/**
 * Rule interface.
 */
public interface Rule {

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    RuleEnum getName();

    /**
     * Get rule type.
     *
     * @return rule type.
     */
    RuleTypeEnum getType();
}
