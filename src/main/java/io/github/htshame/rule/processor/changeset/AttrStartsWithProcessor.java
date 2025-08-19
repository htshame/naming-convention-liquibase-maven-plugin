package io.github.htshame.rule.processor.changeset;

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
 * Business logic for the <code>attr-starts-with</code> rule.
 * <p>
 * Checks that the value of a given attribute starts with the specified prefix.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="attr-starts-with"&gt;
 *     &lt;tag&gt;createIndex&lt;/tag&gt;
 *     &lt;targetAttr&gt;indexName&lt;/targetAttr&gt;
 *     &lt;requiredPrefix&gt;idx_&lt;/requiredPrefix&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that the value of <code>indexName</code>:</p>
 * <pre><code>
 * &lt;createIndex indexName="idx_user_metadata_external_user_id"/&gt;
 * </code></pre>
 * indeed starts with <code>idx_</code>.
 */
public class AttrStartsWithProcessor implements ChangeSetRule {

    private final String tag;
    private final String targetAttr;
    private final String requiredPrefix;

    /**
     * Constructor.
     *
     * @param tag            - rule.tag value.
     * @param targetAttr     - rule.targetAttr value.
     * @param requiredPrefix - rule.requiredPrefix value.
     */
    public AttrStartsWithProcessor(final String tag,
                                   final String targetAttr,
                                   final String requiredPrefix) {
        this.tag = Objects.requireNonNull(
                tag, validationErrorMessage(getName(), RuleStructureEnum.TAG));
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
        return RuleEnum.ATTRIBUTE_STARTS_WITH;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link AttrStartsWithProcessor}.
     */
    public static AttrStartsWithProcessor instantiate(final Element element) {
        return new AttrStartsWithProcessor(
                getText(element, RuleStructureEnum.TAG.getValue()),
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
            boolean isTargetAttrPresent = targetTagElement.hasProperty(targetAttr);
            if (!isTargetAttrPresent) {
                continue;
            }
            String targetAttrActualValue = targetTagElement.getPropertyValue(targetAttr);
            if (!targetAttrActualValue.startsWith(requiredPrefix)) {
                Object[] messageArguments = {
                        tag,
                        targetAttr,
                        targetAttrActualValue,
                        requiredPrefix
                };
                String error = ErrorMessageUtil.getChangeSetErrorMessage(
                        getName(),
                        changeLogFormat,
                        messageArguments
                );
                errors.add(error);
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(RuleUtil.composeErrorMessage(changeSetElement, getName(), errors));
        }
    }

}
