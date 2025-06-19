package io.github.htshame.rule;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.util.parser.ExclusionParser;
import org.w3c.dom.Element;

/**
 * Interface for rule handling.
 */
public interface Rule {

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    RuleEnum getName();

    /**
     * Validates the changeSet against the rule which implements {@link Rule}.
     *
     * @param changeSetElement  - element containing changeSet.
     * @param exclusionParser   - exclusion parser.
     * @param changeLogFileName - changeLog file name.
     * @throws ValidationException - thrown if validation fails.
     */
    void validate(Element changeSetElement,
                  ExclusionParser exclusionParser,
                  String changeLogFileName) throws ValidationException;

}
