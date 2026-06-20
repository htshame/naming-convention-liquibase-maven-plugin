package io.github.htshame;

import io.github.htshame.core.PluginConfig;
import io.github.htshame.core.ValidateChangeLogService;
import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.PluginTypeEnum;
import io.github.htshame.exception.ValidateChangeLogException;
import io.github.htshame.log.PluginLogger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * <code>validate-liquibase-changeLog</code> mojo executor.
 */
@Mojo(name = "validate-liquibase-changeLog", defaultPhase = LifecyclePhase.COMPILE)
public class ValidateChangeLogMojo extends AbstractMojo {

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
    private boolean shouldFailBuild;

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
     * Flag that determines whether the exclusions file content will be generated if the build fails.
     * <br>
     * This is helpful when changeLog already has a bunch of errors that will not be addressed
     * and user does not want to spend time writing exclusions manually.
     * <br>
     * If set to <code>true</code>, the contents of exclusions file will be generated in the logs.
     * <br>
     * If set to <code>false</code>, the contents of exclusions file will not be generated.
     * <br>
     * Default value is <code>false</code>.
     */
    @Parameter(defaultValue = "false")
    private boolean shouldGenerateExclusions;

    /**
     * Plugin descriptor.
     */
    @Parameter(defaultValue = "${plugin}", readonly = true)
    private PluginDescriptor pluginDescriptor;

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
        configReminder();

        PluginConfig config = new PluginConfig(
                changeLogFormat,
                pathToRulesFile,
                pathToExclusionsFile,
                changeLogDirectory,
                shouldGenerateExclusions,
                pluginDescriptor.getVersion(),
                PluginTypeEnum.MAVEN);

        PluginLogger logger = preparePluginLogger();

        ValidateChangeLogService validateChangeLogService = new ValidateChangeLogService(
                logger,
                config);

        try {
            validateChangeLogService.execute();
        } catch (ValidateChangeLogException e) {
            if (shouldFailBuild) {
                throw new MojoExecutionException(e.getMessage());
            }
            logger.warn(e.getMessage()
                    + " Build will not fail because <shouldFailBuild>false</shouldFailBuild>");
        }
    }

    /**
     * Analyze input parameters and remind a user that parameters exist.
     */
    private void configReminder() {
        if (pathToExclusionsFile == null) {
            getLog().info("Parameter <pathToExclusionsFile> is not set. Great job!");
        }
        if (shouldFailBuild) {
            getLog().info("Parameter <shouldFailBuild> is set to 'true' (default value) or not defined.");
        }
        if ("xml".equals(changeLogFormat)) {
            getLog().info("Parameter <changeLogFormat> is set to 'xml' (default value) or not defined.");
        }
        if (!shouldGenerateExclusions) {
            getLog().info("Parameter <shouldGenerateExclusions> is set to 'false' (default value) or not defined.");
        }
    }

    /**
     * Prepare plugin logger.
     *
     * @return plugin logger.
     */
    private PluginLogger preparePluginLogger() {
        return new PluginLogger() {
            @Override
            public void info(final String message) {
                getLog().info(message);
            }

            @Override
            public void warn(final String message) {
                getLog().warn(message);
            }

            @Override
            public void error(final String message) {
                getLog().error(message);
            }

            @Override
            public void error(final String message, final Exception e) {
                getLog().error(message, e);
            }
        };
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
