package io.github.htshame.core;

import io.github.htshame.dto.RuleValidationErrorDto;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ChangeLogCollectorException;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.RuleParserException;
import io.github.htshame.exception.ValidateChangeLogException;
import io.github.htshame.log.PluginLogger;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.parser.RuleParser;
import io.github.htshame.rule.Rule;
import io.github.htshame.util.ChangeLogFilesCollector;
import io.github.htshame.util.ExclusionsGenerator;
import io.github.htshame.validator.ValidationManager;

import java.io.File;
import java.util.List;

/**
 * Validate changeLog service. It handles and orchestrates all business logic of the plugin.
 */
public final class ValidateChangeLogService {

    private static final String BASE_URL = "https://htshame.github.io";
    private static final String PROJECT_NAME_PATH = "/naming-convention-liquibase-maven-plugin";

    private final PluginLogger logger;
    private final PluginConfig config;
    private final ValidationManager validationManager;
    private final ExclusionsGenerator exclusionsGenerator;

    /**
     * Constructor.
     *
     * @param logger - logger.
     * @param config - configuration.
     */
    public ValidateChangeLogService(final PluginLogger logger,
                                    final PluginConfig config) {
        this.logger = logger;
        this.config = config;
        this.validationManager = new ValidationManager();
        this.exclusionsGenerator = new ExclusionsGenerator(logger, config);
    }

    /**
     * Run the plugin execution.
     *
     * @throws ValidateChangeLogException - validation exception.
     */
    public void execute() throws ValidateChangeLogException {
        List<Rule> rules = prepareRules();
        ExclusionParser exclusionParser = prepareExclusions();
        List<File> changeLogFiles = prepareChangeLogFiles(config.getChangeLogFormat());

        List<RuleValidationErrorDto> validationErrors = validationManager.validate(
                changeLogFiles,
                rules,
                exclusionParser,
                config.getChangeLogFormat());

        try {
            checkValidationResult(validationErrors);
        } catch (ValidateChangeLogException e) {
            logger.warn("Failing the build because <shouldFailBuild> is not provided or set to 'true'");
            if (config.getShouldGenerateExclusions()) {
                exclusionsGenerator.generateExclusions(validationErrors);
            }
            throw e;
        }
    }

    /**
     * Check is validation errors exist. Log them if they do.
     *
     * @param validationErrors - list of validation errors.
     * @throws ValidateChangeLogException - thrown in there are validation errors.
     */
    private void checkValidationResult(final List<RuleValidationErrorDto> validationErrors)
            throws ValidateChangeLogException {
        if (validationErrors.isEmpty()) {
            logger.info("All ChangeLog files passed validation");
            return;
        }
        logger.error("====== Liquibase changeset validation failed ======");
        for (RuleValidationErrorDto validationError : validationErrors) {
            if (validationError.getErrorMessage() == null) {
                logger.error(validationError.getGenericMessage());
            } else {
                logger.error(validationError.getErrorMessage());
            }
        }
        throw new ValidateChangeLogException("Validation failed: " + validationErrors.size() + " violation(s) found.");
    }

    /**
     * Prepare validation rules.
     *
     * @return list of rules.
     * @throws ValidateChangeLogException - if rule parsing fails.
     */
    private List<Rule> prepareRules() throws ValidateChangeLogException {
        try {
            return RuleParser.parseRules(config.getPathToRulesFile());
        } catch (RuleParserException e) {
            logger.error("Error parsing rules file. Double-check the path to rules XML file "
                    + "provided in <pathToRulesFile>. The sample file: "
                    + BASE_URL
                    + PROJECT_NAME_PATH + "/schema/example/rules_example.xml", e);
            throw new ValidateChangeLogException(e.getMessage());
        }
    }

    /**
     * Prepare exclusions.
     *
     * @return instance of exclusion parser.
     * @throws ValidateChangeLogException - if exclusions parsing fails.
     */
    private ExclusionParser prepareExclusions() throws ValidateChangeLogException {
        try {
            return ExclusionParser.parseExclusions(config.getPathToExclusionsFile());
        } catch (ExclusionParserException e) {
            logger.error("Error parsing exclusions file. Double-check the path to exclusions XML file "
                    + "provided in <pathToExclusionsFile>. The sample file: "
                    + BASE_URL
                    + PROJECT_NAME_PATH + "/schema/example/exclusions_example.xml", e);
            throw new ValidateChangeLogException(e.getMessage());
        }
    }

    /**
     * Collect changeLog files to validate.
     *
     * @param changeLogFormatEnum - changeLog format.
     * @return list of changeLog files.
     * @throws ValidateChangeLogException - if changeLog collection fails.
     */
    private List<File> prepareChangeLogFiles(final ChangeLogFormatEnum changeLogFormatEnum)
            throws ValidateChangeLogException {
        try {
            return ChangeLogFilesCollector.collectChangeLogFiles(config.getChangeLogDirectory(), changeLogFormatEnum);
        } catch (ChangeLogCollectorException e) {
            logger.error("Error changeLog files. Double-check the changeLog directory "
                    + "provided in <changeLogDirectory> and changeLog format provided in <changeLogFormat>", e);
            throw new ValidateChangeLogException(e.getMessage());
        }
    }
}
