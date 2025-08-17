package io.github.htshame.rule.processor;

import io.github.htshame.change.element.ChangeLogElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.parser.rule.ChangeSetRule;
import io.github.htshame.util.ErrorMessageUtil;
import io.github.htshame.util.RuleUtil;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.htshame.util.ErrorMessageUtil.validationErrorMessage;
import static io.github.htshame.util.RuleUtil.getText;

/**
 * Business logic for the <code>attr-not-ends-with-conditioned</code> rule.
 * <p>
 * Checks that the value of a given attribute does not end with the specified suffix
 * if the specified condition is met.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="attr-not-ends-with-conditioned"&gt;
 *     &lt;tag&gt;createIndex&lt;/tag&gt;
 *     &lt;conditionAttr&gt;unique&lt;/conditionAttr&gt;
 *     &lt;conditionValue&gt;true&lt;/conditionValue&gt;
 *     &lt;targetAttr&gt;indexName&lt;/targetAttr&gt;
 *     &lt;forbiddenSuffix&gt;_unique&lt;/forbiddenSuffix&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that the value of <code>indexName</code>:</p>
 * <pre><code>
 * &lt;createIndex tableName="user_metadata" indexName="idx_unique_user_metadata_external_user_id" unique="true"&gt;
 *     &lt;column name="external_user_id"/&gt;
 * &lt;/createIndex&gt;
 * </code></pre>
 * indeed does not end with <code>_unique</code>.
 */
public class AttrNotEndsWithConditionedProcessor implements ChangeSetRule {

    private final String tag;
    private final String conditionAttr;
    private final String conditionValue;
    private final String targetAttr;
    private final String forbiddenSuffix;

    /**
     * Constructor.
     *
     * @param tag             - rule.tag value.
     * @param conditionAttr   - rule.conditionAttr value.
     * @param conditionValue  - rule.conditionValue value.
     * @param targetAttr      - rule.targetAttr value.
     * @param forbiddenSuffix - rule.forbiddenSuffix value.
     */
    public AttrNotEndsWithConditionedProcessor(final String tag,
                                               final String conditionAttr,
                                               final String conditionValue,
                                               final String targetAttr,
                                               final String forbiddenSuffix) {
        this.tag = Objects.requireNonNull(
                tag, validationErrorMessage(getName(), RuleStructureEnum.TAG));
        this.conditionAttr = Objects.requireNonNull(
                conditionAttr, validationErrorMessage(getName(), RuleStructureEnum.CONDITION_ATTR));
        this.conditionValue = Objects.requireNonNull(
                conditionValue, validationErrorMessage(getName(), RuleStructureEnum.CONDITION_VALUE));
        this.targetAttr = Objects.requireNonNull(
                targetAttr, validationErrorMessage(getName(), RuleStructureEnum.TARGET_ATTR));
        this.forbiddenSuffix = Objects.requireNonNull(
                forbiddenSuffix, validationErrorMessage(getName(), RuleStructureEnum.FORBIDDEN_SUFFIX));
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.ATTRIBUTE_NOT_ENDS_WITH_CONDITIONED;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link AttrNotEndsWithConditionedProcessor}.
     */
    public static AttrNotEndsWithConditionedProcessor instantiate(final Element element) {
        return new AttrNotEndsWithConditionedProcessor(
                getText(element, RuleStructureEnum.TAG.getValue()),
                getText(element, RuleStructureEnum.CONDITION_ATTR.getValue()),
                getText(element, RuleStructureEnum.CONDITION_VALUE.getValue()),
                getText(element, RuleStructureEnum.TARGET_ATTR.getValue()),
                getText(element, RuleStructureEnum.FORBIDDEN_SUFFIX.getValue()));
    }

    /**
     * Validate changeSet.
     *
     * @param changeSetElement  - changeSet element.
     * @param exclusionParser   - exclusion parser.
     * @param changeLogFileName - changeLog file name.
     * @param changeLogFormat   - changeLog format.
     * @throws ValidationException - thrown if validation fails.
     */
    @Override
    public void validateChangeSet(final ChangeLogElement changeSetElement,
                                  final ExclusionParser exclusionParser,
                                  final String changeLogFileName,
                                  final ChangeLogFormatEnum changeLogFormat) throws ValidationException {
        if (RuleUtil.shouldSkipProcessingRule(changeSetElement, exclusionParser, changeLogFileName, getName())) {
            return;
        }
        List<ChangeLogElement> targetTagElementList = changeSetElement.findElementsByName(changeSetElement, tag);
        List<String> errors = new ArrayList<>();

        for (ChangeLogElement targetTagElement : targetTagElementList) {
            if (!conditionValue.equals(targetTagElement.getPropertyValue(conditionAttr))) {
                continue;
            }
            boolean isTargetAttrPresent = targetTagElement.hasProperty(targetAttr);
            if (isTargetAttrPresent) {
                String targetAttrActualValue = targetTagElement.getPropertyValue(targetAttr);
                if (!targetAttrActualValue.endsWith(forbiddenSuffix)) {
                    continue;
                }
                Object[] messageArguments = {
                        tag,
                        conditionAttr,
                        conditionValue,
                        targetAttr,
                        forbiddenSuffix,
                        targetAttrActualValue
                };
                String errorMessage = ErrorMessageUtil.getChangeSetErrorMessage(
                        getName(),
                        changeLogFormat,
                        messageArguments);
                errors.add(errorMessage);
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(RuleUtil.composeErrorMessage(changeSetElement, getName(), errors));
        }
    }
}
