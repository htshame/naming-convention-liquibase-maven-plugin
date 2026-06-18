package io.github.htshame.rule.processor.changeset.xml;

import io.github.htshame.change.element.ChangeLogElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.processor.changeset.AttrEndsWithProcessor;
import io.github.htshame.rule.processor.changeset.ChangeSetRuleProcessorTestUtil;
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

public class AttrEndsWithProcessorXmlTest extends ChangeSetRuleProcessorTestUtil {

    private static final RuleEnum RULE_ENUM = RuleEnum.ATTRIBUTE_ENDS_WITH;
    private static final ChangeLogFormatEnum CHANGELOG_FORMAT = ChangeLogFormatEnum.XML;

    /**
     * Default constructor.
     */
    public AttrEndsWithProcessorXmlTest() {
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
        RuleEnum actual = AttrEndsWithProcessor.instantiate(ruleElement).getName();

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
            ExclusionParserException,
            ChangeLogParseException {
        // arrange
        List<ChangeLogElement> changeSetElements = parseChangeSetFile(
                getBaseUrlPathFormatted() + getChangelogFailureFile(),
                CHANGELOG_FORMAT);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionEmptyUrl()));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_1",
                        "test",
                        List.of("Tag <addForeignKeyConstraint> must have [constraintName] ending with [_fk], "
                                + "but found: [fk_user_activation_user_profile_id_user_profile_id]")),
                prepareTestErrorMessage(
                        "changelog_02_2",
                        "test",
                        List.of("Tag <addForeignKeyConstraint> must have [constraintName] ending with [_fk], "
                                + "but found: [user_activation_user_profile_id_user_profile_id_FK]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeLogElement changeSetElement : changeSetElements) {
            try {
                AttrEndsWithProcessor.instantiate(ruleElement).validateChangeSet(
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
    public void testValidateChangeSetFailureWrongExclusion() throws ParserConfigurationException,
            IOException,
            SAXException,
            ExclusionParserException,
            ChangeLogParseException {
        // arrange
        List<ChangeLogElement> changeSetElements = parseChangeSetFile(
                getBaseUrlPathFormatted() + getChangelogFailureFile(),
                CHANGELOG_FORMAT);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionWrongUrl()));
        List<String> expectedErrorMessages = Arrays.asList(
                prepareTestErrorMessage(
                        "changelog_02_1",
                        "test",
                        List.of("Tag <addForeignKeyConstraint> must have [constraintName] ending with [_fk], "
                                + "but found: [fk_user_activation_user_profile_id_user_profile_id]")),
                prepareTestErrorMessage(
                        "changelog_02_2",
                        "test",
                        List.of("Tag <addForeignKeyConstraint> must have [constraintName] ending with [_fk], "
                                + "but found: [user_activation_user_profile_id_user_profile_id_FK]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeLogElement changeSetElement : changeSetElements) {
            try {
                AttrEndsWithProcessor.instantiate(ruleElement).validateChangeSet(
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
            ExclusionParserException,
            ChangeLogParseException {
        // arrange
        List<ChangeLogElement> changeSetElements = parseChangeSetFile(
                getBaseUrlPathFormatted() + getChangelogFailureFile(),
                CHANGELOG_FORMAT);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionUrl()));
        List<String> expectedErrorMessages = Collections.singletonList(
                prepareTestErrorMessage(
                        "changelog_02_2",
                        "test",
                        List.of("Tag <addForeignKeyConstraint> must have [constraintName] ending with [_fk], "
                                + "but found: [user_activation_user_profile_id_user_profile_id_FK]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeLogElement changeSetElement : changeSetElements) {
            try {
                AttrEndsWithProcessor.instantiate(ruleElement).validateChangeSet(
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
    public void testValidateChangeSetSuccess() throws ParserConfigurationException,
            IOException,
            SAXException,
            ExclusionParserException,
            ChangeLogParseException {
        // arrange
        List<ChangeLogElement> changeSetElements = parseChangeSetFile(
                getBaseUrlPathFormatted() + getChangelogSuccessFile(),
                CHANGELOG_FORMAT);
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionEmptyUrl()));

        // act
        for (ChangeLogElement changeSetElement : changeSetElements) {
            try {
                AttrEndsWithProcessor.instantiate(ruleElement).validateChangeSet(
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
