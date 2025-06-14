package io.github.htshame.rule.processor;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ValidationException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NoHyphensInAttributesProcessorTest extends RuleProcessorTest {

    private static final String RULE_URL =
            "src/test/resources/io/github/htshame/rule/processor/no-hyphens-in-attributes-rule.xml";

    /**
     * Default constructor.
     */
    public NoHyphensInAttributesProcessorTest() {
        super(RULE_URL);
    }

    /**
     * Test getting the name.
     */
    @Test
    public void testGetName() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Element ruleElement = prepareRuleELement();

        // act
        RuleEnum actual = NoHyphensInAttributesProcessor.fromXml(ruleElement).getName();

        // assert
        assertEquals(RuleEnum.NO_HYPHENS_IN_ATTRIBUTES, actual);
    }

    /**
     * Test failed validation.
     */
    @Test
    public void testValidateFailure() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File("src/test/resources/io/github/htshame/rule/processor/"
                        + "no-hyphens-in-attributes-failure.xml"));
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();

        // act
        try {
            NoHyphensInAttributesProcessor.fromXml(ruleElement).validate(document);
        } catch (ValidationException e) {
            isExceptionThrown = true;
        }

        // assert
        assertTrue(isExceptionThrown);
    }

    /**
     * Test successful validation.
     */
    @Test
    public void testValidateSuccess() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File("src/test/resources/io/github/htshame/rule/processor/"
                        + "no-hyphens-in-attributes-success.xml"));
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();

        // act
        try {
            NoHyphensInAttributesProcessor.fromXml(ruleElement).validate(document);
        } catch (ValidationException e) {
            isExceptionThrown = true;
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
