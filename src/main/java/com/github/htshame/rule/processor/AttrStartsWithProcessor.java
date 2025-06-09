package com.github.htshame.rule.processor;

import com.github.htshame.dto.ChangeSetAttributeDto;
import com.github.htshame.enums.RuleEnum;
import com.github.htshame.enums.RuleStructureEnum;
import com.github.htshame.exception.ValidationException;
import com.github.htshame.rule.Rule;
import com.github.htshame.util.ChangeSetUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static com.github.htshame.util.RuleUtil.getText;

/**
 * Business logic for <code>attr-starts-with</code> rule.
 * <p>
 * Checks that the value of given attribute starts with given prefix.
 * <p>
 * E.g.:
 * <p>
 * Rule configuration:
 * <pre><code>
 *    <rule name="attr-starts-with">
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
public class AttrStartsWithProcessor implements Rule {

    private final String tag;
    private final String attribute;
    private final String prefix;

    /**
     * Constructor.
     *
     * @param tag       - rule.tag value.
     * @param attribute - rule.targetAttribute value.
     * @param prefix    - rule.requiredPrefix value.
     */
    public AttrStartsWithProcessor(final String tag,
                                   final String attribute,
                                   final String prefix) {
        this.tag = tag;
        this.attribute = attribute;
        this.prefix = prefix;
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.ATTRIBUTE_STARTS_WITH;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link AttrStartsWithProcessor}.
     */
    public static AttrStartsWithProcessor fromXml(final Element element) {
        return new AttrStartsWithProcessor(
                getText(element, RuleStructureEnum.TAG_TAG.getValue()),
                getText(element, RuleStructureEnum.TARGET_ATTRIBUTE_TAG.getValue()),
                getText(element, RuleStructureEnum.REQUIRED_PREFIX_TAG.getValue()));
    }

    /**
     * Validate changeLog file.
     *
     * @param document - document.
     * @throws ValidationException - thrown if validation fails.
     */
    @Override
    public void validate(final Document document) throws ValidationException {
        NodeList nodes = document.getElementsByTagName(tag);
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            String attrValue = element.getAttribute(attribute);
            if (element.hasAttribute(attribute) && !attrValue.startsWith(prefix)) {
                ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(element);
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
