package io.github.htshame.rule.processor.xml;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.rule.processor.AttrEndsWithProcessor;
import io.github.htshame.rule.processor.RuleProcessorTestUtil;
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

public class AttrEndsWithProcessorXmlTest extends RuleProcessorTestUtil {

    private static final String BASE_FILE_PATH = "src/test/resources/io/github/htshame/rule/processor/attr-ends-with/";
    private static final String RULE_URL = BASE_FILE_PATH + "attr-ends-with-rule.xml";
    private static final String EXCLUSION_EMPTY_URL = BASE_FILE_PATH + "exclusions_empty.xml";
    private static final String EXCLUSION_WRONG_URL = BASE_FILE_PATH + "exclusions_wrong_xml.xml";
    private static final String EXCLUSION_URL = BASE_FILE_PATH + "exclusions_xml.xml";
    private static final String ATTR_ENDS_WITH_FAILURE_XML = "attr-ends-with-failure.xml";
    private static final String ATTR_ENDS_WITH_SUCCESS_XML = "attr-ends-with-success.xml";

    /**
     * Default constructor.
     */
    public AttrEndsWithProcessorXmlTest() {
        super(RULE_URL, RuleEnum.ATTRIBUTE_ENDS_WITH);
    }

    /**
     * Test getting the name.
     */
    @Test
    public void testGetName() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Element ruleElement = prepareRuleELement();

        // act
        RuleEnum actual = AttrEndsWithProcessor.instantiate(ruleElement).getName();

        // assert
        assertEquals(RuleEnum.ATTRIBUTE_ENDS_WITH, actual);
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
                BASE_FILE_PATH + ATTR_ENDS_WITH_FAILURE_XML,
                ChangeLogFormatEnum.XML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_EMPTY_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_1",
                        "test",
                        List.of("Element [addForeignKeyConstraint] must have [constraintName] ending with [_fk], "
                                + "but found: [fk_user_activation_user_profile_id_user_profile_id]")),
                prepareTestErrorMessage(
                        "changelog_02_2",
                        "test",
                        List.of("Element [addForeignKeyConstraint] must have [constraintName] ending with [_fk], "
                                + "but found: [user_activation_user_profile_id_user_profile_id_FK]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                AttrEndsWithProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        ATTR_ENDS_WITH_FAILURE_XML);
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
                BASE_FILE_PATH + ATTR_ENDS_WITH_FAILURE_XML,
                ChangeLogFormatEnum.XML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_WRONG_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_1",
                        "test",
                        List.of("Element [addForeignKeyConstraint] must have [constraintName] ending with [_fk], "
                                + "but found: [fk_user_activation_user_profile_id_user_profile_id]")),
                prepareTestErrorMessage(
                        "changelog_02_2",
                        "test",
                        List.of("Element [addForeignKeyConstraint] must have [constraintName] ending with [_fk], "
                                + "but found: [user_activation_user_profile_id_user_profile_id_FK]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                AttrEndsWithProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        ATTR_ENDS_WITH_FAILURE_XML);
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
                BASE_FILE_PATH + ATTR_ENDS_WITH_FAILURE_XML,
                ChangeLogFormatEnum.XML);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_URL));
        List<String> expectedErrorMessages = Collections.singletonList(
                prepareTestErrorMessage(
                        "changelog_02_2",
                        "test",
                        List.of("Element [addForeignKeyConstraint] must have [constraintName] ending with [_fk], "
                                + "but found: [user_activation_user_profile_id_user_profile_id_FK]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                AttrEndsWithProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        ATTR_ENDS_WITH_FAILURE_XML);
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
                BASE_FILE_PATH + ATTR_ENDS_WITH_SUCCESS_XML,
                ChangeLogFormatEnum.XML);
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_EMPTY_URL));

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                AttrEndsWithProcessor.instantiate(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        ATTR_ENDS_WITH_SUCCESS_XML);
            } catch (ValidationException e) {
                isExceptionThrown = true;
            }
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
