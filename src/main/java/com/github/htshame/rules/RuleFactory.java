package com.github.htshame.rules;

import org.w3c.dom.Element;

@FunctionalInterface
public interface RuleFactory {

    Rule fromXml(Element element);

}
