package io.github.htshame.rule.processor.yaml;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.processor.RuleProcessorTestUtil;
import io.github.htshame.rule.processor.TagMustExistProcessor;
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

public class TagMustExistYamlTest extends RuleProcessorTestUtil {

    private static final String BASE_FILE_PATH =
            "src/test/resources/io/github/htshame/rule/processor/tag-must-exist/";
    private static final String RULE_URL = BASE_FILE_PATH + "tag-must-exist-rule.xml";
    private static final String EXCLUSION_EMPTY_URL = BASE_FILE_PATH + "exclusions_empty.xml";
    private static final String EXCLUSION_URL = BASE_FILE_PATH + "exclusions_yaml.xml";
    private static final String EXCLUSION_WRONG_URL = BASE_FILE_PATH + "exclusions_wrong_yaml.xml";
    private static final String TAG_MUST_EXIST_FAILURE = "tag-must-exist-failure.yaml";
    private static final String TAG_MUST_EXIST_SUCCESS = "tag-must-exist-success.yaml";
    private static final int EXPECTED_NUMBER_OF_ERRORS = 3;


    /**
     * Default constructor.
     */
    public TagMustExistYamlTest() {
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
                BASE_FILE_PATH + TAG_MUST_EXIST_FAILURE,
                ChangeLogFormatEnum.YAML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_EMPTY_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_3",
                        "test1",
                        List.of("Key [changeSet]. Required nested property [comment] is missing or empty")),
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test1",
                        List.of(
                                "Key [changeSet]. Required nested property [comment] is missing or empty",
                                "Key [rollback]. Required nested property [comment] is missing or empty")),
                prepareTestErrorMessage(
                        "changelog_02_5",
                        "test1",
                        List.of(
                                "Key [changeSet]. Required nested property [comment] is missing or empty",
                                "Key [rollback]. Required nested property [comment] is missing or empty")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                TagMustExistProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        TAG_MUST_EXIST_FAILURE,
                        ChangeLogFormatEnum.YAML);
            } catch (ValidationException e) {
                exceptionCount++;
                actualErrorMessages.add(e.getMessage());
            }
        }

        // assert
        assertEquals(EXPECTED_NUMBER_OF_ERRORS, exceptionCount);
        for (int i = 0; i < expectedErrorMessages.size(); i++) {
            assertEquals(expectedErrorMessages.get(i), actualErrorMessages.get(i));
        }
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
                BASE_FILE_PATH + TAG_MUST_EXIST_FAILURE,
                ChangeLogFormatEnum.YAML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_WRONG_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_3",
                        "test1",
                        List.of("Key [changeSet]. Required nested property [comment] is missing or empty")),
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test1",
                        List.of(
                                "Key [changeSet]. Required nested property [comment] is missing or empty",
                                "Key [rollback]. Required nested property [comment] is missing or empty")),
                prepareTestErrorMessage(
                        "changelog_02_5",
                        "test1",
                        List.of(
                                "Key [changeSet]. Required nested property [comment] is missing or empty",
                                "Key [rollback]. Required nested property [comment] is missing or empty")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                TagMustExistProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        TAG_MUST_EXIST_FAILURE,
                        ChangeLogFormatEnum.YAML);
            } catch (ValidationException e) {
                exceptionCount++;
                actualErrorMessages.add(e.getMessage());
            }
        }

        // assert
        assertEquals(EXPECTED_NUMBER_OF_ERRORS, exceptionCount);
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
                BASE_FILE_PATH + TAG_MUST_EXIST_FAILURE,
                ChangeLogFormatEnum.YAML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_URL));
        List<String> expectedErrorMessages = List.of(
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test1",
                        List.of(
                                "Key [changeSet]. Required nested property [comment] is missing or empty",
                                "Key [rollback]. Required nested property [comment] is missing or empty")),
                prepareTestErrorMessage(
                        "changelog_02_5",
                        "test1",
                        List.of(
                                "Key [changeSet]. Required nested property [comment] is missing or empty",
                                "Key [rollback]. Required nested property [comment] is missing or empty")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                TagMustExistProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        TAG_MUST_EXIST_FAILURE,
                        ChangeLogFormatEnum.YAML);
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
                BASE_FILE_PATH + TAG_MUST_EXIST_SUCCESS,
                ChangeLogFormatEnum.YAML);
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_EMPTY_URL));

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                TagMustExistProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        TAG_MUST_EXIST_SUCCESS,
                        ChangeLogFormatEnum.YAML);
            } catch (ValidationException e) {
                isExceptionThrown = true;
            }
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
