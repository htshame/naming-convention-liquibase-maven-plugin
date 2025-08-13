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
 * Business logic for the <code>no-uppercase-in-attributes</code> rule.
 * <p>
 * Checks that the changeSet attributes do not contain uppercase characters.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="no-uppercase-in-attributes"&gt;
 *     &lt;excludedAttrs&gt;
 *         &lt;attr&gt;defaultValue&lt;/attr&gt;
 *         &lt;attr&gt;defaultValueComputed&lt;/attr&gt;
 *     &lt;/excludedAttrs&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that there are no uppercase characters in attributes, excluding attributes specified in
 * <code>excludedAttrs</code>.</p>
 */
public class NoUppercaseInAttributesProcessor implements ChangeSetRule {

    private final Set<String> excludedAttrs;

    /**
     * Constructor.
     *
     * @param excludedAttrs - excluded attributes.
     */
    public NoUppercaseInAttributesProcessor(final Set<String> excludedAttrs) {
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
        return RuleEnum.NO_UPPERCASE_IN_ATTRIBUTES;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link NoUppercaseInAttributesProcessor}.
     */
    public static NoUppercaseInAttributesProcessor instantiate(final Element element) {
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
        return new NoUppercaseInAttributesProcessor(excludedParents);
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
                    && areUppercaseLettersPresent(attrValue)) {
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
            boolean isExcluded = isExcludedByAncestorTag(child);
            if (isExcluded) {
                continue;
            }
            validateElement(child, changeLogFormat, errors);
        }

        return errors;
    }

    /**
     * Are there uppercase chars present.
     *
     * @param attrValue - attribute value.
     * @return <code>true</code> if present, <code>false</code> - if not.
     */
    private boolean areUppercaseLettersPresent(final String attrValue) {
        for (char character : attrValue.toCharArray()) {
            if (Character.isUpperCase(character)) {
                return true;
            }
        }
        return false;
    }

}
