package io.github.htshame.util;

import io.github.htshame.dto.RuleValidationErrorDto;
import io.github.htshame.log.PluginLogger;

import java.util.List;

/**
 * Generates the contents of the exclusions file.
 */
public class ExclusionsGenerator {

    private final PluginLogger log;

    /**
     * Constructor.
     *
     * @param log - logger.
     */
    public ExclusionsGenerator(final PluginLogger log) {
        this.log = log;
    }

    /**
     * Generate the contents of the exclusions file.
     *
     * @param validationErrors - validation errors.
     */
    public void generateExclusions(final List<RuleValidationErrorDto> validationErrors) {

    }
}
