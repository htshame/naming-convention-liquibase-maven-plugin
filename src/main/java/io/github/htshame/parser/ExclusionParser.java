package io.github.htshame.parser;

import io.github.htshame.dto.ChangeSetExclusionDto;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ExclusionParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class parses the exclusions XML file.
 * <p>Example:</p>
 * <pre><code>
 * &lt;exclusions&gt;
 *     &lt;fileExclusion fileName="changelog_03.xml" rule="tag-must-exist"/&gt;
 * &lt;/exclusions&gt;
 * </code></pre>
 */
public final class ExclusionParser {

    /**
     * Sets of excluded rules mapped with the changeLog file.
     */
    private final Map<String, Set<RuleEnum>> fileRuleExclusions = new HashMap<>();

    /**
     * Sets of excluded rules mapped by the excluded changeSet.
     */
    private final Map<ChangeSetExclusionDto, Set<RuleEnum>> changeSetRuleExclusions = new HashMap<>();

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

            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(exclusionsFile);

            NodeList fileExclusions = document.getElementsByTagName(ExclusionEnum.FILE_EXCLUSION_TAG.getValue());
            for (int i = 0; i < fileExclusions.getLength(); i++) {
                Element exclusion = (Element) fileExclusions.item(i);
                String fileName = exclusion.getAttribute(ExclusionEnum.FILE_NAME_ATTR.getValue()).trim();
                String rule = exclusion.getAttribute(ExclusionEnum.RULE_ATTR.getValue()).trim();
                RuleEnum ruleEnum = RuleEnum.fromValue(rule);

                Set<RuleEnum> rules = config.fileRuleExclusions.computeIfAbsent(fileName, k -> new HashSet<>());
                if (RuleEnum.ALL_RULES.equals(ruleEnum)) {
                    rules.addAll(Arrays.asList(RuleEnum.values()));
                } else {
                    rules.add(ruleEnum);
                }
            }

            NodeList changeSetExclusions =
                    document.getElementsByTagName(ExclusionEnum.CHANGESET_EXCLUSION_TAG.getValue());
            for (int i = 0; i < changeSetExclusions.getLength(); i++) {
                Element exclusion = (Element) changeSetExclusions.item(i);
                String fileName = exclusion.getAttribute(ExclusionEnum.FILE_NAME_ATTR.getValue()).trim();
                String rule = exclusion.getAttribute(ExclusionEnum.RULE_ATTR.getValue()).trim();
                String changeSetId = exclusion.getAttribute(ExclusionEnum.CHANGESET_ID_ATTR.getValue()).trim();
                String changeSetAuthor = exclusion.getAttribute(ExclusionEnum.CHANGESET_AUTHOR_ATTR.getValue()).trim();

                config.changeSetRuleExclusions
                        .computeIfAbsent(
                                new ChangeSetExclusionDto(fileName, changeSetId, changeSetAuthor),
                                k -> new HashSet<>())
                        .add(RuleEnum.fromValue(rule));
            }
            return config;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new ExclusionParserException("Error parsing exclusion XML file");
        }
    }

    /**
     * Check whether the rule is excluded or not for the given file.
     *
     * @param fileName - changeLog file name.
     * @param ruleName - rule name.
     * @return <code>true</code> if excluded, <code>false</code> if not excluded.
     */
    public boolean isFileExcluded(final String fileName,
                                  final RuleEnum ruleName) {
        Set<RuleEnum> excludedRules = fileRuleExclusions.get(fileName);
        return excludedRules != null && excludedRules.contains(ruleName);
    }

    /**
     * Check whether the rule is excluded or not for the given changeSet.
     *
     * @param fileName        - changeLog file name.
     * @param changeSetId     - changeSet id.
     * @param changeSetAuthor - changeSet author.
     * @param ruleName        - rule name.
     * @return <code>true</code> if excluded, <code>false</code> if not excluded.
     */
    public boolean isChangeSetExcluded(final String fileName,
                                       final String changeSetId,
                                       final String changeSetAuthor,
                                       final RuleEnum ruleName) {
        Set<RuleEnum> excludedRules = changeSetRuleExclusions.get(
                new ChangeSetExclusionDto(fileName, changeSetId, changeSetAuthor));
        return excludedRules != null && excludedRules.contains(ruleName);
    }

    /**
     * Exclusion XML tags and attributes.
     */
    private enum ExclusionEnum {
        FILE_EXCLUSION_TAG("fileExclusion"),
        CHANGESET_EXCLUSION_TAG("changeSetExclusion"),
        FILE_NAME_ATTR("fileName"),
        CHANGESET_ID_ATTR("changeSetId"),
        CHANGESET_AUTHOR_ATTR("changeSetAuthor"),
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
