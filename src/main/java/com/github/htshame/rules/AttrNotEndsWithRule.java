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

import static com.github.htshame.parser.RuleParser.getText;

/**
 * Business logic for "attr-not-ends-with" rule.
 * <p>
 * Checks that the value of given attribute starts with given prefix.
 * <p>
 * E.g.:
 * <p>
 * Rule configuration:
 * <pre><code>
 *     <rule name="attr-not-starts-with">
 *         <tag>createIndex</tag>
 *         <targetAttribute>indexName</targetAttribute>
 *         <requiredPrefix>idx_</requiredPrefix>
 *     </rule>
 * </code></pre>
 * will verify that value of <code>indexName</code>
 * <pre><code>
 *     <createIndex indexName="idx_user_metadata_external_user_id"/>
 * </code></pre>
 * indeed starts with <code>idx_</code>.
 */
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

    public static AttrNotEndsWithRule fromXml(Element element) {
        return new AttrNotEndsWithRule(
                getText(element, RuleParser.RuleStructureEnum.TAG_TAG.getValue()),
                getText(element, RuleParser.RuleStructureEnum.CONDITION_ATTRIBUTE_TAG.getValue()),
                getText(element, RuleParser.RuleStructureEnum.CONDITION_VALUE_TAG.getValue()),
                getText(element, RuleParser.RuleStructureEnum.TARGET_ATTRIBUTE_TAG.getValue()),
                getText(element, RuleParser.RuleStructureEnum.REQUIRED_SUFFIX_TAG.getValue()));
    }

    @Override
    public void validate(Document doc, File file) throws ValidationException {
        NodeList elements = doc.getElementsByTagName(tag);
        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            if (conditionValue.equals(element.getAttribute(conditionAttribute))) {
                String actualValue = element.getAttribute(targetAttribute);
                if (!actualValue.endsWith(requiredSuffix)) {
                    ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(element);
                    String errorMessage = String.format(
                            "ChangeSet: id=\"%s\", author=\"%s\". Tag <%s> with %s=\"%s\" must have %s ending with "
                                    + "[%s], but found: [%s]",
                            changeSetAttributeDto.getId(),
                            changeSetAttributeDto.getAuthor(),
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
