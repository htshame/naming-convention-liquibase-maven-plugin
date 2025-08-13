package io.github.htshame.rule.processor.changelog;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ValidationException;
import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChangeLogFileLinesLimitProcessorTest extends ChangeLogRuleProcessorTestUtil {

    private static final RuleEnum RULE_ENUM = RuleEnum.CHANGELOG_FILE_LINES_LIMIT;

    /**
     * Default constructor.
     */
    public ChangeLogFileLinesLimitProcessorTest() {
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
        RuleEnum actual = ChangeLogFileLinesLimitProcessor.instantiate(ruleElement).getName();

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
            ChangeLogFileLinesLimitProcessor.instantiate(ruleElement).validateChangeLogFile(changeLogFile);
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
            ChangeLogFileLinesLimitProcessor.instantiate(ruleElement).validateChangeLogFile(changeLogFile);
        } catch (ValidationException e) {
            isExceptionThrown = true;
            assertEquals("File [changelog-file-lines-limit-failure.xml] has [119] lines,"
                    + " longer than [100] lines max. Rule [changelog-file-lines-limit]",
                    e.getMessage());
        }

        // assert
        assertTrue(isExceptionThrown);
    }

    /**
     * Test failed validation with wrong file path.
     */
    @Test
    public void testChangeLogValidationFailureNotFound() throws ParserConfigurationException,
            IOException,
            SAXException {
        // arrange
        Element ruleElement = prepareRuleELement();
        File changeLogFile = getChangeLogFailureFileNotFound();
        boolean isExceptionThrown = false;

        // act
        try {
            ChangeLogFileLinesLimitProcessor.instantiate(ruleElement).validateChangeLogFile(changeLogFile);
        } catch (ValidationException e) {
            isExceptionThrown = true;
            assertEquals("Unable to read file [changelog-file-lines-limit-failure.xml1]."
                            + " Rule [changelog-file-lines-limit]",
                    e.getMessage());
        }

        // assert
        assertTrue(isExceptionThrown);
    }
}
