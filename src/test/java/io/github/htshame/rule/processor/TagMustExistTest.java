package io.github.htshame.rule.processor;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.util.parser.ExclusionParser;
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
import static org.junit.Assert.assertTrue;

public class TagMustExistTest extends RuleProcessorTestUtil {

    private static final String BASE_FILE_PATH =
            "src/test/resources/io/github/htshame/rule/processor/tag-must-exist/";
    private static final String RULE_URL = BASE_FILE_PATH + "tag-must-exist-rule.xml";
    private static final String EXCLUSION_EMPTY_URL = BASE_FILE_PATH + "exclusions_empty.xml";
    private static final String EXCLUSION_URL = BASE_FILE_PATH + "exclusions.xml";
    private static final String EXCLUSION_WRONG_URL = BASE_FILE_PATH + "exclusions_wrong.xml";
    private static final String TAG_MUST_EXIST_FAILURE_XML = "tag-must-exist-failure.xml";
    private static final String TAG_MUST_EXIST_SUCCESS_XML = "tag-must-exist-success.xml";


    /**
     * Default constructor.
     */
    public TagMustExistTest() {
        super(RULE_URL, RuleEnum.TAG_MUST_EXIST);
    }

    /**
     * Test getting the name.
     */
    @Test
    public void testGetName() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Element ruleElement = prepareRuleELement();

        // act
        RuleEnum actual = TagMustExistProcessor.instantiate(ruleElement).getName();

        // assert
        assertEquals(RuleEnum.TAG_MUST_EXIST, actual);
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
                BASE_FILE_PATH + TAG_MUST_EXIST_FAILURE_XML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_EMPTY_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_3",
                        "test1",
                        List.of("Tag <changeSet> does not contain required tag <comment>")),
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test1",
                        List.of("Tag <changeSet> does not contain required tag <comment>")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                TagMustExistProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        TAG_MUST_EXIST_FAILURE_XML);
            } catch (ValidationException e) {
                exceptionCount++;
                actualErrorMessages.add(e.getMessage());
            }
        }

        // assert
        assertEquals(2, exceptionCount);
        assertTrue(expectedErrorMessages.containsAll(actualErrorMessages));
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
                BASE_FILE_PATH + TAG_MUST_EXIST_FAILURE_XML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_WRONG_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_3",
                        "test1",
                        List.of("Tag <changeSet> does not contain required tag <comment>")),
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test1",
                        List.of("Tag <changeSet> does not contain required tag <comment>")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                TagMustExistProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        TAG_MUST_EXIST_FAILURE_XML);
            } catch (ValidationException e) {
                exceptionCount++;
                actualErrorMessages.add(e.getMessage());
            }
        }

        // assert
        assertEquals(2, exceptionCount);
        assertTrue(expectedErrorMessages.containsAll(actualErrorMessages));
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
                BASE_FILE_PATH + TAG_MUST_EXIST_FAILURE_XML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_URL));
        List<String> expectedErrorMessages = Collections.singletonList(
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test1",
                        List.of("Tag <changeSet> does not contain required tag <comment>")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                TagMustExistProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        TAG_MUST_EXIST_FAILURE_XML);
            } catch (ValidationException e) {
                exceptionCount++;
                actualErrorMessages.add(e.getMessage());
            }
        }

        // assert
        assertEquals(1, exceptionCount);
        assertTrue(expectedErrorMessages.containsAll(actualErrorMessages));
    }

    /**
     * Test successful validation.
     */
    @Test
    public void testValidateSuccess() throws ParserConfigurationException,
            IOException,
            SAXException,
            ExclusionParserException,
            ChangeLogParseException {
        // arrange
        List<ChangeSetElement> changeSetElements = parseChangeSetFile(
                BASE_FILE_PATH + TAG_MUST_EXIST_SUCCESS_XML);
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_EMPTY_URL));

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                TagMustExistProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        TAG_MUST_EXIST_SUCCESS_XML);
            } catch (ValidationException e) {
                isExceptionThrown = true;
            }
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
