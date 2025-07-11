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

public class ChangeLogFileMustMatchRegexpProcessorTest extends ChangeLogRuleProcessorTestUtil {

    private static final RuleEnum RULE_ENUM = RuleEnum.CHANGELOG_FILE_NAME_MUST_MATCH_REGEXP;

    /**
     * Default constructor.
     */
    public ChangeLogFileMustMatchRegexpProcessorTest() {
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
        RuleEnum actual = ChangeLogFileMustMatchRegexpProcessor.instantiate(ruleElement).getName();

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
            ChangeLogFileMustMatchRegexpProcessor.instantiate(ruleElement).validateChangeLog(changeLogFile);
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
            ChangeLogFileMustMatchRegexpProcessor.instantiate(ruleElement).validateChangeLog(changeLogFile);
        } catch (ValidationException e) {
            isExceptionThrown = true;
            assertEquals("File [changelog-file-name-must-match-regexp-failure.xml] "
                            + "does not match required regexp [^changelog_\\d+\\.(xml|json|ya?ml)$]. "
                            + "Rule [changelog-file-name-must-match-regexp]",
                    e.getMessage());
        }

        // assert
        assertTrue(isExceptionThrown);
    }
}
