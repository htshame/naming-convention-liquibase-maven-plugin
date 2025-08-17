package io.github.htshame.rule.processor.changeset;

import io.github.htshame.change.element.ChangeLogElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.parser.rule.ChangeSetRule;
import io.github.htshame.util.ErrorMessageUtil;
import io.github.htshame.util.RuleUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.htshame.util.RuleUtil.EXCLUDED_ATTRIBUTES;
import static io.github.htshame.util.RuleUtil.isExcludedByAncestorTag;
import static io.github.htshame.util.RuleUtil.shouldCollectValuesRuleListFormat;

/**
 * Business logic for the <code>no-underscores-in-attributes</code> rule.
 * <p>
 * Checks that the changeSet attributes do not contain underscores.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="no-underscores-in-attributes"&gt;
 *     &lt;excludedAttrs&gt;
 *         &lt;attr&gt;defaultValue&lt;/attr&gt;
 *         &lt;attr&gt;defaultValueComputed&lt;/attr&gt;
 *     &lt;/excludedAttrs&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that there are no underscores in attributes, excluding attributes specified in
 * <code>excludedAttrs</code>.</p>
 */
public class NoUnderscoresInAttributesProcessor implements ChangeSetRule {

    private static final String UNDERSCORE = "_";

    private final Set<String> excludedAttrs;

    /**
     * Constructor.
     *
     * @param excludedAttrs - excluded attributes.
     */
    public NoUnderscoresInAttributesProcessor(final Set<String> excludedAttrs) {
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
    public void validateChangeSet(final ChangeLogElement changeSetElement,
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
        return RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link NoUnderscoresInAttributesProcessor}.
     */
    public static NoUnderscoresInAttributesProcessor instantiate(final Element element) {
        NodeList excludedTags = element
                .getElementsByTagName(RuleStructureEnum.EXCLUDED_ATTRS.getValue());
        Set<String> excludedParents = new HashSet<>();
        if (shouldCollectValuesRuleListFormat(excludedTags, RuleStructureEnum.EXCLUDED_ATTRS)) {
            NodeList excludedAttrElements = ((Element) excludedTags.item(0))
                    .getElementsByTagName(RuleStructureEnum.ATTR.getValue());
            for (int j = 0; j < excludedAttrElements.getLength(); j++) {
                excludedParents.add(excludedAttrElements.item(j).getTextContent());
            }
        }
        return new NoUnderscoresInAttributesProcessor(excludedParents);
    }

    /**
     * Validate element.
     *
     * @param element         - element.
     * @param changeLogFormat - changeLog format.
     * @param errors          - list of errors.
     * @return list of errors.
     */
    private List<String> validateElement(final ChangeLogElement element,
                                         final ChangeLogFormatEnum changeLogFormat,
                                         final List<String> errors) {
        Map<String, String> attributes = element.getProperties();
        for (Map.Entry<String, String> attr : attributes.entrySet()) {
            String attrName = attr.getKey();
            String attrValue = attr.getValue();

            if (!isExcludedByAncestorTag(element)
                    && !EXCLUDED_ATTRIBUTES.contains(attrName)
                    && !excludedAttrs.contains(attrName)
                    && attrValue.contains(UNDERSCORE)) {
                Object[] messageArguments = {
                        attrName,
                        element.getName(),
                        attrValue
                };
                String errorMessage = ErrorMessageUtil.getChangeSetErrorMessage(
                        getName(),
                        changeLogFormat,
                        messageArguments);
                errors.add(errorMessage);
            }
        }

        List<ChangeLogElement> children = element.getChildren();
        for (ChangeLogElement child : children) {
            boolean isExcluded = isExcludedByAncestorTag(child);
            if (isExcluded) {
                continue;
            }
            validateElement(child, changeLogFormat, errors);
        }

        return errors;
    }

}
