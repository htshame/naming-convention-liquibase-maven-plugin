package io.github.htshame.core;

import io.github.htshame.enums.PluginTypeEnum;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Plugin config test.
 */
public class PluginConfigTest {

    private static final String CHANGE_LOG_FORMAT = "XML";
    private static final File PATH_TO_RULES_FILE = new File("");
    private static final File PATH_TO_EXCLUSIONS_FILE = new File("");
    private static final File CHANGE_LOG_DIRECTORY = new File("");
    private static final boolean SHOULD_GENERATE_EXCLUSIONS = true;
    private static final String PLUGIN_VERSION = "1.0";
    private static final PluginTypeEnum PLUGIN_TYPE = PluginTypeEnum.MAVEN;
    private static final URL RULE_FILE_URL;
    private static final URL EXCLUSIONS_FILE_URL;

    static {
        try {
            RULE_FILE_URL = new URL("https://raw.githubusercontent.com/htshame/"
                    + "naming-convention-liquibase-maven-plugin/refs/heads/main/docs/schema/example/rules_example.xml");
            EXCLUSIONS_FILE_URL = new URL("https://raw.githubusercontent.com/htshame/"
                    + "naming-convention-liquibase-maven-plugin/refs/heads/main/docs/schema/example/"
                    + "exclusions_example.xml");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test builder.
     */
    @Test
    public void testBuilder() {
        // arrange
        URL rulesFileUrl = null;
        URL exclusionsFileUrl = null;

        // act
        PluginConfig actual = PluginConfig.builder()
                .changeLogFormat(CHANGE_LOG_FORMAT)
                .pathToRulesFile(PATH_TO_RULES_FILE)
                .pathToExclusionsFile(PATH_TO_EXCLUSIONS_FILE)
                .changeLogDirectory(CHANGE_LOG_DIRECTORY)
                .shouldGenerateExclusions(SHOULD_GENERATE_EXCLUSIONS)
                .pluginVersion(PLUGIN_VERSION)
                .pluginType(PLUGIN_TYPE)
                .ruleFileUrl(rulesFileUrl)
                .exclusionsFileUrl(exclusionsFileUrl)
                .build();

        // assert
        Assert.assertEquals(CHANGE_LOG_FORMAT, actual.getChangeLogFormat().name());
        Assert.assertEquals(PATH_TO_RULES_FILE, actual.getPathToRulesFile());
        Assert.assertEquals(PATH_TO_EXCLUSIONS_FILE, actual.getPathToExclusionsFile());
        Assert.assertEquals(CHANGE_LOG_DIRECTORY, actual.getChangeLogDirectory());
        Assert.assertEquals(SHOULD_GENERATE_EXCLUSIONS, actual.getShouldGenerateExclusions());
        Assert.assertEquals(PLUGIN_VERSION, actual.getPluginVersion());
        Assert.assertEquals(PLUGIN_TYPE, actual.getPluginType());
        Assert.assertEquals(rulesFileUrl, actual.getRulesFileUrl());
        Assert.assertEquals(exclusionsFileUrl, actual.getExclusionsFileUrl());
    }

    /**
     * Test failure of setting two rule configs.
     */
    @Test
    public void testBuilderFailureDoubleRulesConfig() {
        // arrange
        URL exclusionsFileUrl = null;
        boolean isExceptionThrown = false;

        try {
            // act
            PluginConfig.builder()
                    .changeLogFormat(CHANGE_LOG_FORMAT)
                    .pathToRulesFile(PATH_TO_RULES_FILE)
                    .pathToExclusionsFile(PATH_TO_EXCLUSIONS_FILE)
                    .changeLogDirectory(CHANGE_LOG_DIRECTORY)
                    .shouldGenerateExclusions(SHOULD_GENERATE_EXCLUSIONS)
                    .pluginVersion(PLUGIN_VERSION)
                    .pluginType(PLUGIN_TYPE)
                    .ruleFileUrl(RULE_FILE_URL)
                    .exclusionsFileUrl(exclusionsFileUrl)
                    .build();
        } catch (IllegalArgumentException e) {
            isExceptionThrown = true;
        }

        // assert
        Assert.assertTrue(isExceptionThrown);
    }

    /**
     * Test failure of setting two exclusion configs.
     */
    @Test
    public void testBuilderFailureDoubleExclusionsConfig() {
        // arrange
        URL rulesFileUrl = null;
        boolean isExceptionThrown = false;

        try {
            // act
            PluginConfig.builder()
                    .changeLogFormat(CHANGE_LOG_FORMAT)
                    .pathToRulesFile(PATH_TO_RULES_FILE)
                    .pathToExclusionsFile(PATH_TO_EXCLUSIONS_FILE)
                    .changeLogDirectory(CHANGE_LOG_DIRECTORY)
                    .shouldGenerateExclusions(SHOULD_GENERATE_EXCLUSIONS)
                    .pluginVersion(PLUGIN_VERSION)
                    .pluginType(PLUGIN_TYPE)
                    .ruleFileUrl(rulesFileUrl)
                    .exclusionsFileUrl(EXCLUSIONS_FILE_URL)
                    .build();
        } catch (IllegalArgumentException e) {
            isExceptionThrown = true;
        }

        // assert
        Assert.assertTrue(isExceptionThrown);
    }
}
