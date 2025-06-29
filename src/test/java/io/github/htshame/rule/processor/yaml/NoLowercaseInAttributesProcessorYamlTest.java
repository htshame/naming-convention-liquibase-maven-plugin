package io.github.htshame.rule.processor.yaml;

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

public class NoLowercaseInAttributesProcessorYamlTest extends RuleProcessorTestUtil {

    private static final String BASE_FILE_PATH =
            "src/test/resources/io/github/htshame/rule/processor/no-lowercase-in-attributes/";
    private static final String RULE_URL = BASE_FILE_PATH + "no-lowercase-in-attributes-rule.xml";
    private static final String EXCLUSION_EMPTY_URL = BASE_FILE_PATH + "exclusions_empty.xml";
    private static final String EXCLUSION_WRONG_URL = BASE_FILE_PATH + "exclusions_wrong_yaml.xml";
    private static final String EXCLUSION_URL = BASE_FILE_PATH + "exclusions_yaml.xml";
    private static final String NO_LOWERCASE_IN_ATTRIBUTES_FAILURE_FILE = "no-lowercase-in-attributes-failure.yaml";
    private static final String NO_LOWERCASE_IN_ATTRIBUTES_SUCCESS_FILE = "no-lowercase-in-attributes-success.yaml";

    /**
     * Default constructor.
     */
    public NoLowercaseInAttributesProcessorYamlTest() {
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
                BASE_FILE_PATH + NO_LOWERCASE_IN_ATTRIBUTES_FAILURE_FILE,
                ChangeLogFormatEnum.YAML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_EMPTY_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_1",
                        "test",
                        List.of(
                                "Property [indexName] of key [createIndex] contains lowercase characters in value:"
                                        + " [IDX_user_metadata]")),
                prepareTestErrorMessage(
                        "changelog_02_2",
                        "test",
                        List.of("Property [tableName] of key [createIndex] contains lowercase characters in value:"
                                        + " [user_metadata]",
                                "Property [indexName] of key [createIndex] contains lowercase characters in value:"
                                        + " [user-metadata-IDX]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoLowercaseInAttributesProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        NO_LOWERCASE_IN_ATTRIBUTES_FAILURE_FILE,
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
                BASE_FILE_PATH + NO_LOWERCASE_IN_ATTRIBUTES_FAILURE_FILE,
                ChangeLogFormatEnum.YAML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_WRONG_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_1",
                        "test",
                        List.of(
                                "Property [indexName] of key [createIndex] contains lowercase characters in value:"
                                        + " [IDX_user_metadata]")),
                prepareTestErrorMessage(
                        "changelog_02_2",
                        "test",
                        List.of("Property [tableName] of key [createIndex] contains lowercase characters in value:"
                                        + " [user_metadata]",
                                "Property [indexName] of key [createIndex] contains lowercase characters in value:"
                                        + " [user-metadata-IDX]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoLowercaseInAttributesProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        NO_LOWERCASE_IN_ATTRIBUTES_FAILURE_FILE,
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
                BASE_FILE_PATH + NO_LOWERCASE_IN_ATTRIBUTES_FAILURE_FILE,
                ChangeLogFormatEnum.YAML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_URL));
        List<String> expectedErrorMessages = Collections.singletonList(
                prepareTestErrorMessage(
                        "changelog_02_2",
                        "test",
                        List.of("Property [tableName] of key [createIndex] contains lowercase characters in value:"
                                        + " [user_metadata]",
                                "Property [indexName] of key [createIndex] contains lowercase characters in value:"
                                        + " [user-metadata-IDX]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoLowercaseInAttributesProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        NO_LOWERCASE_IN_ATTRIBUTES_FAILURE_FILE,
                        ChangeLogFormatEnum.YAML);
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
                BASE_FILE_PATH + NO_LOWERCASE_IN_ATTRIBUTES_SUCCESS_FILE,
                ChangeLogFormatEnum.YAML);
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_URL));

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoLowercaseInAttributesProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        NO_LOWERCASE_IN_ATTRIBUTES_SUCCESS_FILE,
                        ChangeLogFormatEnum.YAML);
            } catch (ValidationException e) {
                isExceptionThrown = true;
            }
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
