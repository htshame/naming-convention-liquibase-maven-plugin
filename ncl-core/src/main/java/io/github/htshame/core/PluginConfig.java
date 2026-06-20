package io.github.htshame.core;

import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.enums.PluginTypeEnum;

import java.io.File;
import java.net.URL;
import java.util.Objects;

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
    private final PluginTypeEnum pluginType;
    private final URL rulesFileUrl;
    private final URL exclusionsFileUrl;

    /**
     * Private constructor used by the Builder.
     *
     * @param builder - the builder instance.
     */
    private PluginConfig(final Builder builder) {
        this.changeLogFormat = builder.changeLogFormatBuilder;
        this.pathToRulesFile = builder.pathToRulesFileBuilder;
        this.pathToExclusionsFile = builder.pathToExclusionsFileBuilder;
        this.changeLogDirectory = builder.changeLogDirectoryBuilder;
        this.shouldGenerateExclusions = builder.shouldGenerateExclusionsBuilder;
        this.pluginVersion = builder.pluginVersionBuilder;
        this.pluginType = builder.pluginTypeBuilder;
        this.rulesFileUrl = builder.rulesFileUrlBuilder;
        this.exclusionsFileUrl = builder.exclusionsFileUrlBuilder;
    }

    /**
     * Create a new builder instance.
     *
     * @return builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link PluginConfig}.
     */
    public static final class Builder {

        /**
         * Private constructor.
         */
        private Builder() {

        }

        private ChangeLogFormatEnum changeLogFormatBuilder;
        private File pathToRulesFileBuilder;
        private File pathToExclusionsFileBuilder;
        private File changeLogDirectoryBuilder;
        private boolean shouldGenerateExclusionsBuilder;
        private String pluginVersionBuilder;
        private PluginTypeEnum pluginTypeBuilder;
        private URL rulesFileUrlBuilder;
        private URL exclusionsFileUrlBuilder;

        /**
         * Set changeLog format.
         *
         * @param changeLogFormat - changeLog format.
         * @return this builder.
         */
        public Builder changeLogFormat(final String changeLogFormat) {
            this.changeLogFormatBuilder = ChangeLogFormatEnum.fromValue(changeLogFormat.toLowerCase());
            return this;
        }

        /**
         * Set path to rules file.
         *
         * @param pathToRulesFile - path to file with described rules.
         * @return this builder.
         */
        public Builder pathToRulesFile(final File pathToRulesFile) {
            this.pathToRulesFileBuilder = pathToRulesFile;
            return this;
        }

        /**
         * Set path to exclusions file.
         *
         * @param pathToExclusionsFile - path to file with exclusions.
         * @return this builder.
         */
        public Builder pathToExclusionsFile(final File pathToExclusionsFile) {
            this.pathToExclusionsFileBuilder = pathToExclusionsFile;
            return this;
        }

        /**
         * Set changeLog directory.
         *
         * @param changeLogDirectory - path to changeLog directory.
         * @return this builder.
         */
        public Builder changeLogDirectory(final File changeLogDirectory) {
            this.changeLogDirectoryBuilder = changeLogDirectory;
            return this;
        }

        /**
         * Set whether exclusions file should be generated.
         *
         * @param shouldGenerateExclusions - whether should exclusions file be generated.
         * @return this builder.
         */
        public Builder shouldGenerateExclusions(final boolean shouldGenerateExclusions) {
            this.shouldGenerateExclusionsBuilder = shouldGenerateExclusions;
            return this;
        }

        /**
         * Set plugin version.
         *
         * @param pluginVersion - plugin version.
         * @return this builder.
         */
        public Builder pluginVersion(final String pluginVersion) {
            this.pluginVersionBuilder = pluginVersion;
            return this;
        }

        /**
         * Set plugin type.
         *
         * @param pluginType - plugin type.
         * @return this builder.
         */
        public Builder pluginType(final PluginTypeEnum pluginType) {
            this.pluginTypeBuilder = pluginType;
            return this;
        }

        /**
         * Set rules file URL.
         *
         * @param rulesFileUrl - rules file URL.
         * @return this builder.
         */
        public Builder ruleFileUrl(final URL rulesFileUrl) {
            this.rulesFileUrlBuilder = rulesFileUrl;
            return this;
        }

        /**
         * Set exclusions file URL.
         *
         * @param exclusionsFileUrl - exclusions file url.
         * @return this builder.
         */
        public Builder exclusionsFileUrl(final URL exclusionsFileUrl) {
            this.exclusionsFileUrlBuilder = exclusionsFileUrl;
            return this;
        }

        /**
         * Build the {@link PluginConfig} instance.
         *
         * @return new PluginConfig instance.
         */
        public PluginConfig build() {
            Objects.requireNonNull(changeLogFormatBuilder, "changeLogFormat must not be null");
            Objects.requireNonNull(changeLogDirectoryBuilder, "changeLogDirectory must not be null");
            Objects.requireNonNull(pluginVersionBuilder, "pluginVersion must not be null");
            Objects.requireNonNull(pluginTypeBuilder, "pluginType must not be null");
            if ((pathToRulesFileBuilder == null) == (rulesFileUrlBuilder == null)) {
                throw new IllegalArgumentException(
                        "Exactly one of 'pathToRulesFile' or 'rulesFileUrl' parameters must be present");
            }
            if (pathToExclusionsFileBuilder != null && exclusionsFileUrlBuilder != null) {
                throw new IllegalArgumentException(
                        "Only one of 'pathToExclusionsFile' or 'exclusionsFileUrl' parameters must be present");
            }
            return new PluginConfig(this);
        }
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
     * @return plugin version.
     */
    public String getPluginVersion() {
        return pluginVersion;
    }

    /**
     * Get plugin type.
     *
     * @return plugin type.
     */
    public PluginTypeEnum getPluginType() {
        return pluginType;
    }

    /**
     * Get rules file URL.
     *
     * @return rules file URL.
     */
    public URL getRulesFileUrl() {
        return rulesFileUrl;
    }

    /**
     * Get exclusions file URL.
     *
     * @return exclusions file URL.
     */
    public URL getExclusionsFileUrl() {
        return exclusionsFileUrl;
    }
}
