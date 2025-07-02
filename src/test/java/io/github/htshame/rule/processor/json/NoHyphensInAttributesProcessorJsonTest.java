package io.github.htshame.rule.processor.json;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.processor.NoHyphensInAttributesProcessor;
import io.github.htshame.rule.processor.RuleProcessorTestUtil;
import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class NoHyphensInAttributesProcessorJsonTest extends RuleProcessorTestUtil {

    private static final RuleEnum RULE_ENUM = RuleEnum.NO_HYPHENS_IN_ATTRIBUTES;
    private static final ChangeLogFormatEnum CHANGELOG_FORMAT = ChangeLogFormatEnum.JSON;

    /**
     * Default constructor.
     */
    public NoHyphensInAttributesProcessorJsonTest() {
        super(RULE_ENUM, CHANGELOG_FORMAT);
    }

    /**
     * Test getting the name.
     */
    @Test
    public void testGetName() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Element ruleElement = prepareRuleELement();

        // act
        RuleEnum actual = NoHyphensInAttributesProcessor.instantiate(ruleElement).getName();

        // assert
        assertEquals(RuleEnum.NO_HYPHENS_IN_ATTRIBUTES, actual);
    }

    /**
     * Test failed validation.
     */
    @Test
    public void testValidateFailure() throws ParserConfigurationException,
            IOException,
            SAXException,
            ExclusionParserException,
            ChangeLogParseException {
        // arrange
        List<ChangeSetElement> changeSetElements = parseChangeSetFile(
                getBaseUrlPathFormatted() + getChangelogFailureFile(),
                CHANGELOG_FORMAT);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionEmptyUrl()));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_3",
                        "test",
                        List.of("Property [tableName] of object [createIndex] contains hyphen in value: "
                                + "[user-metadata]")),
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test",
                        List.of("Property [tableName] of object [createIndex] contains hyphen in value: "
                                + "[user-metadata]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoHyphensInAttributesProcessor.instantiate(ruleElement).validate(
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
     * Test failed validation with wrong exclusion.
     */
    @Test
    public void testValidateFailureWrongExclusion() throws ParserConfigurationException,
            IOException,
            SAXException,
            ExclusionParserException,
            ChangeLogParseException {
        // arrange
        List<ChangeSetElement> changeSetElements = parseChangeSetFile(
                getBaseUrlPathFormatted() + getChangelogFailureFile(),
                CHANGELOG_FORMAT);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionWrongUrl()));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_3",
                        "test",
                        List.of("Property [tableName] of object [createIndex] contains hyphen in value: "
                                + "[user-metadata]")),
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test",
                        List.of("Property [tableName] of object [createIndex] contains hyphen in value: "
                                + "[user-metadata]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoHyphensInAttributesProcessor.instantiate(ruleElement).validate(
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
    public void testValidateFailureWithExclusion() throws ParserConfigurationException,
            IOException,
            SAXException,
            ExclusionParserException,
            ChangeLogParseException {
        // arrange
        List<ChangeSetElement> changeSetElements = parseChangeSetFile(
                getBaseUrlPathFormatted() + getChangelogFailureFile(),
                CHANGELOG_FORMAT);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionUrl()));
        List<String> expectedErrorMessages = Collections.singletonList(
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test",
                        List.of("Property [tableName] of object [createIndex] contains hyphen in value: "
                                + "[user-metadata]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoHyphensInAttributesProcessor.instantiate(ruleElement).validate(
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
        assertEquals(1, exceptionCount);
        assertErrors(expectedErrorMessages, actualErrorMessages);
    }

    /**
     * Test successful validation.
     */
    @Test
    public void testValidateSuccess() throws ParserConfigurationException,
            IOException,
            SAXException,
            ExclusionParserException, ChangeLogParseException {
        // arrange
        List<ChangeSetElement> changeSetElements = parseChangeSetFile(
                getBaseUrlPathFormatted() + getChangelogSuccessFile(),
                CHANGELOG_FORMAT);
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionEmptyUrl()));

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoHyphensInAttributesProcessor.instantiate(ruleElement).validate(
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
