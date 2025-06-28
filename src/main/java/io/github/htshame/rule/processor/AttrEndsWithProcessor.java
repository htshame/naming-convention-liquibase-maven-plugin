package io.github.htshame.rule.processor;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.rule.Rule;
import io.github.htshame.util.RuleUtil;
import io.github.htshame.util.parser.ExclusionParser;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

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
    public static AttrEndsWithProcessor instantiate(final Element element) {
        return new AttrEndsWithProcessor(
                getText(element, RuleStructureEnum.TAG.getValue()),
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
    public void validate(final ChangeSetElement changeSetElement,
                         final ExclusionParser exclusionParser,
                         final String changeLogFileName) throws ValidationException {
        if (RuleUtil.shouldSkipProcessingRule(changeSetElement, exclusionParser, changeLogFileName, getName())) {
            return;
        }
        List<ChangeSetElement> targetTagElementList = changeSetElement.findElementsByName(changeSetElement, tag);
        List<String> errors = new ArrayList<>();

        for (ChangeSetElement targetTagElement : targetTagElementList) {
            boolean isTargetAttrPresent = targetTagElement.hasProperty(targetAttr);
            if (isTargetAttrPresent) {
                String targetAttrActualValue = targetTagElement.getPropertyValue(targetAttr);
                if (!targetAttrActualValue.endsWith(requiredSuffix)) {
                    String errorMessage = String.format(
                            "Element [%s] must have [%s] ending with [%s], but found: [%s]",
                            tag,
                            targetAttr,
                            requiredSuffix,
                            targetAttrActualValue);
                    errors.add(errorMessage);
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(RuleUtil.composeErrorMessage(changeSetElement, getName(), errors));
        }
    }
}
