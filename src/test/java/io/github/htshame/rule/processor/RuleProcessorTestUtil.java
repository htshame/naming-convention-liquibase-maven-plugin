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

public class RuleProcessorTestUtil {

    private final String ruleFilePath;
    private final RuleEnum ruleName;

    /**
     * Constructor.
     *
     * @param ruleFilePath - path to the rule file.
     * @param ruleName     - rule name.
     */
    public RuleProcessorTestUtil(final String ruleFilePath,
                                 final RuleEnum ruleName) {
        this.ruleFilePath = ruleFilePath;
        this.ruleName = ruleName;
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
}
