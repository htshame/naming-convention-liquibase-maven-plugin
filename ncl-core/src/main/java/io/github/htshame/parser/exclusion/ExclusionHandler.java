package io.github.htshame.parser.exclusion;

import io.github.htshame.parser.ExclusionParser;
import org.w3c.dom.Element;

/**
 * Exclusion handler interface.
 */
public interface ExclusionHandler {

    /**
     * Handle exclusion parsing.
     *
     * @param element - element.
     * @param parser  - exclusion parser.
     */
    void handle(Element element, ExclusionParser parser);

}
