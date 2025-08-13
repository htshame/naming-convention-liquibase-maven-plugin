package io.github.htshame.rule.processor.changelog;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ChangeLogRuleProcessingException;
import io.github.htshame.exception.RuleParserException;
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

public class ChangeLogMustEndWithNewlineProcessorTest extends ChangeLogRuleProcessorTestUtil {

    private static final RuleEnum RULE_ENUM = RuleEnum.CHANGELOG_MUST_END_WITH_NEWLINE;

    /**
     * Default constructor.
     */
    public ChangeLogMustEndWithNewlineProcessorTest() {
        super(RULE_ENUM);
    }

    /**
     * Test getting the name.
     */
    @Test
    public void testGetName() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Element ruleElement = prepareRuleELement();

        // act
        RuleEnum actual = ChangeLogMustEndWithNewlineProcessor.instantiate(ruleElement).getName();

        // assert
        assertEquals(RULE_ENUM, actual);
    }

    /**
     * Test successful validation.
     */
    @Test
    public void testChangeLogValidationSuccess() throws ParserConfigurationException,
            IOException,
            SAXException {
        // arrange
        Element ruleElement = prepareRuleELement();
        File changeLogFile = getChangeLogSuccessFile();
        boolean isExceptionThrown = false;

        // act
        try {
            ChangeLogMustEndWithNewlineProcessor.instantiate(ruleElement).validateChangeLogFile(changeLogFile);
        } catch (ValidationException e) {
            isExceptionThrown = true;
        }

        // assert
        assertFalse(isExceptionThrown);
    }

    /**
     * Test failed validation.
     */
    @Test
    public void testChangeLogValidationFailure() throws ParserConfigurationException,
            IOException,
            SAXException {
        // arrange
        Element ruleElement = prepareRuleELement();
        File changeLogFile = getChangeLogFailureFile();
        boolean isExceptionThrown = false;

        // act
        try {
            ChangeLogMustEndWithNewlineProcessor.instantiate(ruleElement).validateChangeLogFile(changeLogFile);
        } catch (ValidationException e) {
            isExceptionThrown = true;
            assertEquals("File: [changelog-must-end-with-newline-failure.xml]. "
                            + "Rule [changelog-must-end-with-newline]. "
                            + "File changelog-must-end-with-newline-failure.xml does not end with a newline",
                    e.getMessage());
        }

        // assert
        assertTrue(isExceptionThrown);
    }

    /**
     * Test failed validation. Wrong rule configuration.
     */
    @Test
    public void testChangeLogValidationFailureWrongRule() throws ParserConfigurationException,
            IOException,
            SAXException,
            ValidationException {
        // arrange
        Document ruleDocument = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File("src/test/resources/io/github/htshame/rule/processor/changelog/"
                        + RULE_ENUM.getValue() + "/" + RULE_ENUM.getValue() + "-rule-failure.xml"));
        NodeList ruleNodes = ruleDocument.getElementsByTagName(RuleStructureEnum.RULE.getValue());
        Element ruleElement = (Element) ruleNodes.item(0);
        File changeLogFile = getChangeLogFailureFile();
        boolean isExceptionThrown = false;

        // act
        try {
            ChangeLogMustEndWithNewlineProcessor.instantiate(ruleElement).validateChangeLogFile(changeLogFile);
        } catch (RuleParserException e) {
            isExceptionThrown = true;
            assertEquals("Rule [CHANGELOG_MUST_END_WITH_NEWLINE] configuration should not contain child tags",
                    e.getMessage());
        }

        // assert
        assertTrue(isExceptionThrown);
    }

    /**
     * Test failed validation. Wrong file.
     */
    @Test
    public void testChangeLogValidationFailureWrongFile() throws ParserConfigurationException,
            IOException,
            SAXException,
            ValidationException {
        // arrange
        Element ruleElement = prepareRuleELement();
        File changeLogFile = new File("123123123123123");
        boolean isExceptionThrown = false;

        // act
        try {
            ChangeLogMustEndWithNewlineProcessor.instantiate(ruleElement).validateChangeLogFile(changeLogFile);
        } catch (ChangeLogRuleProcessingException e) {
            isExceptionThrown = true;
        }

        // assert
        assertTrue(isExceptionThrown);
    }
}
