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
        String requiredChild = RuleUtil.getText(element, RuleStructureEnum.REQUIRED_TAG.getValue());
        Set<String> requiredForChildTags = new HashSet<>();
        NodeList requiredChildTags = element.getElementsByTagName(RuleStructureEnum.REQUIRED_FOR_CHILD_TAGS.getValue());
        if (requiredChildTags.getLength() != 0) {
            NodeList excludedTagElements = ((Element) requiredChildTags.item(0))
                    .getElementsByTagName(RuleStructureEnum.TAG.getValue());
            for (int j = 0; j < excludedTagElements.getLength(); j++) {
                requiredForChildTags.add(excludedTagElements.item(j).getTextContent());
            }
        }
        return new TagMustExistProcessor(requiredChild, requiredForChildTags);
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
            element.getChildren().stream()
                    .filter(child -> {
                        if (requiredTag.equals(child.getName())) {
                            return child.getValue() != null && child.getValue().isBlank();
                        }
                        return false;
                    }).map(child -> String.format(
                            "Tag <%s>. Required tag <%s> can not be empty",
                            tagName,
                            requiredTag))
                    .forEach(errors::add);
        } else if (CHANGE_SET_TAG_NAME.equals(tagName) || isSearchInChildTagRequired) {
            String errorMessage = String.format(
                    "Tag <%s> does not contain required tag <%s>",
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
     * Checks that element contains the requiredTag.
     *
     * @param element - element.
     * @return <code>true</code> if contains. <code>false</code> - if not.
     */
    private boolean hasRequiredChild(final ChangeSetElement element) {
        List<ChangeSetElement> children = element.getChildren();
        for (ChangeSetElement child : children) {
            if (child.getName().equals(requiredTag)) {
                return true;
            }
        }
        return false;
    }
}
