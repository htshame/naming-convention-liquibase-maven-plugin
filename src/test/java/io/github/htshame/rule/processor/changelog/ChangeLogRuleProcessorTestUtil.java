package io.github.htshame.rule.processor.changelog;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.enums.RuleStructureEnum;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ChangeLogRuleProcessorTestUtil {

    private final String ruleFilePath;
    private final String changeLogSuccessFile;
    private final String changeLogFailureFile;

    /**
     * Constructor.
     *
     * @param ruleName - rule name.
     */
    public ChangeLogRuleProcessorTestUtil(final RuleEnum ruleName) {
        String baseFilePath =
                "src/test/resources/io/github/htshame/rule/processor/changelog/" + ruleName.getValue() + "/";
        this.ruleFilePath = baseFilePath + ruleName.getValue() + "-rule.xml";
        this.changeLogSuccessFile = baseFilePath + ruleName.getValue() + "-success.xml";
        this.changeLogFailureFile = baseFilePath + ruleName.getValue() + "-failure.xml";
    }

    protected Element prepareRuleELement() throws ParserConfigurationException, IOException, SAXException {
        Document ruleDocument = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File(ruleFilePath));
        NodeList ruleNodes = ruleDocument.getElementsByTagName(RuleStructureEnum.RULE.getValue());
        return (Element) ruleNodes.item(0);
    }

    protected File getChangeLogSuccessFile() {
        return new File(changeLogSuccessFile);
    }

    protected File getChangeLogFailureFile() {
        return new File(changeLogFailureFile);
    }

    protected File getChangeLogFailureFileNotFound() {
        return new File(changeLogFailureFile + "1");
    }
}
