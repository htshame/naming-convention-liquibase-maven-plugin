package com.github.htshame.rule.processor;

import com.github.htshame.enums.RuleEnum;
import com.github.htshame.enums.RuleStructureEnum;
import com.github.htshame.exception.ValidationException;
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

public class AttrEndsWithConditionedProcessorTest {

    private static final String TAG = "createIndex";
    private static final String CONDITION_ATTRIBUTE = "unique";
    private static final String CONDITION_VALUE = "true";
    private static final String TARGET_ATTRIBUTE = "indexName";
    private static final String REQUIRED_SUFFIX = "_unique";

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
        RuleEnum actual = AttrEndsWithConditionedProcessor.fromXml(ruleElement).getName();

        // assert
        assertEquals(RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED, actual);
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
                        + "attr-ends-with-conditioned-failure.xml"));
        boolean isExceptionThrown = false;

        // act
        try {
            new AttrEndsWithConditionedProcessor(
                    TAG,
                    CONDITION_ATTRIBUTE,
                    CONDITION_VALUE,
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
                .parse(new File("src/test/resources/com/github/htshame/rule/processor/"
                        + "attr-ends-with-conditioned-success.xml"));
        boolean isExceptionThrown = false;

        // act
        try {
            new AttrEndsWithConditionedProcessor(
                    TAG,
                    CONDITION_ATTRIBUTE,
                    CONDITION_VALUE,
                    TARGET_ATTRIBUTE,
                    REQUIRED_SUFFIX).validate(document);
        } catch (ValidationException e) {
            isExceptionThrown = true;
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
