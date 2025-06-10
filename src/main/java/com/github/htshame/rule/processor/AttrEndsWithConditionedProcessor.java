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
 * Business logic for the <code>attr-ends-with-conditioned</code> rule.
 * <p>
 * Checks that the value of a given attribute ends with the specified suffix
 * if the specified condition is met.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="attr-ends-with-conditioned"&gt;
 *     &lt;tag&gt;createIndex&lt;/tag&gt;
 *     &lt;conditionAttribute&gt;unique&lt;/conditionAttribute&gt;
 *     &lt;conditionValue&gt;true&lt;/conditionValue&gt;
 *     &lt;targetAttribute&gt;indexName&lt;/targetAttribute&gt;
 *     &lt;requiredSuffix&gt;_unique&lt;/requiredSuffix&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that the value of <code>indexName</code>:</p>
 * <pre><code>
 * &lt;createIndex tableName="user_metadata" indexName="idx_user_metadata_external_user_id_unique" unique="true"&gt;
 *     &lt;column name="external_user_id"/&gt;
 * &lt;/createIndex&gt;
 * </code></pre>
 * indeed ends with <code>_unique</code>.
 */

public class AttrEndsWithConditionedProcessor implements Rule {

    private final String tag;
    private final String conditionAttribute;
    private final String conditionValue;
    private final String targetAttribute;
    private final String requiredSuffix;

    /**
     * Constructor.
     *
     * @param tag                - rule.tag value.
     * @param conditionAttribute - rule.conditionAttribute value.
     * @param conditionValue     - rule.conditionValue value.
     * @param targetAttribute    - rule.targetAttribute value.
     * @param requiredSuffix     - rule.requiredSuffix value.
     */
    public AttrEndsWithConditionedProcessor(final String tag,
                                            final String conditionAttribute,
                                            final String conditionValue,
                                            final String targetAttribute,
                                            final String requiredSuffix) {
        this.tag = tag;
        this.conditionAttribute = conditionAttribute;
        this.conditionValue = conditionValue;
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
        return RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link AttrEndsWithConditionedProcessor}.
     */
    public static AttrEndsWithConditionedProcessor fromXml(final Element element) {
        return new AttrEndsWithConditionedProcessor(
                getText(element, RuleStructureEnum.TAG_TAG.getValue()),
                getText(element, RuleStructureEnum.CONDITION_ATTRIBUTE_TAG.getValue()),
                getText(element, RuleStructureEnum.CONDITION_VALUE_TAG.getValue()),
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
