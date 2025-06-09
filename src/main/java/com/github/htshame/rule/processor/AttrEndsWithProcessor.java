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

import static com.github.htshame.util.RuleUtil.getText;

/**
 * Business logic for <code>attr-ends-with</code> rule.
 * <p>
 * Checks that the value of given attribute ends with given prefix.
 * <p>
 * E.g.:
 * <p>
 * Rule configuration:
 * <pre><code>
 *     <rule name="attr-ends-with-conditioned">
 *          <tag>addForeignKeyConstraint</tag>
 *          <targetAttribute>constraintName</targetAttribute>
 *          <requiredSuffix>_fk</requiredSuffix>
 *      </rule>
 * </code></pre>
 * will verify that value of <code>constraintName</code>
 * <pre><code>
 *      <addForeignKeyConstraint baseTableName="user_activation" baseColumnNames="user_profile_id"
 *                                  constraintName="user_activation_user_profile_id_user_profile_id_fk"
 *                                  referencedTableName="user_profile" referencedColumnNames="id"/>
 * </code></pre>
 * indeed ends with <code>_fk</code>.
 */
public class AttrEndsWithProcessor implements Rule {

    private final String tag;
    private final String targetAttribute;
    private final String requiredSuffix;

    /**
     * Constructor.
     *
     * @param tag             - rule.tag value.
     * @param targetAttribute - rule.targetAttribute value.
     * @param requiredSuffix  - rule.requiredSuffix value.
     */
    public AttrEndsWithProcessor(final String tag,
                                 final String targetAttribute,
                                 final String requiredSuffix) {
        this.tag = tag;
        this.targetAttribute = targetAttribute;
        this.requiredSuffix = requiredSuffix;
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.ATTRIBUTE_ENDS_WITH;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link AttrEndsWithProcessor}.
     */
    public static AttrEndsWithProcessor fromXml(final Element element) {
        return new AttrEndsWithProcessor(
                getText(element, RuleStructureEnum.TAG_TAG.getValue()),
                getText(element, RuleStructureEnum.TARGET_ATTRIBUTE_TAG.getValue()),
                getText(element, RuleStructureEnum.REQUIRED_SUFFIX_TAG.getValue()));
    }

    /**
     * Validate changeLog file.
     *
     * @param document - document.
     * @throws ValidationException - thrown if validation fails.
     */
    @Override
    public void validate(final Document document) throws ValidationException {
        NodeList elements = document.getElementsByTagName(tag);
        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            String actualValue = element.getAttribute(targetAttribute);
            if (!actualValue.endsWith(requiredSuffix)) {
                ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(element);
                String errorMessage = String.format(
                        "ChangeSet: id=\"%s\", author=\"%s\". Tag <%s> must have %s ending with "
                                + "[%s], but found: [%s]",
                        changeSetAttributeDto.getId(),
                        changeSetAttributeDto.getAuthor(),
                        tag,
                        targetAttribute,
                        requiredSuffix,
                        actualValue);
                throw new ValidationException(errorMessage);
            }
        }
    }
}
