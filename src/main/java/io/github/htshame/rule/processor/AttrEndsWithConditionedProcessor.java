package io.github.htshame.rule.processor;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.Rule;
import io.github.htshame.rule.RuleHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static io.github.htshame.util.RuleUtil.getText;

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
 *     &lt;conditionAttr&gt;unique&lt;/conditionAttr&gt;
 *     &lt;conditionValue&gt;true&lt;/conditionValue&gt;
 *     &lt;targetAttr&gt;indexName&lt;/targetAttr&gt;
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

public class AttrEndsWithConditionedProcessor extends RuleHelper implements Rule {

    private final String tag;
    private final String conditionAttr;
    private final String conditionValue;
    private final String targetAttr;
    private final String requiredSuffix;

    /**
     * Constructor.
     *
     * @param tag            - rule.tag value.
     * @param conditionAttr  - rule.conditionAttr value.
     * @param conditionValue - rule.conditionValue value.
     * @param targetAttr     - rule.targetAttr value.
     * @param requiredSuffix - rule.requiredSuffix value.
     */
    public AttrEndsWithConditionedProcessor(final String tag,
                                            final String conditionAttr,
                                            final String conditionValue,
                                            final String targetAttr,
                                            final String requiredSuffix) {
        this.tag = tag;
        this.conditionAttr = conditionAttr;
        this.conditionValue = conditionValue;
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
                getText(element, RuleStructureEnum.TAG.getValue()),
                getText(element, RuleStructureEnum.CONDITION_ATTR.getValue()),
                getText(element, RuleStructureEnum.CONDITION_VALUE.getValue()),
                getText(element, RuleStructureEnum.TARGET_ATTR.getValue()),
                getText(element, RuleStructureEnum.REQUIRED_SUFFIX.getValue()));
    }

    /**
     * Validate changeSet.
     *
     * @param changeSetElement  - changeSet element.
     * @param exclusionParser   - exclusion parser.
     * @param changeLogFileName - changeLog file name.
     * @throws ValidationException - thrown if validation fails.
     */
    @Override
    public void validate(final Element changeSetElement,
                         final ExclusionParser exclusionParser,
                         final String changeLogFileName) throws ValidationException {
        if (shouldSkipProcessingRule(changeSetElement, exclusionParser, changeLogFileName, getName())) {
            return;
        }
        NodeList elements = changeSetElement.getElementsByTagName(tag);
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            if (!conditionValue.equals(element.getAttribute(conditionAttr))) {
                continue;
            }
            boolean isTargetAttrPresent = element.hasAttribute(targetAttr);
            if (isTargetAttrPresent) {
                String actualValue = element.getAttribute(targetAttr);
                if (actualValue.endsWith(requiredSuffix)) {
                    continue;
                }
                String errorMessage = String.format(
                        "Tag <%s> with %s=\"%s\" must have %s ending with [%s], but found: [%s]",
                        tag,
                        conditionAttr,
                        conditionValue,
                        targetAttr,
                        requiredSuffix,
                        actualValue);
                errors.add(errorMessage);
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(composeErrorMessage(changeSetElement, getName(), errors));
        }
    }
}
