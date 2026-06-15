package io.github.htshame.util;

import io.github.htshame.core.PluginConfig;
import io.github.htshame.dto.RuleValidationErrorDto;
import io.github.htshame.log.PluginLogger;

import java.util.List;

/**
 * Generates the contents of the exclusions file.
 */
public class ExclusionsGenerator {

    private final PluginLogger logger;
    private final PluginConfig config;

    /**
     * Constructor.
     *
     * @param logger - logger.
     * @param config - plugin configuration.
     */
    public ExclusionsGenerator(final PluginLogger logger,
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
            logger.info("====== Generating content of the exclusions file ======\n");
            logger.info(String.format("<exclusions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                            + "            xsi:noNamespaceSchemaLocation=\""
                            + "https://htshame.github.io/naming-convention-liquibase-maven-plugin"
                            + "/schema/ruleset/%s/liquibase-naming-convention-%s.xsd\">",
                    config.getPluginVersion(), config.getPluginVersion()));
            for (RuleValidationErrorDto errorDto : validationErrors) {
                if (errorDto.getRule() == null) {
                    continue;
                }
                switch (errorDto.getRule().getType()) {
                    case CHANGE_SET_RULE:
                        logger.info(String.format(
                                "    <changeSetExclusion fileName=\"%s\" changeSetId=\"%s\" "
                                        + "changeSetAuthor=\"%s\" rule=\"%s\"/>",
                                errorDto.getChangeLogFileName(),
                                errorDto.getChangeSetId(),
                                errorDto.getChangeSetAuthor(),
                                errorDto.getRule().getValue()));
                        break;
                    case CHANGE_LOG_FILE_RULE:
                        logger.info(String.format(
                                "    <fileExclusion fileName=\"%s\" rule=\"%s\"/>",
                                errorDto.getChangeLogFileName(),
                                errorDto.getRule().getValue()));
                        break;
                    case CHANGE_LOG_RULE:
                        logger.info(String.format(
                                "    <changeLogExclusion fileName=\"%s\" rule=\"%s\"/>",
                                errorDto.getChangeLogFileName(),
                                errorDto.getRule().getValue()));
                        break;
                    default:
                        break;
                }
            }
            logger.info("</exclusions>");
            logger.info("\n====== Content generation of the exclusions file complete ======");
        } catch (Exception e) {
            logger.error("====== Failed to generate content of the exclusions file ======");
        }
    }
}
