package com.github.htshame;

import com.github.htshame.exception.ChangeLogCollectorException;
import com.github.htshame.exception.ExclusionParserException;
import com.github.htshame.exception.RuleParserException;
import com.github.htshame.parser.ChangeLogParser;
import com.github.htshame.parser.ExclusionParser;
import com.github.htshame.parser.RuleParser;
import com.github.htshame.processor.ValidationProcessor;
import com.github.htshame.rule.Rule;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * <code>validate-liquibase-xml</code> processor.
 */
@Mojo(name = "validate-liquibase-xml", defaultPhase = LifecyclePhase.COMPILE)
public class XmlValidatorMojo extends AbstractMojo {

    private static final String INVALID_PATH = "Invalid path: ";

    /**
     * Path to the XML file with rules.
     */
    @Parameter(required = true)
    private File pathToRulesFile;

    /**
     * Path to the XML file with exclusions.
     */
    @Parameter(required = false)
    private File pathToExclusionsFile;

    /**
     * Path to directory with changeLog files.
     */
    @Parameter(required = true)
    private File changeLogDirectory;

    /**
     * Starting plugin execution.
     *
     * @throws MojoExecutionException - thrown if execution fails.
     */
    public void execute() throws MojoExecutionException {
        validateInput();

        Set<Rule> rules;
        ExclusionParser exclusionParser;
        List<File> changeLogFiles;

        try {
            rules = new RuleParser().parseRules(pathToRulesFile);
            exclusionParser = ExclusionParser.parseExclusions(pathToExclusionsFile);
            changeLogFiles = ChangeLogParser.collectChangeLogFiles(changeLogDirectory);
        } catch (RuleParserException | ExclusionParserException | ChangeLogCollectorException e) {
            getLog().error("Error processing input parameters", e);
            throw new MojoExecutionException(e.getMessage());
        }

        List<String> validationErrors = new ValidationProcessor().validate(changeLogFiles, rules, exclusionParser);

        checkValidationResult(validationErrors);

        getLog().info("All ChangeLog files passed validation.");
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
