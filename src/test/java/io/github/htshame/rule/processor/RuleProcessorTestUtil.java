package io.github.htshame.rule.processor;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.validator.ValidatorTestUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RuleProcessorTestUtil {

    private final String ruleFilePath;
    private final RuleEnum ruleName;
    private final String changelogFailureFile;
    private final String changelogSuccessFile;
    private final String exclusionEmptyUrl;
    private final String baseUrlPathFormatted;
    private final String exclusionWrongUrl;
    private final String exclusionUrl;

    /**
     * Constructor.
     *
     * @param ruleName        - rule name.
     * @param changeLogFormat - changeLog format.
     */
    public RuleProcessorTestUtil(final RuleEnum ruleName,
                                 final ChangeLogFormatEnum changeLogFormat) {
        this.ruleName = ruleName;
        this.changelogFailureFile = ruleName.getValue() + "-failure." + changeLogFormat.getValue();
        this.changelogSuccessFile = ruleName.getValue() + "-success." + changeLogFormat.getValue();
        String baseFilePath = "src/test/resources/io/github/htshame/rule/processor/" + ruleName.getValue() + "/";
        this.ruleFilePath = baseFilePath + ruleName.getValue() + "-rule.xml";
        this.baseUrlPathFormatted = baseFilePath + "/" + changeLogFormat.getValue() + "/";
        this.exclusionWrongUrl = baseUrlPathFormatted + "exclusions_wrong_" + changeLogFormat.getValue() + ".xml";
        this.exclusionUrl = baseUrlPathFormatted + "exclusions_" + changeLogFormat.getValue() + ".xml";
        this.exclusionEmptyUrl = baseFilePath + "exclusions_empty.xml";
    }


    protected Element prepareRuleELement() throws ParserConfigurationException, IOException, SAXException {
        Document ruleDocument = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File(ruleFilePath));
        NodeList ruleNodes = ruleDocument.getElementsByTagName(RuleStructureEnum.RULE.getValue());
        return (Element) ruleNodes.item(0);
    }

    protected List<ChangeSetElement> parseChangeSetFile(final String filePath,
                                                        final ChangeLogFormatEnum format)
            throws ChangeLogParseException {
        File changeLogFile = new File(filePath);
        return ValidatorTestUtil.getParser(format).parseChangeLog(changeLogFile);
    }

    protected String prepareTestErrorMessage(final String changeSetId,
                                             final String changeSetAuthor,
                                             final List<String> errors) {
        return String.format("ChangeSet: id=\"%s\", author=\"%s\". Rule [%s]\n    %s",
                changeSetId,
                changeSetAuthor,
                ruleName.getValue(),
                String.join("\n    ", errors));
    }

    protected void assertErrors(final List<String> expectedErrorMessages,
                                final List<String> actualErrorMessages) {
        for (int i = 0; i < expectedErrorMessages.size(); i++) {
            assertEquals(expectedErrorMessages.get(i), actualErrorMessages.get(i));
        }
    }

    public String getChangelogFailureFile() {
        return changelogFailureFile;
    }

    public String getChangelogSuccessFile() {
        return changelogSuccessFile;
    }

    public String getExclusionEmptyUrl() {
        return exclusionEmptyUrl;
    }

    public String getBaseUrlPathFormatted() {
        return baseUrlPathFormatted;
    }

    public String getExclusionWrongUrl() {
        return exclusionWrongUrl;
    }

    public String getExclusionUrl() {
        return exclusionUrl;
    }
}
