package io.github.htshame;

import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ChangeLogCollectorException;
import io.github.htshame.exception.ExclusionParserException;
import io.github.htshame.exception.RuleParserException;
import io.github.htshame.rule.Rule;
import io.github.htshame.util.ChangeLogFilesCollector;
import io.github.htshame.util.parser.ExclusionParser;
import io.github.htshame.util.parser.RuleParser;
import io.github.htshame.validator.ValidationManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

/**
 * <code>validate-liquibase-xml</code> processor.
 */
@Mojo(name = "validate-liquibase-xml", defaultPhase = LifecyclePhase.COMPILE)
public class ValidateLiquibaseXmlMojo extends AbstractMojo {

    private static final String INVALID_PATH = "Invalid path: ";

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
     * <br/>
     * If violations are found:
     * <br/>
     * - the build will fail if set to <code>true</code>;
     * <br/>
     * - the build will not fail if set to <code>false</code>;
     * <br/>
     * Default value is <code>true</code>.
     */
    @Parameter(defaultValue = "true")
    private Boolean shouldFailBuild;

    /**
     * ChangeLog files format.
     * Default value is <code>xml</code>.
     */
    @Parameter(defaultValue = "xml")
    private String changeLogFormat;

    private final ValidationManager validationManager;

    /**
     * Default constructor.
     */
    public ValidateLiquibaseXmlMojo() {
        this.validationManager = new ValidationManager();
    }

    /**
     * Starting plugin execution.
     *
     * @throws MojoExecutionException - thrown if execution fails.
     */
    public void execute() throws MojoExecutionException {
        validateInput();

        List<Rule> rules;
        ExclusionParser exclusionParser;
        List<File> changeLogFiles;
        ChangeLogFormatEnum changeLogFormatEnum = ChangeLogFormatEnum.fromValue(changeLogFormat.toLowerCase());
        try {
            rules = RuleParser.parseRules(pathToRulesFile);
            exclusionParser = ExclusionParser.parseExclusions(pathToExclusionsFile);
            changeLogFiles = ChangeLogFilesCollector.collectChangeLogFiles(changeLogDirectory, changeLogFormatEnum);
        } catch (RuleParserException | ExclusionParserException | ChangeLogCollectorException e) {
            getLog().error("Error processing input parameters", e);
            throw new MojoExecutionException(e.getMessage());
        }

        List<String> validationErrors =
                validationManager.validate(changeLogFiles, rules, exclusionParser, changeLogFormatEnum);

        try {
            checkValidationResult(validationErrors);
            getLog().info("All ChangeLog files passed validation.");
        } catch (MojoExecutionException e) {
            if (Boolean.TRUE.equals(shouldFailBuild)) {
                throw e;
            }
            getLog().warn(e.getMessage()
                    + " Build will not fail because <shouldFailBuild>false</shouldFailBuild>.");
        }
    }

    /**
     * Check is validation errors exist. Log them if they do.
     *
     * @param validationErrors - list of validation errors.
     * @throws MojoExecutionException - thrown in there are validation errors.
     */
    private void checkValidationResult(final List<String> validationErrors) throws MojoExecutionException {
        if (!validationErrors.isEmpty()) {
            getLog().error("====== Liquibase changeset validation failed ======");
            for (String v : validationErrors) {
                getLog().error(v);
            }
            throw new MojoExecutionException("Validation failed: " + validationErrors.size() + " violation(s) found.");
        }
    }

    /**
     * Validate incoming parameters.
     * - changeLog directory exists;
     * - XML rules file is present;
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
