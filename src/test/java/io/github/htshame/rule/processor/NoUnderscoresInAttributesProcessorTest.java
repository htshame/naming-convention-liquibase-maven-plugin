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

public class NoUnderscoresInAttributesProcessorTest extends RuleProcessorTest {

    private static final String BASE_FILE_PATH =
            "src/test/resources/io/github/htshame/rule/processor/no-underscores-in-attributes/";
    private static final String RULE_URL = BASE_FILE_PATH + "no-underscores-in-attributes-rule.xml";
    private static final String EXCLUSION_EMPTY_URL = BASE_FILE_PATH + "exclusions_empty.xml";
    private static final String EXCLUSION_WRONG_URL = BASE_FILE_PATH + "exclusions_wrong.xml";
    private static final String EXCLUSION_URL = BASE_FILE_PATH + "exclusions.xml";
    private static final String NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE_XML = "no-underscores-in-attributes-failure.xml";
    private static final String NO_UNDERSCORES_IN_ATTRIBUTES_SUCCESS_XML = "no-underscores-in-attributes-success.xml";

    /**
     * Default constructor.
     */
    public NoUnderscoresInAttributesProcessorTest() {
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
        RuleEnum actual = NoUnderscoresInAttributesProcessor.fromXml(ruleElement).getName();

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
            ExclusionParserException {
        // arrange
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File(BASE_FILE_PATH + NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE_XML));
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        NodeList changeSetList = document.getElementsByTagName(CHANGE_SET_TAG_NAME);
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_EMPTY_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                "ChangeSet: id=\"changelog_02_3\", author=\"test\"."
                        + " Rule: [" + RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES.getValue() + "]. "
                        + "Attribute indexName in element <createIndex> contains underscore in value: "
                        + "[user_metadata_external_user_id_unique_idx].",
                "ChangeSet: id=\"changelog_02_4\", author=\"test\"."
                        + " Rule: [" + RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES.getValue() + "]. "
                        + "Attribute indexName in element <createIndex> contains underscore in value: "
                        + "[user_metadata_external_user_id_unique_idx].");
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (int i = 0; i < changeSetList.getLength(); i++) {
            try {
                Element changeSetElement = (Element) changeSetList.item(i);
                NoUnderscoresInAttributesProcessor.fromXml(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE_XML);
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
                .parse(new File(BASE_FILE_PATH + NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE_XML));
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        NodeList changeSetList = document.getElementsByTagName(CHANGE_SET_TAG_NAME);
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_WRONG_URL));
        List<String> expectedErrorMessages = Arrays.asList(
                "ChangeSet: id=\"changelog_02_3\", author=\"test\"."
                        + " Rule: [" + RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES.getValue() + "]. "
                        + "Attribute indexName in element <createIndex> contains underscore in value: "
                        + "[user_metadata_external_user_id_unique_idx].",
                "ChangeSet: id=\"changelog_02_4\", author=\"test\"."
                        + " Rule: [" + RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES.getValue() + "]. "
                        + "Attribute indexName in element <createIndex> contains underscore in value: "
                        + "[user_metadata_external_user_id_unique_idx].");
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (int i = 0; i < changeSetList.getLength(); i++) {
            try {
                Element changeSetElement = (Element) changeSetList.item(i);
                NoUnderscoresInAttributesProcessor.fromXml(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE_XML);
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
                .parse(new File(BASE_FILE_PATH + NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE_XML));
        int exceptionCount = 0;
        Element ruleElement = prepareRuleELement();
        NodeList changeSetList = document.getElementsByTagName(CHANGE_SET_TAG_NAME);
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_URL));
        List<String> expectedErrorMessages = List.of(
                "ChangeSet: id=\"changelog_02_4\", author=\"test\"."
                        + " Rule: [" + RuleEnum.NO_UNDERSCORES_IN_ATTRIBUTES.getValue() + "]. "
                        + "Attribute indexName in element <createIndex> contains underscore in value: "
                        + "[user_metadata_external_user_id_unique_idx].");
        List<String> actualErrorMessages = new ArrayList<>();

        // act
        for (int i = 0; i < changeSetList.getLength(); i++) {
            try {
                Element changeSetElement = (Element) changeSetList.item(i);
                NoUnderscoresInAttributesProcessor.fromXml(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        NO_UNDERSCORES_IN_ATTRIBUTES_FAILURE_XML);
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
                .parse(new File(BASE_FILE_PATH + NO_UNDERSCORES_IN_ATTRIBUTES_SUCCESS_XML));
        boolean isExceptionThrown = false;
        Element ruleElement = prepareRuleELement();
        NodeList changeSetList = document.getElementsByTagName(CHANGE_SET_TAG_NAME);
        ExclusionParser exclusionParser = ExclusionParser.parseExclusions(new File(EXCLUSION_URL));

        // act
        for (int i = 0; i < changeSetList.getLength(); i++) {
            try {
                Element changeSetElement = (Element) changeSetList.item(i);

                NoUnderscoresInAttributesProcessor.fromXml(ruleElement).validate(
                        changeSetElement,
                        exclusionParser,
                        NO_UNDERSCORES_IN_ATTRIBUTES_SUCCESS_XML);
            } catch (ValidationException e) {
                isExceptionThrown = true;
            }
        }

        // assert
        assertFalse(isExceptionThrown);
    }
}
