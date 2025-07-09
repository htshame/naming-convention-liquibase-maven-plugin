package io.github.htshame.validator;

import io.github.htshame.change.log.ChangeLogParser;
import io.github.htshame.change.log.JsonChangeLogParser;
import io.github.htshame.change.log.XmlChangeLogParser;
import io.github.htshame.change.log.YamlChangeLogParser;
import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.RuleTypeEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.exception.ValidationException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.ChangeLogRule;
import io.github.htshame.rule.ChangeSetRule;
import io.github.htshame.rule.Rule;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

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
        CHANGELOG_PARSER.put(ChangeLogFormatEnum.JSON, new JsonChangeLogParser());
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

            Set<ChangeSetRule> changeSetRules = new HashSet<>();
            Set<ChangeLogRule> changeLogRules = new HashSet<>();

            Map<RuleTypeEnum, Consumer<Rule>> ruleTypeMap = Map.of(
                    RuleTypeEnum.CHANGE_SET_RULE, rule -> changeSetRules.add((ChangeSetRule) rule),
                    RuleTypeEnum.CHANGE_LOG_RULE, rule -> changeLogRules.add((ChangeLogRule) rule));

            for (Rule rule : rulesToValidateWith) {
                ruleTypeMap.get(rule.getType()).accept(rule);
            }

            validationErrors.addAll(
                    processValidation(
                            changeLogFile,
                            changeLogRules,
                            changeSetRules,
                            exclusionParser,
                            changeLogFormat));
        }
        return validationErrors;
    }

    /**
     * Process a single changeLog file.
     *
     * @param changeLogFile   - changeLog file.
     * @param changeLogRules  - set of changeLog rules.
     * @param changeSetRules  - set of changeSet rules.
     * @param exclusionParser - exclusion parser.
     * @param changeLogFormat - changeLog format.
     * @return list of validation errors. Empty list of there are no errors.
     */
    private List<String> processValidation(final File changeLogFile,
                                           final Set<ChangeLogRule> changeLogRules,
                                           final Set<ChangeSetRule> changeSetRules,
                                           final ExclusionParser exclusionParser,
                                           final ChangeLogFormatEnum changeLogFormat) {
        List<String> validationErrors = new ArrayList<>();
        try {
            processChangeLogRules(
                    changeLogFile,
                    changeLogRules,
                    validationErrors);

            processChangeSetRules(
                    changeLogFile,
                    changeSetRules,
                    changeLogFormat,
                    exclusionParser,
                    validationErrors);
        } catch (Exception e) {
            validationErrors.add("[" + changeLogFile.getName() + "] Failed to parse: " + e.getMessage());
            return validationErrors;
        }
        return validationErrors;
    }

    /**
     * Process changeLog rules.
     *
     * @param changeLogFile    - changeLog file.
     * @param changeLogRules   - changeLog rules.
     * @param validationErrors - validation errors.
     */
    private void processChangeLogRules(final File changeLogFile,
                                       final Set<ChangeLogRule> changeLogRules,
                                       final List<String> validationErrors) {
        for (ChangeLogRule changeLogRule : changeLogRules) {
            try {
                changeLogRule.validateChangeLog(changeLogFile);
            } catch (ValidationException e) {
                validationErrors.add("[" + changeLogFile.getName() + "] " + e.getMessage());
            }
        }
    }

    /**
     * Process changeSet rules.
     *
     * @param changeLogFile    - changeLog file.
     * @param changeSetRules   - changeSet rules.
     * @param changeLogFormat  - changeLog format.
     * @param exclusionParser  - exclusion parser.
     * @param validationErrors - validation errors.
     * @throws ChangeLogParseException - thrown if changeLog parsing fails.
     */
    private void processChangeSetRules(final File changeLogFile,
                                       final Set<ChangeSetRule> changeSetRules,
                                       final ChangeLogFormatEnum changeLogFormat,
                                       final ExclusionParser exclusionParser,
                                       final List<String> validationErrors) throws ChangeLogParseException {
        List<ChangeSetElement> changeSetElements = CHANGELOG_PARSER.get(changeLogFormat)
                .parseChangeLog(changeLogFile);
        for (ChangeSetRule changeSetRule : changeSetRules) {
            for (ChangeSetElement changeSetElement : changeSetElements) {
                try {
                    changeSetRule.validateChangeSet(
                            changeSetElement,
                            exclusionParser,
                            changeLogFile.getName(),
                            changeLogFormat);
                } catch (ValidationException e) {
                    validationErrors.add("[" + changeLogFile.getName() + "] " + e.getMessage());
                }
            }
        }
    }

    /**
     * Exclude rules based on the data from the exclusion file.
     *
     * @param changeSetRules  - set of specified rules.
     * @param exclusionParser - exclusion parser.
     * @param changeLogFile   - changeLog file.
     * @return set of rules to apply to the given changeLog file.
     */
    private Set<Rule> excludeRulesBasedOnExclusionFile(final List<Rule> changeSetRules,
                                                       final ExclusionParser exclusionParser,
                                                       final File changeLogFile) {
        Set<Rule> rulesToValidateWith = new HashSet<>();
        for (Rule changeSetRule : changeSetRules) {
            if (!exclusionParser.isFileExcluded(changeLogFile.getName(), changeSetRule.getName())) {
                rulesToValidateWith.add(changeSetRule);
            }
        }
        return rulesToValidateWith;
    }
}
