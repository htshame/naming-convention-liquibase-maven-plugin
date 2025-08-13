package io.github.htshame.rule.processor;

import io.github.htshame.changeset.element.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.parser.rule.ChangeSetRule;
import io.github.htshame.util.RuleUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.htshame.util.ErrorMessageUtil.getErrorMessage;
import static io.github.htshame.util.RuleUtil.EXCLUDED_ATTRIBUTES;
import static io.github.htshame.util.RuleUtil.isExcludedByAncestorTag;
import static io.github.htshame.util.RuleUtil.shouldCollectValuesRuleListFormat;

/**
 * Business logic for <code>no-hyphens-in-attributes</code> rule.
 * <p>
 * Checks that the changeSet attributes do not contain hyphens.
 * <p>
 * E.g.:
 * <p>
 * Rule configuration:
 * <pre><code>
 * &lt;rule name="no-hyphens-in-attributes"&gt;
 *     &lt;excludedAttrs&gt;
 *         &lt;attr&gt;defaultValue&lt;/attr&gt;
 *         &lt;attr&gt;defaultValueComputed&lt;/attr&gt;
 *     &lt;/excludedAttrs&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that there are no hyphens in attributes, excluding attributes specified in
 * <code>excludedAttrs</code>.</p>
 */
public class NoHyphensInAttributesProcessor implements ChangeSetRule {

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
     * @param changeLogFormat   - changeLog format.
     * @throws ValidationException - thrown if validation fails.
     */
    @Override
    public void validateChangeSet(final ChangeSetElement changeSetElement,
                                  final ExclusionParser exclusionParser,
                                  final String changeLogFileName,
                                  final ChangeLogFormatEnum changeLogFormat) throws ValidationException {
        if (RuleUtil.shouldSkipProcessingRule(changeSetElement, exclusionParser, changeLogFileName, getName())) {
            return;
        }
        List<String> errors = validateElement(changeSetElement, changeLogFormat, new ArrayList<>());
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
        NodeList excludedAttrs = element
                .getElementsByTagName(RuleStructureEnum.EXCLUDED_ATTRS.getValue());
        Set<String> excludedParents = new HashSet<>();
        if (shouldCollectValuesRuleListFormat(excludedAttrs, RuleStructureEnum.EXCLUDED_ATTRS)) {
            NodeList excludedAttrElements = ((Element) excludedAttrs.item(0))
                    .getElementsByTagName(RuleStructureEnum.ATTR.getValue());
            for (int i = 0; i < excludedAttrElements.getLength(); i++) {
                excludedParents.add(excludedAttrElements.item(i).getTextContent());
            }
        }
        return new NoHyphensInAttributesProcessor(excludedParents);
    }

    /**
     * Validate element.
     *
     * @param element         - element.
     * @param changeLogFormat - changeLog format.
     * @param errors          - list of errors.
     * @return list of errors.
     */
    private List<String> validateElement(final ChangeSetElement element,
                                         final ChangeLogFormatEnum changeLogFormat,
                                         final List<String> errors) {
        Map<String, String> attributes = element.getProperties();

        for (Map.Entry<String, String> attr : attributes.entrySet()) {
            String attrName = attr.getKey();
            String attrValue = attr.getValue();

            if (!isExcludedByAncestorTag(element)
                    && !EXCLUDED_ATTRIBUTES.contains(attrName)
                    && !excludedAttrs.contains(attrName)
                    && attrValue.contains(HYPHEN)) {
                Object[] messageArguments = {
                        attrName,
                        element.getName(),
                        attrValue
                };
                String errorMessage = getErrorMessage(
                        getName(),
                        changeLogFormat,
                        messageArguments);
                errors.add(errorMessage);
            }
        }

        List<ChangeSetElement> children = element.getChildren();
        for (ChangeSetElement child : children) {
            boolean isElementExcluded = isExcludedByAncestorTag(child);
            if (isElementExcluded) {
                continue;
            }
            validateElement(child, changeLogFormat, errors);
        }

        return errors;
    }
}
