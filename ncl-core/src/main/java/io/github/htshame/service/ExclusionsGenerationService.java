package io.github.htshame.service;

import io.github.htshame.core.PluginConfig;
import io.github.htshame.dto.RuleValidationErrorDto;
import io.github.htshame.enums.PluginTypeEnum;
import io.github.htshame.log.PluginLogger;

import java.util.List;

/**
 * Generates the contents of the exclusions file.
 */
public class ExclusionsGenerationService {

    private static final String LINE_BREAK = "\n";
    private static final String INDENT = "....";
    private final PluginLogger logger;
    private final PluginConfig config;

    /**
     * Constructor.
     *
     * @param logger - logger.
     * @param config - plugin configuration.
     */
    public ExclusionsGenerationService(final PluginLogger logger,
                                       final PluginConfig config) {
        this.logger = logger;
        this.config = config;
    }

    /**
     * Generate the contents of the exclusions file.
     *
     * @param validationErrors - validation errors.
     */
    public void generateExclusions(final List<RuleValidationErrorDto> validationErrors) {
        try {
            StringBuilder exclusionFileContent = new StringBuilder();
            exclusionFileContent
                    .append(
                            String.format("<exclusions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                                            + "            xsi:noNamespaceSchemaLocation=\""
                                            + "https://htshame.github.io/naming-convention-liquibase-maven-plugin"
                                            + "/schema/ruleset/%s/liquibase-naming-convention-%s.xsd\">",
                                    config.getPluginVersion(), config.getPluginVersion()))
                    .append(LINE_BREAK);
            for (RuleValidationErrorDto errorDto : validationErrors) {
                if (errorDto.getRule() == null) {
                    continue;
                }
                switch (errorDto.getRule().getType()) {
                    case CHANGE_SET_RULE:
                        exclusionFileContent
                                .append(INDENT)
                                .append(String.format(
                                        "<changeSetExclusion fileName=\"%s\" changeSetId=\"%s\" "
                                                + "changeSetAuthor=\"%s\" rule=\"%s\"/>",
                                        errorDto.getChangeLogFileName(),
                                        errorDto.getChangeSetId(),
                                        errorDto.getChangeSetAuthor(),
                                        errorDto.getRule().getValue()))
                                .append(LINE_BREAK);
                        break;
                    case CHANGE_LOG_FILE_RULE:
                        exclusionFileContent
                                .append(INDENT)
                                .append(String.format(
                                        "<fileExclusion fileName=\"%s\" rule=\"%s\"/>",
                                        errorDto.getChangeLogFileName(),
                                        errorDto.getRule().getValue()))
                                .append(LINE_BREAK);
                        break;
                    case CHANGE_LOG_RULE:
                        exclusionFileContent
                                .append(INDENT)
                                .append(String.format(
                                        "<changeLogExclusion fileName=\"%s\" rule=\"%s\"/>",
                                        errorDto.getChangeLogFileName(),
                                        errorDto.getRule().getValue()))
                                .append(LINE_BREAK);
                        break;
                    default:
                        break;
                }
            }
            String exclusionsContent = "\n====== Generating content of the exclusions file ======\n\n"
                    + exclusionFileContent
                    + "</exclusions>"
                    + "\n====== Content generation of the exclusions file complete ======";
            if (PluginTypeEnum.MAVEN.equals(config.getPluginType())) {
                logger.info(exclusionsContent);
            } else if (PluginTypeEnum.GRADLE.equals(config.getPluginType())) {
                logger.error(exclusionsContent);
            }
        } catch (Exception e) {
            logger.error("\n====== Failed to generate content of the exclusions file ======");
        }
    }
}
