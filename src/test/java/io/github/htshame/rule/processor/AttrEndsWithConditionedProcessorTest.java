package io.github.htshame.rule.processor;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.htshame.util.ChangeSetUtil.CHANGE_SET_TAG_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttrEndsWithConditionedProcessorTest extends RuleProcessorTestUtil {

    private static final String BASE_FILE_PATH =
            "src/test/resources/io/github/htshame/rule/processor/attr-ends-with-conditioned/";
    private static final String RULE_URL = BASE_FILE_PATH + "attr-ends-with-conditioned-rule.xml";
    private static final String EXCLUSION_EMPTY_URL = BASE_FILE_PATH + "exclusions_empty.xml";
    private static final String EXCLUSION_WRONG_URL = BASE_FILE_PATH + "exclusions_wrong.xml";
    private static final String EXCLUSION_URL = BASE_FILE_PATH + "exclusions.xml";
    private static final String ATTR_ENDS_WITH_CONDITIONED_FAILURE_XML = "attr-ends-with-conditioned-failure.xml";
    private static final String ATTR_ENDS_WITH_CONDITIONED_SUCCESS_XML = "attr-ends-with-conditioned-success.xml";

    /**
     * Default constructor.
     */
    public AttrEndsWithConditionedProcessorTest() {
        super(RULE_URL);
    }

    /**
     * Test getting the name.
     */
    @Test
    public void testGetName() throws ParserConfigurationException, IOException, SAXException {
        // arrange
        Element ruleElement = prepareRuleELement();

        // act
        RuleEnum actual = AttrEndsWithConditionedProcessor.fromXml(ruleElement).getName();

        // assert
        assertEquals(RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED, actual);
    }

    /**
     * Test failed validation.
     */
    @Test
    public void testValidateFailure() throws ParserConfigurationException,
            IOException,
            SAXException,
            ExclusionParserException {
        // arrange
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File(BASE_FILE_PATH + ATTR_ENDS_WITH_CONDITIONED_FAILURE_XML));
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        NodeList changeSetList = document.getElementsByTagName(CHANGE_SET_TAG_NAME);
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_EMPTY_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                "ChangeSet: id=\"changelog_02_3\", author=\"test\". "
                        + "Rule [" + RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED.getValue() + "]\n"
                        + "Tag <createIndex> with unique=\"true\" must have indexName ending with [_unique], "
                        + "but found: [user_metadata_external_user_id_unique_idx]",
                "ChangeSet: id=\"changelog_02_4\", author=\"test\". "
                        + "Rule [" + RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED.getValue() + "]\n"
                        + "Tag <createIndex> with unique=\"true\" must have indexName ending with [_unique], "
                        + "but found: [user_metadata_external_user_id_unique_idx1]");
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (int i = 0; i < changeSetList.getLength(); i++) {
            try {
                Element changeSetElement = (Element) changeSetList.item(i);

                AttrEndsWithConditionedProcessor.fromXml(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        ATTR_ENDS_WITH_CONDITIONED_FAILURE_XML);
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
            ExclusionParserException {
        // arrange
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File(BASE_FILE_PATH + ATTR_ENDS_WITH_CONDITIONED_FAILURE_XML));
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        NodeList changeSetList = document.getElementsByTagName(CHANGE_SET_TAG_NAME);
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_WRONG_URL));

        List<String> expectedErrorMessages = Arrays.asList(
                "ChangeSet: id=\"changelog_02_3\", author=\"test\". "
                        + "Rule [" + RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED.getValue() + "]\n"
                        + "Tag <createIndex> with unique=\"true\" must have indexName ending with [_unique], "
                        + "but found: [user_metadata_external_user_id_unique_idx]",
                "ChangeSet: id=\"changelog_02_4\", author=\"test\". "
                        + "Rule [" + RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED.getValue() + "]\n"
                        + "Tag <createIndex> with unique=\"true\" must have indexName ending with [_unique], "
                        + "but found: [user_metadata_external_user_id_unique_idx1]");
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (int i = 0; i < changeSetList.getLength(); i++) {
            try {
                Element changeSetElement = (Element) changeSetList.item(i);

                AttrEndsWithConditionedProcessor.fromXml(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        ATTR_ENDS_WITH_CONDITIONED_FAILURE_XML);
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
            ExclusionParserException {
        // arrange
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File(BASE_FILE_PATH + ATTR_ENDS_WITH_CONDITIONED_FAILURE_XML));
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        NodeList changeSetList = document.getElementsByTagName(CHANGE_SET_TAG_NAME);
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_URL));
        List<String> expectedErrorMessages = List.of(
                "ChangeSet: id=\"changelog_02_4\", author=\"test\". "
                        + "Rule [" + RuleEnum.ATTRIBUTE_ENDS_WITH_CONDITIONED.getValue() + "]\n"
                        + "Tag <createIndex> with unique=\"true\" must have indexName ending with [_unique], "
                        + "but found: [user_metadata_external_user_id_unique_idx1]");
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (int i = 0; i < changeSetList.getLength(); i++) {
            try {
                Element changeSetElement = (Element) changeSetList.item(i);

                AttrEndsWithConditionedProcessor.fromXml(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        ATTR_ENDS_WITH_CONDITIONED_FAILURE_XML);
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
            ExclusionParserException {
        // arrange
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File(BASE_FILE_PATH + ATTR_ENDS_WITH_CONDITIONED_SUCCESS_XML));
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();
        NodeList changeSetList = document.getElementsByTagName(CHANGE_SET_TAG_NAME);
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_EMPTY_URL));

        // act
        for (int i = 0; i < changeSetList.getLength(); i++) {
            try {
                Element changeSetElement = (Element) changeSetList.item(i);

                AttrEndsWithConditionedProcessor.fromXml(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        ATTR_ENDS_WITH_CONDITIONED_SUCCESS_XML);
            } catch (ValidationException e) {
                isExceptionThrown = true;
            }
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
