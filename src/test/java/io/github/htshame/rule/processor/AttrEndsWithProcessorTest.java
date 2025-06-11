package io.github.htshame.rule.processor;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ValidationException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttrEndsWithProcessorTest {

    private static final String TAG = "addForeignKeyConstraint";
    private static final String TARGET_ATTRIBUTE = "constraintName";
    private static final String REQUIRED_SUFFIX = "_fk";

    /**
     * Test getting the name.
     */
    @Test
    public void testGetName() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File("src/test/resources/rules.xml"));
        NodeList ruleNodes = document.getElementsByTagName(RuleStructureEnum.RULE_TAG.getValue());
        Element ruleElement = (Element) ruleNodes.item(0);

        // act
        RuleEnum actual = AttrEndsWithProcessor.fromXml(ruleElement).getName();

        // assert
        assertEquals(RuleEnum.ATTRIBUTE_ENDS_WITH, actual);
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
                        + "attr-ends-with-failure.xml"));
        boolean isExceptionThrown = false;

        // act
        try {
            new AttrEndsWithProcessor(
                    TAG,
                    TARGET_ATTRIBUTE,
                    REQUIRED_SUFFIX).validate(document);
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
                        + "attr-ends-with-success.xml"));
        boolean isExceptionThrown = false;

        // act
        try {
            new AttrEndsWithProcessor(
                    TAG,
                    TARGET_ATTRIBUTE,
                    REQUIRED_SUFFIX).validate(document);
        } catch (ValidationException e) {
            isExceptionThrown = true;
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
