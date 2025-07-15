package io.github.htshame;

import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ChangeLogCollectorException;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.RuleParserException;
import io.github.htshame.parser.ExclusionParser;
import io.github.htshame.parser.RuleParser;
import io.github.htshame.rule.Rule;
import io.github.htshame.util.ChangeLogFilesCollector;
import io.github.htshame.validator.ValidationManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

/**
 * <code>validate-liquibase-changeLog</code> mojo processor.
 */
@Mojo(name = "validate-liquibase-changeLog", defaultPhase = LifecyclePhase.COMPILE)
public class ValidateChangeLogMojo extends AbstractMojo {

    private static final String INVALID_PATH = "Invalid path: ";
    private static final String BASE_URL = "https://htshame.github.io";
    private static final String PROJECT_NAME_PATH = "/naming-convention-liquibase-maven-plugin";

    /**
     * Path to the XML file with rules.
     */
    @Parameter(required = true)
    private File pathToRulesFile;

    /**
     * Path to the XML file with exclusions.
     */
    @Parameter
    private File pathToExclusionsFile;

    /**
     * Path to directory with changeLog files.
     */
    @Parameter(required = true)
    private File changeLogDirectory;

    /**
     * Flag that determines whether the build will be failed in case violations are found.
     * <br>
     * If violations are found:
     * <br>
     * - the build will fail if set to <code>true</code>;
     * <br>
     * - the build will not fail if set to <code>false</code>;
     * <br>
     * Default value is <code>true</code>.
     */
    @Parameter(defaultValue = "true")
    private Boolean shouldFailBuild;

    /**
     * ChangeLog files format.
     * <p>
     * Supported formats:
     * <br>
     * - xml
     * <br>
     * - yaml/yml
     * <br>
     * - json
     * <p>
     * Default value is <code>xml</code>.
     */
    @Parameter(defaultValue = "xml")
    private String changeLogFormat;

    /**
     * Default constructor.
     */
    public ValidateChangeLogMojo() {

    }

    /**
     * Starting plugin execution.
     *
     * @throws MojoExecutionException - thrown if execution fails.
     */
    public void execute() throws MojoExecutionException {
        validateInput();

        ChangeLogFormatEnum changeLogFormatEnum = ChangeLogFormatEnum.fromValue(changeLogFormat.toLowerCase());

        List<Rule> rules = prepareRules();
        ExclusionParser exclusionParser = prepareExclusions();
        List<File> changeLogFiles = prepareChangeLogFiles(changeLogFormatEnum);
        ValidationManager validationManager = prepareValidationManager();

        List<String> validationErrors = validationManager.validate(
                changeLogFiles,
                rules,
                exclusionParser,
                changeLogFormatEnum);

        try {
            checkValidationResult(validationErrors);
        } catch (MojoExecutionException e) {
            getLog().warn("Failing the build because <shouldFailBuild> is not provided or set to 'true'");
            if (Boolean.TRUE.equals(shouldFailBuild)) {
                throw e;
            }
            getLog().warn(e.getMessage()
                    + " Build will not fail because <shouldFailBuild>false</shouldFailBuild>");
        }
    }

    /**
     * Prepare validation rules.
     *
     * @return list of rules.
     * @throws MojoExecutionException - if rule parsing fails.
     */
    private List<Rule> prepareRules() throws MojoExecutionException {
        try {
            return RuleParser.parseRules(pathToRulesFile);
        } catch (RuleParserException e) {
            getLog().error("Error parsing rules file. Double-check the path to rules XML file "
                    + "provided in <pathToRulesFile>. The sample file: "
                    + BASE_URL
                    + PROJECT_NAME_PATH + "/schema/example/rules_example.xml", e);
            throw new MojoExecutionException(e.getMessage());
        }
    }

    /**
     * Prepare exclusions.
     *
     * @return instance of exclusion parser.
     * @throws MojoExecutionException - if exclusions parsing fails.
     */
    private ExclusionParser prepareExclusions() throws MojoExecutionException {
        try {
            return ExclusionParser.parseExclusions(pathToExclusionsFile);
        } catch (ExclusionParserException e) {
            getLog().error("Error parsing exclusions file. Double-check the path to exclusions XML file "
                    + "provided in <pathToExclusionsFile>. The sample file: "
                    + BASE_URL
                    + PROJECT_NAME_PATH + "/schema/example/exclusions_example.xml", e);
            throw new MojoExecutionException(e.getMessage());
        }
    }

    /**
     * Collect changeLog files to validate.
     *
     * @param changeLogFormatEnum - changeLog format.
     * @return list of changeLog files.
     * @throws MojoExecutionException - if changeLog collection fails.
     */
    private List<File> prepareChangeLogFiles(final ChangeLogFormatEnum changeLogFormatEnum)
            throws MojoExecutionException {
        try {
            return ChangeLogFilesCollector.collectChangeLogFiles(changeLogDirectory, changeLogFormatEnum);
        } catch (ChangeLogCollectorException e) {
            getLog().error("Error changeLog files. Double-check the changeLog directory "
                    + "provided in <changeLogDirectory> and changeLog format provided in <changeLogFormat>", e);
            throw new MojoExecutionException(e.getMessage());
        }
    }

    /**
     * Instantiate validation manager.
     *
     * @return validation manager.
     */
    private ValidationManager prepareValidationManager() {
        return new ValidationManager();
    }

    /**
     * Check is validation errors exist. Log them if they do.
     *
     * @param validationErrors - list of validation errors.
     * @throws MojoExecutionException - thrown in there are validation errors.
     */
    private void checkValidationResult(final List<String> validationErrors) throws MojoExecutionException {
        if (validationErrors.isEmpty()) {
            getLog().info("All ChangeLog files passed validation");
            return;
        }
        getLog().error("====== Liquibase changeset validation failed ======");
        for (String validationError : validationErrors) {
            getLog().error(validationError);
        }
        throw new MojoExecutionException("Validation failed: " + validationErrors.size() + " violation(s) found.");
    }

    /**
     * Validate incoming parameters.
     * <p>
     * - changeLog directory exists;
     * <br>
     * - XML rules file is present;
     * <br>
     * - XML exclusions file exists if provided;
     * <br>
     * - changeLog format is supported;
     *
     * @throws MojoExecutionException - if something's not found.
     */
    private void validateInput() throws MojoExecutionException {
        if (!changeLogDirectory.isDirectory()) {
            throw new MojoExecutionException(INVALID_PATH + changeLogDirectory);
        }
        if (!pathToRulesFile.exists()) {
            throw new MojoExecutionException(INVALID_PATH + pathToRulesFile);
        }
        if (pathToExclusionsFile != null && !pathToExclusionsFile.exists()) {
            throw new MojoExecutionException(INVALID_PATH + pathToExclusionsFile);
        }
        try {
            ChangeLogFormatEnum.fromValue(changeLogFormat.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new MojoExecutionException("ChangeLog format [" + changeLogFormat + "] is not supported");
        }
    }

}
