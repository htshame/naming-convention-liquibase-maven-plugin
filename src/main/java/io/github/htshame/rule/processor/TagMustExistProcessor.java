package io.github.htshame.rule.processor;

import io.github.htshame.dto.ChangeSetAttributeDto;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.rule.Rule;
import io.github.htshame.util.ChangeSetUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashSet;
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
        NodeList excludedTagElements = ((Element) element
                .getElementsByTagName(RuleStructureEnum.EXCLUDED_TAGS.getValue()).item(0))
                .getElementsByTagName(RuleStructureEnum.TAG_TAG.getValue());
        for (int j = 0; j < excludedTagElements.getLength(); j++) {
            excludedParents.add(excludedTagElements.item(j).getTextContent());
        }
        return new TagMustExistProcessor(requiredChild, excludedParents);
    }

    /**
     * Validate changeLog file.
     *
     * @param document - document.
     * @throws ValidationException - thrown if validation fails.
     */
    @Override
    public void validate(final Document document) throws ValidationException {
        Element root = document.getDocumentElement();
        traverse(root);
    }

    /**
     * Traverse the contents of the document.
     *
     * @param element - element.
     * @throws ValidationException - thrown if validation fails.
     */
    private void traverse(final Element element) throws ValidationException {
        String tagName = element.getTagName();

        boolean isExcluded = excludedTags.contains(tagName);

        if (!isExcluded) {
            boolean hasRequiredChild = hasRequiredChild(element);
            if (!hasRequiredChild) {
                ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(element);
                String errorMessage = String.format(
                        "ChangeSet: id=\"%s\", author=\"%s\". Tag <%s> does not contain required tag <%s>",
                        changeSetAttributeDto.getId(),
                        changeSetAttributeDto.getAuthor(),
                        tagName,
                        requiredTag);
                throw new ValidationException(errorMessage);
            }
            return;
        }

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                traverse((Element) node);
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
