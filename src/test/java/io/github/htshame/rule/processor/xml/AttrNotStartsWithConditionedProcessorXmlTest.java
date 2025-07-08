package io.github.htshame.rule.processor.xml;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.processor.AttrNotStartsWithConditionedProcessor;
import io.github.htshame.rule.processor.RuleProcessorTestUtil;
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

public class AttrNotStartsWithConditionedProcessorXmlTest extends RuleProcessorTestUtil {

    private static final RuleEnum RULE_ENUM = RuleEnum.ATTRIBUTE_NOT_STARTS_WITH_CONDITIONED;
    private static final ChangeLogFormatEnum CHANGELOG_FORMAT = ChangeLogFormatEnum.XML;

    /**
     * Default constructor.
     */
    public AttrNotStartsWithConditionedProcessorXmlTest() {
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
        RuleEnum actual = AttrNotStartsWithConditionedProcessor.instantiate(ruleElement).getName();

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
                        List.of("Tag <createIndex> with unique=\"true\" must not have [indexName] "
                                + "starting with [idx_unique_], "
                                + "but found: [idx_unique_user_metadata_external_user_id]")),
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test",
                        List.of("Tag <createIndex> with unique=\"true\" must not have [indexName] "
                                + "starting with [idx_unique_], "
                                + "but found: [idx_unique_1user_metadata_external_user_id]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                AttrNotStartsWithConditionedProcessor.instantiate(ruleElement).validateChangeSet(
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
                        List.of("Tag <createIndex> with unique=\"true\" must not have [indexName] "
                                + "starting with [idx_unique_], "
                                + "but found: [idx_unique_user_metadata_external_user_id]")),
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test",
                        List.of("Tag <createIndex> with unique=\"true\" must not have [indexName] "
                                + "starting with [idx_unique_], "
                                + "but found: [idx_unique_1user_metadata_external_user_id]")));

        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                AttrNotStartsWithConditionedProcessor.instantiate(ruleElement).validateChangeSet(
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
        List<ChangeSetElement> changeSetElements = parseChangeSetFile(
                getBaseUrlPathFormatted() + getChangelogFailureFile(),
                CHANGELOG_FORMAT);
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionUrl()));
        List<String> expectedErrorMessages = List.of(
                prepareTestErrorMessage(
                        "changelog_02_4",
                        "test",
                        List.of("Tag <createIndex> with unique=\"true\" must not have [indexName] "
                                + "starting with [idx_unique_], "
                                + "but found: [idx_unique_1user_metadata_external_user_id]")));
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                AttrNotStartsWithConditionedProcessor.instantiate(ruleElement).validateChangeSet(
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
        List<ChangeSetElement> changeSetElements = parseChangeSetFile(
                getBaseUrlPathFormatted() + getChangelogSuccessFile(),
                CHANGELOG_FORMAT);
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(getExclusionEmptyUrl()));

        // act
        for (ChangeSetElement changeSetElement : changeSetElements) {
            try {
                AttrNotStartsWithConditionedProcessor.instantiate(ruleElement).validateChangeSet(
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
