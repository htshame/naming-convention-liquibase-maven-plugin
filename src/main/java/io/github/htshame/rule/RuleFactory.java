package io.github.htshame.rule;

import org.w3c.dom.Element;

/**
 * Rule factory.
 * Enforces that all rule handlers implement {@link RuleFactory#fromXml} method.
 */
@FunctionalInterface
public interface RuleFactory {

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link Rule}.
     */
    Rule fromXml(Element element);

}
