package io.github.htshame.rule.processor;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ValidationException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NoHyphensInAttributesProcessorTest {

    /**
     * Test getting the name.
     */
    @Test
    public void testGetName() {
        // arrange

        // act
        RuleEnum actual = NoHyphensInAttributesProcessor.fromXml(null).getName();

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

        // act
        try {
            new NoHyphensInAttributesProcessor().validate(document);
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

        // act
        try {
            new NoHyphensInAttributesProcessor().validate(document);
        } catch (ValidationException e) {
            isExceptionThrown = true;
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
