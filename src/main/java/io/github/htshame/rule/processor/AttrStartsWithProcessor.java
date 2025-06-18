package io.github.htshame.rule.processor;

import io.github.htshame.dto.ChangeSetAttributeDto;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.Rule;
import io.github.htshame.rule.RulePreProcessor;
import io.github.htshame.util.ChangeSetUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

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

public class AttrStartsWithProcessor extends RulePreProcessor implements Rule {

    private final String tag;
    private final String attribute;
    private final String prefix;

    /**
     * Constructor.
     *
     * @param tag       - rule.tag value.
     * @param attribute - rule.targetAttr value.
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
     * @throws ValidationException - thrown if validation fails.
     */
    @Override
    public void validate(final Element changeSetElement,
                         final ExclusionParser exclusionParser,
                         final String changeLogFileName) throws ValidationException {
        if (shouldSkipProcessingRule(changeSetElement, exclusionParser, changeLogFileName, getName())) {
            return;
        }
        NodeList nodes = changeSetElement.getElementsByTagName(tag);
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            String attrValue = element.getAttribute(attribute);
            if (element.hasAttribute(attribute) && !attrValue.startsWith(prefix)) {
                ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(changeSetElement);
                String error = String.format(
                        "ChangeSet: id=\"%s\", author=\"%s\". Rule: [%s]. <%s %s=\"%s\"> must start with \"%s\"",
                        changeSetAttributeDto.getId(),
                        changeSetAttributeDto.getAuthor(),
                        getName().getValue(),
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
