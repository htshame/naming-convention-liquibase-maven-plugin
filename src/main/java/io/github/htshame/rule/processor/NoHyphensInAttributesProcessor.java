package io.github.htshame.rule.processor;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.rule.Rule;
import io.github.htshame.util.RuleUtil;
import io.github.htshame.util.parser.ExclusionParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.htshame.util.RuleUtil.isExcludedByAncestorTag;

/**
 * Business logic for <code>no-hyphens-in-attributes</code> rule.
 * <p>
 * Checks that the contents of changeLog file do not contain hyphens.
 * <p>
 * E.g.:
 * <p>
 * Rule configuration:
 * {@code <rule name="no-hyphens-in-attributes"/>}
 */
public class NoHyphensInAttributesProcessor implements Rule {

    private static final List<String> EXCLUDED_ATTRIBUTES = Arrays.asList("id", "author");
    private static final String HYPHEN = "-";

    private final Set<String> excludedAttrs;

    /**
     * Constructor.
     *
     * @param excludedAttrs - excluded attributes.
     */
    public NoHyphensInAttributesProcessor(final Set<String> excludedAttrs) {
        this.excludedAttrs = excludedAttrs != null ? excludedAttrs : new HashSet<>();
    }

    /**
     * Validate changeSet.
     *
     * @param changeSetElement  - changeSet element.
     * @param exclusionParser   - exclusion parser.
     * @param changeLogFileName - changeLog file name.
     * @throws ValidationException - thrown if validation fails.
     */
    @Override
    public void validate(final ChangeSetElement changeSetElement,
                         final ExclusionParser exclusionParser,
                         final String changeLogFileName) throws ValidationException {
        if (RuleUtil.shouldSkipProcessingRule(changeSetElement, exclusionParser, changeLogFileName, getName())) {
            return;
        }
        List<String> errors = validateElement(changeSetElement, new ArrayList<>());
        if (!errors.isEmpty()) {
            throw new ValidationException(RuleUtil.composeErrorMessage(changeSetElement, getName(), errors));
        }
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.NO_HYPHENS_IN_ATTRIBUTES;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link NoHyphensInAttributesProcessor}.
     */
    public static NoHyphensInAttributesProcessor instantiate(final Element element) {
        Set<String> excludedParents = new HashSet<>();
        NodeList excludedTags = element
                .getElementsByTagName(RuleStructureEnum.EXCLUDED_ATTRS.getValue());
        if (excludedTags.getLength() != 0) {
            NodeList excludedTagElements = ((Element) excludedTags.item(0))
                    .getElementsByTagName(RuleStructureEnum.ATTR.getValue());
            for (int j = 0; j < excludedTagElements.getLength(); j++) {
                excludedParents.add(excludedTagElements.item(j).getTextContent());
            }
        }

        return new NoHyphensInAttributesProcessor(excludedParents);
    }

    /**
     * Validate element.
     *
     * @param element - element.
     * @param errors  - list of errors.
     * @return list of errors.
     */
    private List<String> validateElement(final ChangeSetElement element,
                                         final List<String> errors) {
        Map<String, String> attributes = element.getProperties();

        for (Map.Entry<String, String> attr : attributes.entrySet()) {
            String attrName = attr.getKey();
            String attrValue = attr.getValue();

            if (!isExcludedByAncestorTag(element)
                    && !EXCLUDED_ATTRIBUTES.contains(attrName)
                    && !excludedAttrs.contains(attrName)
                    && attrValue.contains(HYPHEN)) {
                String errorMessage = String.format(
                        "Attribute %s in element <%s> contains hyphen in value: [%s].",
                        attrName,
                        element.getName(),
                        attrValue);
                errors.add(errorMessage);
            }
        }

        List<ChangeSetElement> children = element.getChildren();
        for (ChangeSetElement child : children) {
            validateElement(child, errors);
        }

        return errors;
    }
}
