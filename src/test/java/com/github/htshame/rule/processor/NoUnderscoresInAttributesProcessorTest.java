package com.github.htshame.rule.processor;

import com.github.htshame.enums.RuleEnum;
import com.github.htshame.exception.ValidationException;
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

public class NoUnderscoresInAttributesProcessorTest {

    /**
     * Test getting the name.
     */
    @Test
    public void testGetName() {
        // arrange

        // act
        RuleEnum actual = NoUnderscoresInAttributesProcessor.fromXml(null).getName();

        // assert
        assertEquals(RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES, actual);
    }

    /**
     * Test failed validation.
     */
    @Test
    public void testValidateFailure() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File("src/test/resources/com/github/htshame/rule/processor/"
                        + "no-underscores-in-attributes-failure.xml"));
        boolean isExceptionThrown = false;

        // act
        try {
            new NoUnderscoresInAttributesProcessor().validate(document);
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
                .parse(new File("src/test/resources/com/github/htshame/rule/processor/"
                        + "no-underscores-in-attributes-success.xml"));
        boolean isExceptionThrown = false;

        // act
        try {
            new NoUnderscoresInAttributesProcessor().validate(document);
        } catch (ValidationException e) {
            isExceptionThrown = true;
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
