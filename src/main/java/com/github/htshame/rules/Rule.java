package com.github.htshame.rules;

import com.github.htshame.enums.RuleEnum;
import com.github.htshame.exception.ValidationException;
import org.w3c.dom.Document;

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
     * Validates the document against the rule which implements {@link Rule}.
     *
     * @param document - document.
     * @throws ValidationException - thrown if validation fails.
     */
    void validate(Document document) throws ValidationException;
}
