package com.github.htshame.rules;

import com.github.htshame.enums.RuleEnum;
import com.github.htshame.exception.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;

public class AttrNotEndsWithRule implements Rule {

    private final String tag;
    private final String conditionAttribute;
    private final String conditionValue;
    private final String targetAttribute;
    private final String requiredSuffix;

    public AttrNotEndsWithRule(String tag,
                               String conditionAttribute,
                               String conditionValue,
                               String targetAttribute,
                               String requiredSuffix) {
        this.tag = tag;
        this.conditionAttribute = conditionAttribute;
        this.conditionValue = conditionValue;
        this.targetAttribute = targetAttribute;
        this.requiredSuffix = requiredSuffix;
    }

    @Override
    public RuleEnum getName() {
        return RuleEnum.ATTRIBUTE_NOT_ENDS_WITH;
    }

    @Override
    public void validate(Document doc, File file) throws ValidationException {
        NodeList elements = doc.getElementsByTagName(tag);
        for (int i = 0; i < elements.getLength(); i++) {
            Element elem = (Element) elements.item(i);
            if (conditionValue.equals(elem.getAttribute(conditionAttribute))) {
                String actualValue = elem.getAttribute(targetAttribute);
                if (!actualValue.endsWith(requiredSuffix)) {
                    String errorMessage = String.format(
                            "File: %s. Tag <%s> with %s=\"%s\" must have %s ending with [%s], but found: [%s]",
                            file.getName(),
                            tag,
                            conditionAttribute,
                            conditionValue,
                            targetAttribute,
                            requiredSuffix,
                            actualValue);
                    throw new ValidationException(errorMessage);
                }
            }
        }
    }
}
