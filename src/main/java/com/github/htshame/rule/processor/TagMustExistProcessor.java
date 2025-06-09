package com.github.htshame.rule.processor;

import com.github.htshame.dto.ChangeSetAttributeDto;
import com.github.htshame.enums.RuleEnum;
import com.github.htshame.enums.RuleStructureEnum;
import com.github.htshame.exception.ValidationException;
import com.github.htshame.rule.Rule;
import com.github.htshame.util.ChangeSetUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashSet;
import java.util.Set;

import static com.github.htshame.util.RuleUtil.getText;

/**
 * Business logic for <code>tag-must-exist</code> rule.
 * <p>
 * Checks that each changeSet contains the provided tag.
 * <p>
 * E.g.:
 * <p>
 * Rule configuration:
 * <pre><code>
 *     <rule name="tag-must-exist">
 *          <requiredTag>comment</requiredTag>
 *          <excludedAncestorTags>
 *              <tag>databaseChangeLog</tag>
 *              <tag>include</tag>
 *          </excludedAncestorTags>
 *      </rule>
 * </code></pre>
 * will verify that changeSet
 * <pre><code>
 *     <changeSet id="changelog_02-4" author="test">
 *         <comment>Very informative comment.</comment>
 *     </changeSet>
 * </code></pre>
 * contains <code>comment</code>.
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
                .getElementsByTagName(RuleStructureEnum.EXCLUDED_ANCESTOR_TAGS.getValue()).item(0))
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
