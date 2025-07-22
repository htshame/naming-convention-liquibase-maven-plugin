package io.github.htshame.parser;

import io.github.htshame.dto.ChangeSetExclusionDto;
import io.github.htshame.enums.ExclusionTypeEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.parser.exclusion.ChangeSetExclusionHandler;
import io.github.htshame.parser.exclusion.ExclusionHandler;
import io.github.htshame.parser.exclusion.FileExclusionHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
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

    /**
     * Exclusion type map.
     */
    private static final EnumMap<ExclusionTypeEnum, ExclusionHandler> EXCLUSION_TYPE_MAP =
            new EnumMap<>(ExclusionTypeEnum.class);

    static {
        EXCLUSION_TYPE_MAP.put(ExclusionTypeEnum.FILE_EXCLUSION, new FileExclusionHandler());
        EXCLUSION_TYPE_MAP.put(ExclusionTypeEnum.CHANGESET_EXCLUSION, new ChangeSetExclusionHandler());
    }

    /**
     * Default constructor.
     */
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
        ExclusionParser parser = new ExclusionParser();
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(exclusionsFile);

            NodeList elements = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < elements.getLength(); i++) {
                Node node = elements.item(i);
                if (!(node instanceof Element)) {
                    continue;
                }
                Element element = (Element) node;
                EXCLUSION_TYPE_MAP.get(ExclusionTypeEnum.fromTypeName(element.getTagName())).handle(element, parser);
            }
            return parser;
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
     * Get rule exclusions applied to files.
     *
     * @return rule exclusions applied to files.
     */
    public Map<String, Set<RuleEnum>> getFileRuleExclusions() {
        return fileRuleExclusions;
    }

    /**
     * Get rule exclusions applied to changeSets.
     *
     * @return rule exclusions applied to changeSets.
     */
    public Map<ChangeSetExclusionDto, Set<RuleEnum>> getChangeSetRuleExclusions() {
        return changeSetRuleExclusions;
    }

}
