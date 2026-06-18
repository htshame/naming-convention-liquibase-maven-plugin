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

    private final boolean shouldGenerateExclusions;

    private final String pluginVersion;

    /**
     * Constructor.
     *
     * @param changeLogFormat          - changeLog format.
     * @param pathToRulesFile          - path to file with described rules.
     * @param pathToExclusionsFile     - path to file with exclusions.
     * @param changeLogDirectory       - path to changeLog directory.
     * @param shouldGenerateExclusions - whether should exclusions file be generated.
     * @param pluginVersion            - plugin version.
     */
    public PluginConfig(final String changeLogFormat,
                        final File pathToRulesFile,
                        final File pathToExclusionsFile,
                        final File changeLogDirectory,
                        final boolean shouldGenerateExclusions,
                        final String pluginVersion) {
        this.changeLogFormat = ChangeLogFormatEnum.fromValue(changeLogFormat.toLowerCase());
        this.pathToRulesFile = pathToRulesFile;
        this.pathToExclusionsFile = pathToExclusionsFile;
        this.changeLogDirectory = changeLogDirectory;
        this.shouldGenerateExclusions = shouldGenerateExclusions;
        this.pluginVersion = pluginVersion;
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
    public boolean getShouldGenerateExclusions() {
        return shouldGenerateExclusions;
    }

    /**
     * Get plugin version.
     *
     * @return plugin versin.
     */
    public String getPluginVersion() {
        return pluginVersion;
    }
}
