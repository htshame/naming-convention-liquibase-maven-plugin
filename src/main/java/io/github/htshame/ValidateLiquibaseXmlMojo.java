package io.github.htshame;

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

    @Parameter(defaultValue = "true")
    private Boolean shouldFailBuild;

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

        try {
            rules = RuleParser.parseRules(pathToRulesFile);
            exclusionParser = ExclusionParser.parseExclusions(pathToExclusionsFile);
            changeLogFiles = ChangeLogFilesCollector.collectChangeLogFiles(changeLogDirectory, changeLogFormat);
        } catch (RuleParserException | ExclusionParserException | ChangeLogCollectorException e) {
            getLog().error("Error processing input parameters", e);
            throw new MojoExecutionException(e.getMessage());
        }

        List<String> validationErrors = validationManager.validate(changeLogFiles, rules, exclusionParser);

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
    }

}
