package io.github.htshame.rule.processor.changelog.xml;

import io.github.htshame.change.element.ChangeLogElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.processor.changelog.ChangeLogRuleProcessorTestUtil;
import io.github.htshame.rule.processor.changelog.TagMustNotExistInChangeLogProcessor;
import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TagMustNotExistInChangeLogProcessorXmlTest extends ChangeLogRuleProcessorTestUtil {

    private static final RuleEnum RULE_ENUM = RuleEnum.TAG_MUST_NOT_EXIST_IN_CHANGELOG;
    private static final ChangeLogFormatEnum CHANGELOG_FORMAT = ChangeLogFormatEnum.XML;

    /**
     * Default constructor.
     */
    public TagMustNotExistInChangeLogProcessorXmlTest() {
        super(RULE_ENUM, CHANGELOG_FORMAT);
    }

    /**
     * Test getting the name.
     */
    @Test
    public void testGetName() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Element ruleElement = prepareRuleELement(CHANGELOG_FORMAT, "success");

        // act
        RuleEnum actual = TagMustNotExistInChangeLogProcessor.instantiate(ruleElement).getName();

        // assert
        assertEquals(RULE_ENUM, actual);
    }

    /**
     * Test failed validation.
     */
    @Test
    public void testValidateChangeSetFailure() throws ParserConfigurationException,
            IOException,
            SAXException,
            ExclusionParserException, ChangeLogParseException {
        // arrange
        List<ChangeLogElement> changeLogElements = parseChangeLogFile(
                getBaseUrlPathFormatted() + getChangelogFailureFile(),
                CHANGELOG_FORMAT);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement(CHANGELOG_FORMAT, "failure");
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionEmptyUrl()));
        List<String> expectedErrorMessages = Arrays.asList(
                "Tag <includeAll> must not exist in change log",
                "Tag <includeAll> must not exist in change log"
        );
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeLogElement changeLogElement : changeLogElements) {
            try {
                TagMustNotExistInChangeLogProcessor.instantiate(ruleElement).validateChangeLog(
                        changeLogElement,
                        exclusionParser,
                        getChangelogFailureFile(),
                        CHANGELOG_FORMAT);
            } catch (ValidationException e) {
                exceptionCount++;
                actualErrorMessages.add(e.getMessage());
            }
        }

        // assert
        assertEquals(2, exceptionCount);
        assertErrors(expectedErrorMessages, actualErrorMessages);
    }

    /**
     * Test failed validation with wrong exclusion.
     */
    @Test
    public void testValidateChangeSetFailureWrongExclusion() throws ParserConfigurationException,
            IOException,
            SAXException,
            ExclusionParserException,
            ChangeLogParseException {
        // arrange
        List<ChangeLogElement> changeSetElements = parseChangeLogFile(
                getBaseUrlPathFormatted() + getChangelogFailureFile(),
                CHANGELOG_FORMAT);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement(CHANGELOG_FORMAT, "failure");
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionWrongUrl()));
        List<String> expectedErrorMessages = Arrays.asList(
                "Tag <includeAll> must not exist in change log",
                "Tag <includeAll> must not exist in change log"
        );

        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeLogElement changeSetElement : changeSetElements) {
            try {
                TagMustNotExistInChangeLogProcessor.instantiate(ruleElement).validateChangeLog(
                        changeSetElement,
                        exclusionParser,
                        getChangelogFailureFile(),
                        CHANGELOG_FORMAT);
            } catch (ValidationException e) {
                exceptionCount++;
                actualErrorMessages.add(e.getMessage());
            }
        }

        // assert
        assertEquals(2, exceptionCount);
        assertErrors(expectedErrorMessages, actualErrorMessages);
    }

    /**
     * Test failed validation with exclusion.
     */
    @Test
    public void testValidateChangeSetFailureWithExclusion() throws ParserConfigurationException,
            IOException,
            SAXException,
            ExclusionParserException, ChangeLogParseException {
        // arrange
        List<ChangeLogElement> changeSetElements = parseChangeLogFile(
                getBaseUrlPathFormatted() + getChangelogFailureFile(),
                CHANGELOG_FORMAT);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement(CHANGELOG_FORMAT, "failure");
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionUrl()));

        // act
        for (ChangeLogElement changeSetElement : changeSetElements) {
            try {
                TagMustNotExistInChangeLogProcessor.instantiate(ruleElement).validateChangeLog(
                        changeSetElement,
                        exclusionParser,
                        getChangelogFailureFile(),
                        CHANGELOG_FORMAT);
            } catch (ValidationException e) {
                exceptionCount++;
            }
        }

        // assert
        assertEquals(0, exceptionCount);
    }

    /**
     * Test successful validation.
     */
    @Test
    public void testValidateChangeSetSuccess() throws ParserConfigurationException,
            IOException,
            SAXException,
            ExclusionParserException,
            ChangeLogParseException {
        // arrange
        List<ChangeLogElement> changeSetElements = parseChangeLogFile(
                getBaseUrlPathFormatted() + getChangelogSuccessFile(),
                CHANGELOG_FORMAT);
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement(CHANGELOG_FORMAT, "success");
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionEmptyUrl()));

        // act
        for (ChangeLogElement changeSetElement : changeSetElements) {
            try {
                TagMustNotExistInChangeLogProcessor.instantiate(ruleElement).validateChangeLog(
                        changeSetElement,
                        exclusionParser,
                        getChangelogSuccessFile(),
                        CHANGELOG_FORMAT);
            } catch (ValidationException e) {
                isExceptionThrown = true;
            }
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
