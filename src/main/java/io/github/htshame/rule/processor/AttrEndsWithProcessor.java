package io.github.htshame.rule.processor;

import io.github.htshame.dto.ChangeSetAttributeDto;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.rule.Rule;
import io.github.htshame.util.ChangeSetUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static io.github.htshame.util.RuleUtil.getText;

/**
 * Business logic for the <code>attr-ends-with</code> rule.
 * <p>
 * Checks that the value of a given attribute ends with the specified suffix.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="attr-ends-with-conditioned"&gt;
 *     &lt;tag&gt;addForeignKeyConstraint&lt;/tag&gt;
 *     &lt;targetAttr&gt;constraintName&lt;/targetAttr&gt;
 *     &lt;requiredSuffix&gt;_fk&lt;/requiredSuffix&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that the value of <code>constraintName</code>:</p>
 * <pre><code>
 * &lt;addForeignKeyConstraint baseTableName="user_activation"
 *                           baseColumnNames="user_profile_id"
 *                           constraintName="user_activation_user_profile_id_user_profile_id_fk"
 *                           referencedTableName="user_profile" referencedColumnNames="id"/&gt;
 * </code></pre>
 * indeed ends with <code>_fk</code>.
 */

public class AttrEndsWithProcessor implements Rule {

    private final String tag;
    private final String targetAttr;
    private final String requiredSuffix;

    /**
     * Constructor.
     *
     * @param tag             - rule.tag value.
     * @param targetAttr - rule.targetAttr value.
     * @param requiredSuffix  - rule.requiredSuffix value.
     */
    public AttrEndsWithProcessor(final String tag,
                                 final String targetAttr,
                                 final String requiredSuffix) {
        this.tag = tag;
        this.targetAttr = targetAttr;
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
                getText(element, RuleStructureEnum.TAG.getValue()),
                getText(element, RuleStructureEnum.TARGET_ATTR.getValue()),
                getText(element, RuleStructureEnum.REQUIRED_SUFFIX.getValue()));
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
            String actualValue = element.getAttribute(targetAttr);
            if (!actualValue.endsWith(requiredSuffix)) {
                ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(element);
                String errorMessage = String.format(
                        "ChangeSet: id=\"%s\", author=\"%s\". Tag <%s> must have %s ending with "
                                + "[%s], but found: [%s]",
                        changeSetAttributeDto.getId(),
                        changeSetAttributeDto.getAuthor(),
                        tag,
                        targetAttr,
                        requiredSuffix,
                        actualValue);
                throw new ValidationException(errorMessage);
            }
        }
    }
}
