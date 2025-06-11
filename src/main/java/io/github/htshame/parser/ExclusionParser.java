package io.github.htshame.parser;

import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ExclusionParserException;
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
 * This class parses the exclusions XML file.
 * <p>Example:</p>
 * <pre><code>
 * &lt;exclusions&gt;
 *     &lt;exclusion fileName="changelog_03.xml" rule="tag-must-exist"/&gt;
 * &lt;/exclusions&gt;
 * </code></pre>
 */

public final class ExclusionParser {

    /**
     * Sets of excluded rules mapped with the changeLog file.
     */
    private final Map<String, Set<RuleEnum>> fileRuleExclusions = new HashMap<>();

    private ExclusionParser() {

    }

    /**
     * Parse the exclusionsFile with exclusions.
     *
     * @param exclusionsFile - exclusionsFile with exclusions.
     * @return exclusion parser.
     * @throws ExclusionParserException - thrown if parsing fails.
     */
    public static ExclusionParser parseExclusions(final File exclusionsFile) throws ExclusionParserException {
        if (exclusionsFile == null) {
            return new ExclusionParser();
        }
        try {
            ExclusionParser config = new ExclusionParser();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(exclusionsFile);

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
            throw new ExclusionParserException("Error parsing exclusion XML exclusionsFile");
        }
    }

    /**
     * Check whether the rule is excluded or not for the given file.
     *
     * @param fileName - changeLog file name.
     * @param ruleName - rule name.
     * @return <code>true</code> if excluded, <code>false</code> if not excluded.
     */
    public boolean isExcluded(final String fileName,
                              final RuleEnum ruleName) {
        Set<RuleEnum> excludedRules = fileRuleExclusions.get(fileName);
        return excludedRules != null && excludedRules.contains(ruleName);
    }

    /**
     * Exclusion XML tags and attributes.
     */
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
