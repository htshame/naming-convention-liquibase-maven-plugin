package io.github.htshame.rule.processor;

import io.github.htshame.enums.RuleStructureEnum;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class RuleProcessorTest {

    private final String ruleFilePath;

    /**
     * Constructor.
     *
     * @param ruleFilePath - path to the rule file.
     */
    public RuleProcessorTest(final String ruleFilePath) {
        this.ruleFilePath = ruleFilePath;
    }

    Element prepareRuleELement() throws ParserConfigurationException, IOException, SAXException {
        Document ruleDocument = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File(ruleFilePath));
        NodeList ruleNodes = ruleDocument.getElementsByTagName(RuleStructureEnum.RULE.getValue());
        return (Element) ruleNodes.item(0);
    }
}
