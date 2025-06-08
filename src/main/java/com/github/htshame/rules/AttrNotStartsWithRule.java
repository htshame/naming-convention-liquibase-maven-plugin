package com.github.htshame.rules;

import com.github.htshame.dto.ChangeSetAttributeDto;
import com.github.htshame.enums.RuleEnum;
import com.github.htshame.exception.ValidationException;
import com.github.htshame.parser.RuleParser;
import com.github.htshame.util.ChangeSetUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.github.htshame.parser.RuleParser.getText;

public class AttrNotStartsWithRule implements Rule {

    private final String tag;
    private final String attribute;
    private final String prefix;

    public AttrNotStartsWithRule(String tag, String attribute, String prefix) {
        this.tag = tag;
        this.attribute = attribute;
        this.prefix = prefix;
    }

    @Override
    public RuleEnum getName() {
        return RuleEnum.ATTRIBUTE_NOT_STARTS_WITH;
    }

    public static AttrNotStartsWithRule fromXml(Element element) {
        return new AttrNotStartsWithRule(
                getText(element, RuleParser.RuleStructureEnum.TAG_TAG.getValue()),
                getText(element, RuleParser.RuleStructureEnum.TARGET_ATTRIBUTE_TAG.getValue()),
                getText(element, RuleParser.RuleStructureEnum.REQUIRED_PREFIX_TAG.getValue()));
    }

    @Override
    public void validate(Document doc, File file) throws ValidationException {
        NodeList nodes = doc.getElementsByTagName(tag);
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Element elem = (Element) nodes.item(i);
            String attrValue = elem.getAttribute(attribute);
            if (elem.hasAttribute(attribute) && !attrValue.startsWith(prefix)) {
                ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(elem);
                String error = String.format(
                        "ChangeSet: id=\"%s\", author=\"%s\". <%s %s=\"%s\"> must start with \"%s\"",
                        changeSetAttributeDto.getId(),
                        changeSetAttributeDto.getAuthor(),
                        tag,
                        attribute,
                        attrValue,
                        prefix
                );
                errors.add(error);
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }

}
