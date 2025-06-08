package com.github.htshame;

import com.github.htshame.exception.ChangeLogCollectorException;
import com.github.htshame.exception.ExclusionParserException;
import com.github.htshame.exception.RuleParserException;
import com.github.htshame.parser.ChangeLogParser;
import com.github.htshame.parser.ExclusionParser;
import com.github.htshame.parser.RuleParser;
import com.github.htshame.processor.ValidationProcessor;
import com.github.htshame.rules.Rule;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;
import java.util.Set;

@Mojo(name = "validate-liquibase-xml", defaultPhase = LifecyclePhase.COMPILE)
public class XmlValidatorMojo extends AbstractMojo {

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
            rules = RuleParser.parseRules(pathToRulesFile);
            exclusionParser = ExclusionParser.parseExclusions(pathToExclusionsFile);
            changeLogFiles = ChangeLogParser.collectChangeLogFiles(changeLogDirectory);
        } catch (RuleParserException | ExclusionParserException | ChangeLogCollectorException e) {
            getLog().error("Error processing input parameters", e);
            throw new MojoExecutionException(e.getMessage());
        }

        List<String> allViolations = new ValidationProcessor().validate(changeLogFiles, rules, exclusionParser);

        if (!allViolations.isEmpty()) {
            getLog().error("====== Liquibase changeset validation failed ======");
            for (String v : allViolations) {
                getLog().error(v);
            }
            throw new MojoExecutionException("Validation failed: " + allViolations.size() + " violation(s) found.");
        }
        getLog().info("All ChangeLog files passed validation.");
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
            throw new MojoExecutionException("Invalid path: " + changeLogDirectory);
        }
        if (!pathToRulesFile.exists()) {
            throw new MojoExecutionException("Invalid path: " + pathToRulesFile);
        }
    }

}
