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
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.github.htshame.util.ErrorMessageUtil.getErrorMessage;
import static io.github.htshame.util.ErrorMessageUtil.validationErrorMessage;

/**
 * Business logic for the <code>attr-must-exist-in-tag</code> rule.
 * <p>
 * Checks that the specified tag contains the required attribute.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="attr-must-exist-in-tag"&gt;
 *     &lt;tag&gt;createTable&lt;/tag&gt;
 *     &lt;requiredAttr&gt;remarks&lt;/requiredAttr&gt;
 * &lt;/rule&gt;
 * </code></pre>
 * <p>This will verify that attribute provided in <code>requiredAttr</code>
 * is present in tag provided in <code>tag</code>.
 */
public class AttrMustExistInTagProcessor implements ChangeSetRule {

    private final String tag;
    private final String requiredAttribute;

    /**
     * Constructor.
     *
     * @param tag               - rule.tag value.
     * @param requiredAttribute - rule.requiredAttr value.
     */
    public AttrMustExistInTagProcessor(final String tag,
                                       final String requiredAttribute) {
        this.tag = Objects.requireNonNull(
                tag, validationErrorMessage(getName(), RuleStructureEnum.TAG));
        this.requiredAttribute = Objects.requireNonNull(
                requiredAttribute, validationErrorMessage(getName(), RuleStructureEnum.REQUIRED_ATTR));
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.ATTRIBUTE_MUST_EXIST_IN_TAG;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link AttrStartsWithProcessor}.
     */
    public static AttrMustExistInTagProcessor instantiate(final Element element) {
        String tag = getText(element, RuleStructureEnum.TAG.getValue());
        String requiredAttr = getText(element, RuleStructureEnum.REQUIRED_ATTR.getValue());
        return new AttrMustExistInTagProcessor(tag, requiredAttr);
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

        List<String> errors = new ArrayList<>();
        validateElement(changeSetElement, changeLogFormat, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(RuleUtil.composeErrorMessage(changeSetElement, getName(), errors));
        }
    }

    private void validateElement(final ChangeSetElement element,
                                 final ChangeLogFormatEnum changeLogFormat,
                                 final List<String> errors) {
        if (tag.equals(element.getName())) {
            Map<String, String> attributes = element.getProperties();
            if (!attributes.containsKey(requiredAttribute)
                    || (attributes.containsKey(requiredAttribute) && attributes.get(requiredAttribute).isBlank())) {
                Object[] messageArguments = {
                        element.getName(),
                        requiredAttribute
                };
                errors.add(
                        getErrorMessage(
                                getName(),
                                changeLogFormat,
                                messageArguments));
            }
        }

        for (ChangeSetElement child : element.getChildren()) {
            validateElement(child, changeLogFormat, errors);
        }
    }

    private static String getText(final Element parent,
                                  final String childName) {
        NodeList list = parent.getElementsByTagName(childName);
        if (list.getLength() == 0) {
            return null;
        }
        return list.item(0).getTextContent().trim();
    }
}
