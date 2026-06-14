package io.github.htshame.core;

import io.github.htshame.enums.ChangeLogFormatEnum;

import java.io.File;

/**
 * Plugin configuration class.
 */
public final class PluginConfig {

    private final ChangeLogFormatEnum changeLogFormat;

    private final File pathToRulesFile;

    private final File pathToExclusionsFile;

    private final File changeLogDirectory;

    private final Boolean shouldGenerateExclusions;

    /**
     * Constructor.
     *
     * @param changeLogFormat          - changeLog format.
     * @param pathToRulesFile          - path to file with described rules.
     * @param pathToExclusionsFile     - path to file with exclusions.
     * @param changeLogDirectory       - path to changeLog directory.
     * @param shouldGenerateExclusions - whether should exclusions file be generated.
     */
    public PluginConfig(final String changeLogFormat,
                        final File pathToRulesFile,
                        final File pathToExclusionsFile,
                        final File changeLogDirectory,
                        final Boolean shouldGenerateExclusions) {
        this.changeLogFormat = ChangeLogFormatEnum.fromValue(changeLogFormat.toLowerCase());
        this.pathToRulesFile = pathToRulesFile;
        this.pathToExclusionsFile = pathToExclusionsFile;
        this.changeLogDirectory = changeLogDirectory;
        this.shouldGenerateExclusions = shouldGenerateExclusions;
    }

    /**
     * Get changeLog format.
     *
     * @return changeLog format.
     */
    public ChangeLogFormatEnum getChangeLogFormat() {
        return changeLogFormat;
    }

    /**
     * Get path to rules file.
     *
     * @return path to rules file.
     */
    public File getPathToRulesFile() {
        return pathToRulesFile;
    }

    /**
     * Get path to exclusions file.
     *
     * @return path to exclusions file.
     */
    public File getPathToExclusionsFile() {
        return pathToExclusionsFile;
    }

    /**
     * Get changeLog directory.
     *
     * @return changeLog directory.
     */
    public File getChangeLogDirectory() {
        return changeLogDirectory;
    }

    /**
     * Get should generate exclusions flag.
     *
     * @return should generate exclusions flag.
     */
    public Boolean getShouldGenerateExclusions() {
        return shouldGenerateExclusions;
    }
}
