package com.github.htshame.rules;

import com.github.htshame.dto.ChangeSetAttributeDto;
import com.github.htshame.enums.RuleEnum;
import com.github.htshame.exception.ValidationException;
import com.github.htshame.util.ChangeSetUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class NoHyphensInAttributesRule implements Rule {
    private static final List<String> EXCLUDED_ATTRIBUTES = Arrays.asList("id", "author");
    private static final String HYPHEN = "-";

    @Override
    public void validate(Document doc, File file) throws ValidationException {
        validateElement(doc.getDocumentElement());
    }

    @Override
    public RuleEnum getName() {
        return RuleEnum.NO_HYPHENS_IN_ATTRIBUTES;
    }

    public static NoHyphensInAttributesRule fromXml(Element element) {
        return new NoHyphensInAttributesRule();
    }

    private void validateElement(Element element) throws ValidationException {
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attr = (Attr) attributes.item(i);
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (!EXCLUDED_ATTRIBUTES.contains(attrName) && attrValue.contains(HYPHEN)) {
                ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(element);
                String errorMessage = String.format(
                        "ChangeSet: id=\"%s\", author=\"%s\". Attribute %s in element <%s> contains hyphen in value: [%s]. Rule: %s",
                        changeSetAttributeDto.getId(),
                        changeSetAttributeDto.getAuthor(),
                        attrName,
                        element.getTagName(),
                        attrValue,
                        RuleEnum.NO_HYPHENS_IN_ATTRIBUTES.getValue());
                throw new ValidationException(errorMessage);
            }
        }

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                validateElement((Element) child);
            }
        }
    }
}
