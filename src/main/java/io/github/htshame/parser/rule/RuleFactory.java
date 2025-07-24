package io.github.htshame.parser.rule;

import io.github.htshame.rule.Rule;
import org.w3c.dom.Element;

/**
 * Rule factory.
 * <br>
 * Enforces that all rule handlers implement {@link RuleFactory#instantiate} method.
 * @param <T> - rule type.
 */
@FunctionalInterface
public interface RuleFactory<T extends Rule> {

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - changeSet element.
     * @return instance of {@link ChangeSetRule}.
     */
    T instantiate(Element element);

}
