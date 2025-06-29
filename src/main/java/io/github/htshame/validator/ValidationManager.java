package io.github.htshame.validator;

import io.github.htshame.change.log.ChangeLogParser;
import io.github.htshame.change.log.XmlChangeLogParser;
import io.github.htshame.change.log.YamlChangeLogParser;
import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.rule.Rule;
import io.github.htshame.util.parser.ExclusionParser;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is responsible for actual validation based on
 * provided rules XML file, exclusion XML file
 * and contents of the changeLog directory.
 */
public class ValidationManager {

    static final EnumMap<ChangeLogFormatEnum, ChangeLogParser> CHANGELOG_PARSER =
            new EnumMap<>(ChangeLogFormatEnum.class);

    static {
        CHANGELOG_PARSER.put(ChangeLogFormatEnum.XML, new XmlChangeLogParser());
        CHANGELOG_PARSER.put(ChangeLogFormatEnum.YAML, new YamlChangeLogParser());
        CHANGELOG_PARSER.put(ChangeLogFormatEnum.YML, new YamlChangeLogParser());
    }

    /**
     * Default constructor.
     */
    public ValidationManager() {

    }

    /**
     * Commence validation.
     *
     * @param changeLogFiles  - changeLog files to validate.
     * @param rules           - set of rules to validate against.
     * @param exclusionParser - exclusions.
     * @param changeLogFormat - changeLog format.
     * @return list of validation errors. Empty list if there are no errors.
     */
    public List<String> validate(final List<File> changeLogFiles,
                                 final List<Rule> rules,
                                 final ExclusionParser exclusionParser,
                                 final ChangeLogFormatEnum changeLogFormat) {
        List<String> validationErrors = new ArrayList<>();
        for (File changeLogFile : changeLogFiles) {
            Set<Rule> rulesToValidateWith = excludeRulesBasedOnExclusionFile(rules, exclusionParser, changeLogFile);
            validationErrors.addAll(
                    processValidation(changeLogFile, rulesToValidateWith, exclusionParser, changeLogFormat));
        }
        return validationErrors;
    }

    /**
     * Process a single changeLog file.
     *
     * @param changeLogFile   - changeLog file.
     * @param rules           - set of rules.
     * @param exclusionParser - exclusion parser.
     * @param changeLogFormat - changeLog format.
     * @return list of validation errors. Empty list of there are no errors.
     */
    private List<String> processValidation(final File changeLogFile,
                                           final Set<Rule> rules,
                                           final ExclusionParser exclusionParser,
                                           final ChangeLogFormatEnum changeLogFormat) {
        List<String> validationErrors = new ArrayList<>();
        try {
            List<ChangeSetElement> changeSetElements = CHANGELOG_PARSER.get(changeLogFormat)
                    .parseChangeLog(changeLogFile);

            for (Rule rule : rules) {
                for (ChangeSetElement changeSetElement : changeSetElements) {
                    try {
                        rule.validate(changeSetElement, exclusionParser, changeLogFile.getName(), changeLogFormat);
                    } catch (ValidationException e) {
                        validationErrors.add("[" + changeLogFile.getName() + "] " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            validationErrors.add("[" + changeLogFile.getName() + "] Failed to parse: " + e.getMessage());
            return validationErrors;
        }
        return validationErrors;
    }

    /**
     * Exclude rules based on the data from the exclusion file.
     *
     * @param rules           - set of specified rules.
     * @param exclusionParser - exclusion parser.
     * @param changeLogFile   - changeLog file.
     * @return set of rules to apply to the given changeLog file.
     */
    private Set<Rule> excludeRulesBasedOnExclusionFile(final List<Rule> rules,
                                                       final ExclusionParser exclusionParser,
                                                       final File changeLogFile) {
        Set<Rule> rulesToValidateWith = new HashSet<>();
        for (Rule rule : rules) {
            if (!exclusionParser.isFileExcluded(changeLogFile.getName(), rule.getName())) {
                rulesToValidateWith.add(rule);
            }
        }
        return rulesToValidateWith;
    }
}
