package io.github.htshame.rule.processor;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.ChangeSetRule;
import io.github.htshame.util.RuleUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static io.github.htshame.util.ChangeSetUtil.CHANGE_SET_TAG_NAME;
import static io.github.htshame.util.ErrorMessageUtil.getChangeSetError;
import static io.github.htshame.util.ErrorMessageUtil.validationErrorMessage;
import static io.github.htshame.util.RuleUtil.shouldCollectValuesRuleListFormat;

/**
 * Business logic for the <code>tag-must-exist</code> rule.
 * <p>
 * Checks that each <code>changeSet</code> contains the specified required tag.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="tag-must-exist"&gt;
 *     &lt;requiredTag&gt;comment&lt;/requiredTag&gt;
 *     &lt;requiredForChildTags&gt;
 *         &lt;tag&gt;rollback&lt;/tag&gt;
 *     &lt;/requiredForChildTags&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that the following <code>changeSet</code>:</p>
 * <pre><code>
 * &lt;changeSet id="changelog_02-4" author="test"&gt;
 *     &lt;comment&gt;Very informative comment.&lt;/comment&gt;
 * &lt;/changeSet&gt;
 * </code></pre>
 * contains the <code>comment</code> tag.
 * <br>
 * This rule will also be applied to child tags, provided in <code>requiredForChildTags</code>.
 */
public class TagMustExistProcessor implements ChangeSetRule {

    private final String requiredTag;
    private final Set<String> requiredForChildTags;

    /**
     * Constructor.
     *
     * @param requiredTag          - rule.requiredTag value.
     * @param requiredForChildTags - set of tags to search in for <code>requiredTag</code>.
     */
    public TagMustExistProcessor(final String requiredTag,
                                 final Set<String> requiredForChildTags) {
        this.requiredTag = Objects.requireNonNull(
                requiredTag, validationErrorMessage(getName(), RuleStructureEnum.REQUIRED_TAG));
        this.requiredForChildTags = requiredForChildTags;
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.TAG_MUST_EXIST;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link TagMustExistProcessor}.
     */
    public static TagMustExistProcessor instantiate(final Element element) {
        NodeList requiredChildTags = element.getElementsByTagName(
                RuleStructureEnum.REQUIRED_FOR_CHILD_TAGS.getValue());
        Set<String> requiredForChildTags = new HashSet<>();
        if (shouldCollectValuesRuleListFormat(requiredChildTags, RuleStructureEnum.REQUIRED_FOR_CHILD_TAGS)) {
            NodeList excludedTagElements = ((Element) requiredChildTags.item(0))
                    .getElementsByTagName(RuleStructureEnum.TAG.getValue());
            for (int j = 0; j < excludedTagElements.getLength(); j++) {
                requiredForChildTags.add(excludedTagElements.item(j).getTextContent());
            }
        }
        String requiredTag = RuleUtil.getText(element, RuleStructureEnum.REQUIRED_TAG.getValue());
        return new TagMustExistProcessor(requiredTag, requiredForChildTags);
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
     * Traverse the contents of the document.
     *
     * @param element         - element.
     * @param changeLogFormat - changeLog format.
     * @param errors          - list of errors.
     * @return list of errors.
     */
    private List<String> validateElement(final ChangeSetElement element,
                                         final ChangeLogFormatEnum changeLogFormat,
                                         final List<String> errors) {
        String tagName = element.getName();
        boolean isSearchInChildTagRequired = requiredForChildTags.contains(tagName);
        boolean hasRequiredChild = hasRequiredChild(element);

        if (shouldAddError(hasRequiredChild, element, tagName, isSearchInChildTagRequired)) {
            String error = String.format(
                    getChangeSetError(getName(), changeLogFormat),
                    tagName,
                    requiredTag);
            errors.add(error);
        }

        List<ChangeSetElement> children = element.getChildren();
        for (ChangeSetElement node : children) {
            validateElement(node, changeLogFormat, errors);
        }
        return errors;
    }

    /**
     * Should add the error.
     *
     * @param hasRequiredChild           - has required child flag.
     * @param element                    - changeSet element.
     * @param tagName                    - tag name.
     * @param isSearchInChildTagRequired - is search in child tag required.
     * @return <code>true</code> if error should be added, <code>false</code> - if not.
     */
    private boolean shouldAddError(final boolean hasRequiredChild,
                                   final ChangeSetElement element,
                                   final String tagName,
                                   final boolean isSearchInChildTagRequired) {
        return (hasRequiredChild && isErrorPresentInTheChildElement(element))
                || (!hasRequiredChild && (CHANGE_SET_TAG_NAME.equals(tagName) || isSearchInChildTagRequired));
    }

    /**
     * Check if the error is present in the child element.
     *
     * @param element - changeSet element.
     * @return <code>true</code> if present, <code>false</code> - if not.
     */
    private boolean isErrorPresentInTheChildElement(final ChangeSetElement element) {
        for (ChangeSetElement child : element.getChildren()) {
            if (requiredTag.equals(child.getName())) {
                String value = child.getValue();
                if (value == null || value.isBlank()) {
                    return true;
                }
            }
        }

        for (Map.Entry<String, String> entry : element.getProperties().entrySet()) {
            if (requiredTag.equals(entry.getKey())) {
                String value = entry.getValue();
                if (value == null || value.isBlank()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks that element contains the requiredTag.
     *
     * @param element - element.
     * @return <code>true</code> if contains. <code>false</code> - if not.
     */
    private boolean hasRequiredChild(final ChangeSetElement element) {
        List<ChangeSetElement> children = element.getChildren();
        for (ChangeSetElement child : children) {
            if (requiredTag.equals(child.getName())) {
                return true;
            }
        }
        Map<String, String> properties = element.getProperties();
        if (properties != null) {
            for (Map.Entry<String, String> property : properties.entrySet()) {
                if (requiredTag.equals(property.getKey())) {
                    return true;
                }
            }
        }
        return false;
    }
}
