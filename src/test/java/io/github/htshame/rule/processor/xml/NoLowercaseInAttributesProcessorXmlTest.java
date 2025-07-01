package io.github.htshame.rule.processor.xml;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.processor.NoLowercaseInAttributesProcessor;
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

public class NoLowercaseInAttributesProcessorXmlTest extends RuleProcessorTestUtil {

    private static final String BASE_FILE_PATH =
            "src/test/resources/io/github/htshame/rule/processor/no-lowercase-in-attributes/";
    private static final String RULE_URL = BASE_FILE_PATH + "no-lowercase-in-attributes-rule.xml";
    private static final String EXCLUSION_EMPTY_URL = BASE_FILE_PATH + "exclusions_empty.xml";

    private static final ChangeLogFormatEnum CHANGELOG_FORMAT = ChangeLogFormatEnum.XML;
    private static final String BASE_URL_PATH_FORMATTED = BASE_FILE_PATH + "/" + CHANGELOG_FORMAT + "/";
    private static final String EXCLUSION_WRONG_URL = BASE_URL_PATH_FORMATTED + "exclusions_wrong_xml.xml";
    private static final String EXCLUSION_URL = BASE_URL_PATH_FORMATTED + "exclusions_xml.xml";
    private static final String CHANGELOG_FAILURE_FILE = "no-lowercase-in-attributes-failure.xml";
    private static final String CHANGELOG_SUCCESS_FILE = "no-lowercase-in-attributes-success.xml";

    /**
     * Default constructor.
     */
    public NoLowercaseInAttributesProcessorXmlTest() {
        super(RULE_URL, RuleEnum.NO_LOWERCASE_IN_ATTRIBUTES);
    }

    /**
     * Test getting the name.
     */
    @Test
    public void testGetName() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Element ruleElement = prepareRuleELement();

        // act
        RuleEnum actual = NoLowercaseInAttributesProcessor.instantiate(ruleElement).getName();

        // assert
        assertEquals(RuleEnum.NO_LOWERCASE_IN_ATTRIBUTES, actual);
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
                BASE_URL_PATH_FORMATTED + CHANGELOG_FAILURE_FILE,
                ChangeLogFormatEnum.XML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_EMPTY_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_1",
                        "test",
                        List.of(
                                "Attribute [indexName] of tag <createIndex> contains lowercase characters in value: "
                                        + "[IDX_user_metadata]")),
                prepareTestErrorMessage(
                        "changelog_02_2",
                        "test",
                        List.of("Attribute [indexName] of tag <createIndex> contains lowercase characters in value:"
                                        + " [user-metadata-IDX]",
                                "Attribute [tableName] of tag <createIndex> contains lowercase characters in value:"
                                        + " [user_metadata]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoLowercaseInAttributesProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        CHANGELOG_FAILURE_FILE,
                        ChangeLogFormatEnum.XML);
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
                BASE_URL_PATH_FORMATTED + CHANGELOG_FAILURE_FILE,
                ChangeLogFormatEnum.XML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_WRONG_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_1",
                        "test",
                        List.of(
                                "Attribute [indexName] of tag <createIndex> contains lowercase characters in value: "
                                        + "[IDX_user_metadata]")),
                prepareTestErrorMessage(
                        "changelog_02_2",
                        "test",
                        List.of("Attribute [indexName] of tag <createIndex> contains lowercase characters in value:"
                                        + " [user-metadata-IDX]",
                                "Attribute [tableName] of tag <createIndex> contains lowercase characters in value:"
                                        + " [user_metadata]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoLowercaseInAttributesProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        CHANGELOG_FAILURE_FILE,
                        ChangeLogFormatEnum.XML);
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
                BASE_URL_PATH_FORMATTED + CHANGELOG_FAILURE_FILE,
                ChangeLogFormatEnum.XML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_URL));
        List<String> expectedErrorMessages = Collections.singletonList(
                prepareTestErrorMessage(
                        "changelog_02_2",
                        "test",
                        List.of("Attribute [indexName] of tag <createIndex> contains lowercase characters in value:"
                                        + " [user-metadata-IDX]",
                                "Attribute [tableName] of tag <createIndex> contains lowercase characters in value:"
                                        + " [user_metadata]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoLowercaseInAttributesProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        CHANGELOG_FAILURE_FILE,
                        ChangeLogFormatEnum.XML);
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
            ExclusionParserException,
            ChangeLogParseException {
        // arrange
        List<ChangeSetElement> changeSetElements = parseChangeSetFile(
                BASE_URL_PATH_FORMATTED + CHANGELOG_SUCCESS_FILE,
                ChangeLogFormatEnum.XML);
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_URL));

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoLowercaseInAttributesProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        CHANGELOG_SUCCESS_FILE,
                        ChangeLogFormatEnum.XML);
            } catch (ValidationException e) {
                isExceptionThrown = true;
            }
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
