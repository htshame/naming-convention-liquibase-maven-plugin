package io.github.htshame.rule;

import org.w3c.dom.Element;

/**
 * Rule factory.
 * <br>
 * @param <T> - rule type.
 * Enforces that all rule handlers implement {@link RuleFactory#instantiate} method.
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
