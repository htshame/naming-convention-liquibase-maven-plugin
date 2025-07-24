package io.github.htshame.rule.processor;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.parser.rule.ChangeSetRule;
import io.github.htshame.util.RuleUtil;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.htshame.util.ErrorMessageUtil.getErrorMessage;
import static io.github.htshame.util.ErrorMessageUtil.validationErrorMessage;
import static io.github.htshame.util.RuleUtil.getText;

/**
 * Business logic for the <code>attr-starts-with-conditioned</code> rule.
 * <p>
 * Checks that the value of a given attribute starts with the specified prefix
 * if the specified condition is met.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="attr-starts-with-conditioned"&gt;
 *     &lt;tag&gt;createIndex&lt;/tag&gt;
 *     &lt;conditionAttr&gt;unique&lt;/conditionAttr&gt;
 *     &lt;conditionValue&gt;true&lt;/conditionValue&gt;
 *     &lt;targetAttr&gt;indexName&lt;/targetAttr&gt;
 *     &lt;requiredPrefix&gt;idx_unique_&lt;/requiredPrefix&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that the value of <code>indexName</code>:</p>
 * <pre><code>
 * &lt;createIndex tableName="user_metadata" indexName="idx_unique_user_metadata_external_user_id" unique="true"&gt;
 *     &lt;column name="external_user_id"/&gt;
 * &lt;/createIndex&gt;
 * </code></pre>
 * indeed starts with <code>idx_unique_</code>.
 */
public class AttrStartsWithConditionedProcessor implements ChangeSetRule {

    private final String tag;
    private final String conditionAttr;
    private final String conditionValue;
    private final String targetAttr;
    private final String requiredPrefix;

    /**
     * Constructor.
     *
     * @param tag            - rule.tag value.
     * @param conditionAttr  - rule.conditionAttr value.
     * @param conditionValue - rule.conditionValue value.
     * @param targetAttr     - rule.targetAttr value.
     * @param requiredPrefix - rule.requiredPrefix value.
     */
    public AttrStartsWithConditionedProcessor(final String tag,
                                              final String conditionAttr,
                                              final String conditionValue,
                                              final String targetAttr,
                                              final String requiredPrefix) {
        this.tag = Objects.requireNonNull(
                tag, validationErrorMessage(getName(), RuleStructureEnum.TAG));
        this.conditionAttr = Objects.requireNonNull(
                conditionAttr, validationErrorMessage(getName(), RuleStructureEnum.CONDITION_ATTR));
        this.conditionValue = Objects.requireNonNull(
                conditionValue, validationErrorMessage(getName(), RuleStructureEnum.CONDITION_VALUE));
        this.targetAttr = Objects.requireNonNull(
                targetAttr, validationErrorMessage(getName(), RuleStructureEnum.TARGET_ATTR));
        this.requiredPrefix = Objects.requireNonNull(
                requiredPrefix, validationErrorMessage(getName(), RuleStructureEnum.REQUIRED_PREFIX));
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.ATTRIBUTE_STARTS_WITH_CONDITIONED;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link AttrStartsWithConditionedProcessor}.
     */
    public static AttrStartsWithConditionedProcessor instantiate(final Element element) {
        return new AttrStartsWithConditionedProcessor(
                getText(element, RuleStructureEnum.TAG.getValue()),
                getText(element, RuleStructureEnum.CONDITION_ATTR.getValue()),
                getText(element, RuleStructureEnum.CONDITION_VALUE.getValue()),
                getText(element, RuleStructureEnum.TARGET_ATTR.getValue()),
                getText(element, RuleStructureEnum.REQUIRED_PREFIX.getValue()));
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
    public void validateChangeSet(final ChangeSetElement changeSetElement,
                                  final ExclusionParser exclusionParser,
                                  final String changeLogFileName,
                                  final ChangeLogFormatEnum changeLogFormat) throws ValidationException {
        if (RuleUtil.shouldSkipProcessingRule(changeSetElement, exclusionParser, changeLogFileName, getName())) {
            return;
        }
        List<ChangeSetElement> targetTagElementList = changeSetElement.findElementsByName(changeSetElement, tag);
        List<String> errors = new ArrayList<>();

        for (ChangeSetElement targetTagElement : targetTagElementList) {
            if (!conditionValue.equals(targetTagElement.getPropertyValue(conditionAttr))) {
                continue;
            }
            boolean isTargetAttrPresent = targetTagElement.hasProperty(targetAttr);
            if (isTargetAttrPresent) {
                String targetAttrActualValue = targetTagElement.getPropertyValue(targetAttr);
                if (targetAttrActualValue.startsWith(requiredPrefix)) {
                    continue;
                }
                Object[] messageArguments = {
                        tag,
                        conditionAttr,
                        conditionValue,
                        targetAttr,
                        requiredPrefix,
                        targetAttrActualValue
                };
                String errorMessage = getErrorMessage(
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
