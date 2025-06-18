package io.github.htshame.rule.processor;

import io.github.htshame.dto.ChangeSetAttributeDto;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.Rule;
import io.github.htshame.rule.RulePreProcessor;
import io.github.htshame.util.ChangeSetUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.htshame.util.RuleUtil.isExcludedByAncestorTag;

/**
 * Business logic for <code>no-hyphens-in-attributes</code> rule.
 * <p>
 * Checks that the contents of changeLog file do not contain hyphens.
 * <p>
 * E.g.:
 * <p>
 * Rule configuration:
 * {@code <rule name="no-hyphens-in-attributes"/>}
 */
public class NoHyphensInAttributesProcessor extends RulePreProcessor implements Rule {

    private static final List<String> EXCLUDED_ATTRIBUTES = Arrays.asList("id", "author");
    private static final String HYPHEN = "-";

    private final Set<String> excludedAttrs;

    /**
     * Constructor.
     *
     * @param excludedAttrs - excluded attributes.
     */
    public NoHyphensInAttributesProcessor(final Set<String> excludedAttrs) {
        this.excludedAttrs = excludedAttrs != null ? excludedAttrs : new HashSet<>();
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
        validateElement(changeSetElement);
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.NO_HYPHENS_IN_ATTRIBUTES;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link NoHyphensInAttributesProcessor}.
     */
    public static NoHyphensInAttributesProcessor fromXml(final Element element) {
        Set<String> excludedParents = new HashSet<>();
        NodeList excludedTags = element
                .getElementsByTagName(RuleStructureEnum.EXCLUDED_ATTRS.getValue());
        if (excludedTags.item(0) != null) {
            NodeList excludedTagElements = ((Element) excludedTags.item(0))
                    .getElementsByTagName(RuleStructureEnum.ATTR.getValue());
            for (int j = 0; j < excludedTagElements.getLength(); j++) {
                excludedParents.add(excludedTagElements.item(j).getTextContent());
            }
        }

        return new NoHyphensInAttributesProcessor(excludedParents);
    }

    /**
     * Validate element.
     *
     * @param element - element.
     * @throws ValidationException - thrown if validation fails.
     */
    private void validateElement(final Element element) throws ValidationException {
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attr = (Attr) attributes.item(i);
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (!isExcludedByAncestorTag(element)
                    && !EXCLUDED_ATTRIBUTES.contains(attrName)
                    && !excludedAttrs.contains(attrName)
                    && attrValue.contains(HYPHEN)) {
                ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(element);
                String errorMessage = String.format(
                        "ChangeSet: id=\"%s\", author=\"%s\". Rule: [%s]. Attribute %s in element <%s> contains "
                                + "hyphen in value: [%s].",
                        changeSetAttributeDto.getId(),
                        changeSetAttributeDto.getAuthor(),
                        getName().getValue(),
                        attrName,
                        element.getTagName(),
                        attrValue);
                throw new ValidationException(errorMessage);
            }
        }

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                validateElement((Element) child);
            }
        }
    }
}
