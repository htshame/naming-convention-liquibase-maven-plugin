package io.github.htshame.rule.processor;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.rule.Rule;
import io.github.htshame.util.RuleUtil;
import io.github.htshame.util.parser.ExclusionParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.htshame.util.RuleUtil.getText;

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
 *     &lt;excludedTags&gt;
 *         &lt;tag&gt;databaseChangeLog&lt;/tag&gt;
 *         &lt;tag&gt;include&lt;/tag&gt;
 *     &lt;/excludedTags&gt;
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
    private final Set<String> excludedTags;

    /**
     * Constructor.
     *
     * @param requiredTag  - rule.requiredTag value.
     * @param excludedTags - set of rule.excludedAncestorLog values.
     */
    public TagMustExistProcessor(final String requiredTag,
                                 final Set<String> excludedTags) {
        this.requiredTag = requiredTag;
        this.excludedTags = excludedTags;
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
    public static TagMustExistProcessor fromXml(final Element element) {
        String requiredChild = getText(element, RuleStructureEnum.REQUIRED_TAG.getValue());
        Set<String> excludedParents = new HashSet<>();
        NodeList excludedTags = element.getElementsByTagName(RuleStructureEnum.EXCLUDED_TAGS.getValue());
        if (excludedTags.getLength() != 0) {
            NodeList excludedTagElements = ((Element) excludedTags.item(0))
                    .getElementsByTagName(RuleStructureEnum.TAG.getValue());
            for (int j = 0; j < excludedTagElements.getLength(); j++) {
                excludedParents.add(excludedTagElements.item(j).getTextContent());
            }
        }
        return new TagMustExistProcessor(requiredChild, excludedParents);
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
    public void validate(final Element changeSetElement,
                         final ExclusionParser exclusionParser,
                         final String changeLogFileName) throws ValidationException {
        if (RuleUtil.shouldSkipProcessingRule(changeSetElement, exclusionParser, changeLogFileName, getName())) {
            return;
        }
        validateElement(changeSetElement);
    }

    /**
     * Traverse the contents of the document.
     *
     * @param element - element.
     * @throws ValidationException - thrown if validation fails.
     */
    private void validateElement(final Element element) throws ValidationException {
        String tagName = element.getTagName();

        boolean isExcluded = excludedTags.contains(tagName);

        if (!isExcluded) {
            boolean hasRequiredChild = hasRequiredChild(element);
            if (!hasRequiredChild) {
                String errorMessage = String.format(
                        "Tag <%s> does not contain required tag <%s>",
                        tagName,
                        requiredTag);
                throw new ValidationException(RuleUtil.composeErrorMessage(element, getName(), List.of(errorMessage)));
            }
            return;
        }

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                validateElement((Element) node);
            }
        }
    }

    /**
     * Checks that element contains the requiredTag.
     *
     * @param element - element.
     * @return <code>true</code> if contains. <code>false</code> - if not.
     */
    private boolean hasRequiredChild(final Element element) {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE
                    && ((Element) child).getTagName().equals(requiredTag)) {
                return true;
            }
        }
        return false;
    }
}
