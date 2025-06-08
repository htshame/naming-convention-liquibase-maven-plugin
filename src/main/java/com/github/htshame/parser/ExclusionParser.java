package com.github.htshame.parser;

import com.github.htshame.enums.RuleEnum;
import com.github.htshame.exception.ExclusionParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class parses the exclusion XML file.
 * E.g.:
 * <pre><code>
 *      <exclusions>
 *          <exclusion fileName="changelog_03.xml" rule="tag-must-exist"/>
 *      </exclusions>
 * </code></pre>
 */
public final class ExclusionParser {

    private final Map<String, Set<RuleEnum>> fileRuleExclusions = new HashMap<>();

    public static ExclusionParser parseExclusions(File file) throws ExclusionParserException {
        if (file == null) {
            return new ExclusionParser();
        }
        try {
            ExclusionParser config = new ExclusionParser();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);

            NodeList exclusions = doc.getElementsByTagName(ExclusionEnum.EXCLUSION_TAG.getValue());
            for (int i = 0; i < exclusions.getLength(); i++) {
                Element exclusion = (Element) exclusions.item(i);
                String fileName = exclusion.getAttribute(ExclusionEnum.FILE_NAME_ATTR.getValue()).trim();
                String rule = exclusion.getAttribute(ExclusionEnum.RULE_ATTR.getValue()).trim();

                config.fileRuleExclusions
                        .computeIfAbsent(fileName, k -> new HashSet<>())
                        .add(RuleEnum.fromValue(rule));
            }
            return config;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new ExclusionParserException("Error parsing exclusion XML file");
        }
    }

    public boolean isExcluded(String fileName, RuleEnum ruleName) {
        Set<RuleEnum> excludedRules = fileRuleExclusions.get(fileName);
        return excludedRules != null && excludedRules.contains(ruleName);
    }

    private enum ExclusionEnum {
        EXCLUSION_TAG("exclusion"),
        FILE_NAME_ATTR("fileName"),
        RULE_ATTR("rule");

        private final String value;

        ExclusionEnum(final String value) {
            this.value = value;
        }

        private String getValue() {
            return value;
        }
    }
}
