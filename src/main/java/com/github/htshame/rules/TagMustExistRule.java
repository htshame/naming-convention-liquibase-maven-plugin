package com.github.htshame.rules;

import com.github.htshame.dto.ChangeSetAttributeDto;
import com.github.htshame.enums.RuleEnum;
import com.github.htshame.exception.ValidationException;
import com.github.htshame.parser.RuleParser;
import com.github.htshame.util.ChangeSetUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static com.github.htshame.parser.RuleParser.getText;

public class TagMustExistRule implements Rule {

    private final String requiredTag;
    private final Set<String> excludedTags;

    public TagMustExistRule(String requiredTag, Set<String> excludedTags) {
        this.requiredTag = requiredTag;
        this.excludedTags = excludedTags;
    }

    @Override
    public RuleEnum getName() {
        return RuleEnum.TAG_MUST_EXIST;
    }

    public static TagMustExistRule fromXml(Element element) {
        String requiredChild = getText(element, RuleParser.RuleStructureEnum.REQUIRED_TAG.getValue());
        Set<String> excludedParents = new HashSet<>();
        NodeList excludedTagElements = ((Element) element
                .getElementsByTagName(RuleParser.RuleStructureEnum.EXCLUDED_ANCESTOR_TAGS.getValue()).item(0))
                .getElementsByTagName(RuleParser.RuleStructureEnum.TAG_TAG.getValue());
        for (int j = 0; j < excludedTagElements.getLength(); j++) {
            excludedParents.add(excludedTagElements.item(j).getTextContent());
        }
        return new TagMustExistRule(requiredChild, excludedParents);
    }

    @Override
    public void validate(Document doc, File file) throws ValidationException {
        Element root = doc.getDocumentElement();
        traverse(root, file);
    }

    private void traverse(Element element, File file) throws ValidationException {
        String tagName = element.getTagName();

        // Skip excluded tag itself and don't validate, but continue to children
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

            // Optimization: if it already has the required child, skip deeper checks
            return;
        }

        // Always continue to recurse children, regardless of whether the element is excluded
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                traverse((Element) node, file);
            }
        }
    }

    private boolean hasRequiredChild(Element element) {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE &&
                    ((Element) child).getTagName().equals(requiredTag)) {
                return true;
            }
        }
        return false;
    }
}
