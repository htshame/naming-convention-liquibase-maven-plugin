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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.htshame.util.ChangeSetUtil.CHANGE_SET_TAG_NAME;

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
 * <br/>
 * This rule will also be applied to child tags, provided in <code>requiredForChildTags</code>.
 */
public class TagMustExistProcessor implements Rule {

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
        this.requiredTag = requiredTag;
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
        String requiredTag = RuleUtil.getText(element, RuleStructureEnum.REQUIRED_TAG.getValue());
        Set<String> requiredForChildTags = new HashSet<>();
        NodeList requiredChildTags = element.getElementsByTagName(RuleStructureEnum.REQUIRED_FOR_CHILD_TAGS.getValue());
        if (requiredChildTags.getLength() != 0) {
            NodeList excludedTagElements = ((Element) requiredChildTags.item(0))
                    .getElementsByTagName(RuleStructureEnum.TAG.getValue());
            for (int j = 0; j < excludedTagElements.getLength(); j++) {
                requiredForChildTags.add(excludedTagElements.item(j).getTextContent());
            }
        }
        return new TagMustExistProcessor(requiredTag, requiredForChildTags);
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
     * Traverse the contents of the document.
     *
     * @param element - element.
     * @param errors  - list of errors.
     * @return list of errors.
     */
    private List<String> validateElement(final ChangeSetElement element,
                                         final List<String> errors) {
        String tagName = element.getName();

        boolean isSearchInChildTagRequired = requiredForChildTags.contains(tagName);

        boolean hasRequiredChild = hasRequiredChild(element);
        if (hasRequiredChild) {
            if (isErrorPresent(element)) {
                String error = String.format(
                        "Element [%s]. Required child element [%s] can not be empty",
                        tagName,
                        requiredTag);
                errors.add(error);
            }

        } else if (CHANGE_SET_TAG_NAME.equals(tagName) || isSearchInChildTagRequired) {
            String errorMessage = String.format(
                    "Element [%s] does not contain required element [%s]",
                    tagName,
                    requiredTag);
            errors.add(errorMessage);
        }

        List<ChangeSetElement> children = element.getChildren();
        for (ChangeSetElement node : children) {
            validateElement(node, errors);
        }
        return errors;
    }

    /**
     * Check if the error is actually present.
     *
     * @param element - changeSet element.
     * @return <code>true</code> if present, <code>false</code> - if not.
     */
    private boolean isErrorPresent(final ChangeSetElement element) {
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
