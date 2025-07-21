package io.github.htshame.validator;

import io.github.htshame.change.log.ChangeLogParser;
import io.github.htshame.change.log.JsonChangeLogParser;
import io.github.htshame.change.log.XmlChangeLogParser;
import io.github.htshame.change.log.YamlChangeLogParser;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ChangeLogParseException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.rule.Rule;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * Map of changeLog formats and changeLog parsers.
     */
    static final EnumMap<ChangeLogFormatEnum, ChangeLogParser> CHANGELOG_PARSER_MAP =
            new EnumMap<>(ChangeLogFormatEnum.class);

    static {
        CHANGELOG_PARSER_MAP.put(ChangeLogFormatEnum.XML, new XmlChangeLogParser());
        CHANGELOG_PARSER_MAP.put(ChangeLogFormatEnum.YAML, new YamlChangeLogParser());
        CHANGELOG_PARSER_MAP.put(ChangeLogFormatEnum.YML, new YamlChangeLogParser());
        CHANGELOG_PARSER_MAP.put(ChangeLogFormatEnum.JSON, new JsonChangeLogParser());
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
            Set<Rule> rulesToValidateAgainst = excludeRulesBasedOnExclusionFile(rules, exclusionParser, changeLogFile);

            Collection<RuleValidator> ruleValidators = RuleValidatorFactory.instantiate(rulesToValidateAgainst);
            for (RuleValidator ruleValidator : ruleValidators) {
                try {
                    ruleValidator.validate(changeLogFile, validationErrors, changeLogFormat, exclusionParser);
                } catch (ChangeLogParseException e) {
                    validationErrors.add("[" + changeLogFile.getName() + "] Failed to parse: " + e.getMessage());
                }
            }
        }
        return validationErrors;
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
