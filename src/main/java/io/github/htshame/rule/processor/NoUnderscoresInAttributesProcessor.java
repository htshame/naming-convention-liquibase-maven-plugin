package io.github.htshame.rule.processor;

import io.github.htshame.dto.ChangeSetAttributeDto;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.rule.Rule;
import io.github.htshame.util.ChangeSetUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.List;

/**
 * Business logic for the <code>no-underscores-in-attributes</code> rule.
 * <p>
 * Checks that the contents of the changeLog file do not contain underscores.
 * </p>
 * <p>Example:</p>
 * <p>Rule configuration:</p>
 * <pre><code>
 * &lt;rule name="no-underscores-in-attributes"/&gt;
 * </code></pre>
 */

public class NoUnderscoresInAttributesProcessor implements Rule {

    private static final List<String> EXCLUDED_ATTRIBUTES = Arrays.asList("id", "author");
    private static final String UNDERSCORE = "_";

    /**
     * Default constructor.
     */
    public NoUnderscoresInAttributesProcessor() {

    }

    /**
     * Validate changeLog file.
     *
     * @param document - document.
     * @throws ValidationException - thrown if validation fails.
     */
    @Override
    public void validate(final Document document) throws ValidationException {
        validateElement(document.getDocumentElement());
    }

    /**
     * Get rule name.
     *
     * @return rule name.
     */
    @Override
    public RuleEnum getName() {
        return RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES;
    }

    /**
     * Populate rule with the contents from XML file.
     *
     * @param element - element.
     * @return instance of {@link NoUnderscoresInAttributesProcessor}.
     */
    public static NoUnderscoresInAttributesProcessor fromXml(final Element element) {
        return new NoUnderscoresInAttributesProcessor();
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

            if (!EXCLUDED_ATTRIBUTES.contains(attrName) && attrValue.contains(UNDERSCORE)) {
                ChangeSetAttributeDto changeSetAttributeDto = ChangeSetUtil.getAttributesFromAncestor(element);
                String errorMessage = String.format(
                        "ChangeSet: id=\"%s\", author=\"%s\". Attribute %s in element <%s> contains "
                                + "underscore in value: [%s]. Rule: %s",
                        changeSetAttributeDto.getId(),
                        changeSetAttributeDto.getAuthor(),
                        attrName,
                        element.getTagName(),
                        attrValue,
                        RuleEnum.NO_HYPHENS_IN_ATTRIBUTES.getValue());
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
