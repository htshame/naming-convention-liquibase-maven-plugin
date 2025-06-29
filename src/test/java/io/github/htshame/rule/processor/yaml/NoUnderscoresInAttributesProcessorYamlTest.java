package io.github.htshame.rule.processor.yaml;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.processor.NoUnderscoresInAttributesProcessor;
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

public class NoUnderscoresInAttributesProcessorYamlTest extends RuleProcessorTestUtil {

    private static final String BASE_FILE_PATH =
            "src/test/resources/io/github/htshame/rule/processor/no-underscores-in-attributes/";
    private static final String RULE_URL = BASE_FILE_PATH + "no-underscores-in-attributes-rule.xml";
    private static final String EXCLUSION_EMPTY_URL = BASE_FILE_PATH + "exclusions_empty.xml";
    private static final String EXCLUSION_WRONG_URL = BASE_FILE_PATH + "exclusions_wrong_yaml.xml";
    private static final String EXCLUSION_URL = BASE_FILE_PATH + "exclusions_yaml.xml";
    private static final String NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE = "no-underscores-in-attributes-failure.yaml";
    private static final String NO_UNDERSCORES_IN_ATTRIBUTES_SUCCESS = "no-underscores-in-attributes-success.yaml";

    /**
     * Default constructor.
     */
    public NoUnderscoresInAttributesProcessorYamlTest() {
        super(RULE_URL, RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES);
    }

    /**
     * Test getting the name.
     */
    @Test
    public void testGetName() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Element ruleElement = prepareRuleELement();

        // act
        RuleEnum actual = NoUnderscoresInAttributesProcessor.instantiate(ruleElement).getName();

        // assert
        assertEquals(RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES, actual);
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
                BASE_FILE_PATH + NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE,
                ChangeLogFormatEnum.YAML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_EMPTY_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_3",
                        "test",
                        List.of(
                                "Property [tableName] of key [createTable] contains underscore in value: [user_meta]",
                                "Property [name] of key [column] contains underscore in value: [user_data]")),
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test",
                        List.of("Property [tableName] of key [createIndex] contains underscore in value: "
                                        + "[user_metadata]",
                                "Property [indexName] of key [createIndex] contains underscore in value: [user_idx]",
                                "Property [name] of key [column] contains underscore in value: [external_user_id]",
                                "Property [tableName] of key [createTable] contains underscore in value: [user_meta]",
                                "Property [name] of key [column] contains underscore in value: [user_data]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoUnderscoresInAttributesProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE,
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
                BASE_FILE_PATH + NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE,
                ChangeLogFormatEnum.YAML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_WRONG_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_3",
                        "test",
                        List.of(
                                "Property [tableName] of key [createTable] contains underscore in value: [user_meta]",
                                "Property [name] of key [column] contains underscore in value: [user_data]")),
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test",
                        List.of("Property [tableName] of key [createIndex] contains underscore in value: "
                                        + "[user_metadata]",
                                "Property [indexName] of key [createIndex] contains underscore in value: [user_idx]",
                                "Property [name] of key [column] contains underscore in value: [external_user_id]",
                                "Property [tableName] of key [createTable] contains underscore in value: [user_meta]",
                                "Property [name] of key [column] contains underscore in value: [user_data]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoUnderscoresInAttributesProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE,
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
                BASE_FILE_PATH + NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE,
                ChangeLogFormatEnum.YAML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_URL));
        List<String> expectedErrorMessages = Collections.singletonList(
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test",
                        List.of("Property [tableName] of key [createIndex] contains underscore in value: "
                                        + "[user_metadata]",
                                "Property [indexName] of key [createIndex] contains underscore in value: [user_idx]",
                                "Property [name] of key [column] contains underscore in value: [external_user_id]",
                                "Property [tableName] of key [createTable] contains underscore in value: [user_meta]",
                                "Property [name] of key [column] contains underscore in value: [user_data]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoUnderscoresInAttributesProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE,
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
                BASE_FILE_PATH + NO_UNDERSCORES_IN_ATTRIBUTES_SUCCESS,
                ChangeLogFormatEnum.YAML);
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_URL));

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                NoUnderscoresInAttributesProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        NO_UNDERSCORES_IN_ATTRIBUTES_SUCCESS,
                        ChangeLogFormatEnum.YAML);
            } catch (ValidationException e) {
                isExceptionThrown = true;
            }
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
