package io.github.htshame;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidateChangeLogMojoYamlIntegrationTest {

    private ValidateChangeLogMojo validateChangeLogMojo;

    /**
     * Init.
     *
     * @throws NoSuchFieldException   - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        validateChangeLogMojo = new ValidateChangeLogMojo();
        setField("pathToRulesFile", new File("src/test/resources/rules.xml"));
        setField("pathToExclusionsFile", new File("src/test/resources/exclusions.xml"));
        setField("changeLogDirectory", new File("src/test/resources/db/yaml"));
        setField("shouldFailBuild", true);
        setField("changeLogFormat", "yaml");
    }

    /**
     * Integration test for {@link ValidateChangeLogMojo#execute()}.
     */
    @Test
    public void testExecute() {
        // arrange
        boolean isExceptionThrown = false;

        // act
        try {
            validateChangeLogMojo.execute();
        } catch (MojoExecutionException e) {
            // assert
            isExceptionThrown = true;
            assertEquals("Validation failed: 10 violation(s) found.", e.getMessage());
        }
        assertTrue(isExceptionThrown);
    }

    /**
     * Integration test for {@link ValidateChangeLogMojo#execute()}. Should not fail the build.
     *
     * @throws NoSuchFieldException   - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Test
    public void testExecuteFailureShouldNotFailBuildFalse() throws NoSuchFieldException, IllegalAccessException {
        // arrange
        setField("shouldFailBuild", false);
        boolean isExceptionThrown = false;

        // act
        try {
            validateChangeLogMojo.execute();
        } catch (MojoExecutionException e) {
            isExceptionThrown = true;
        }

        assertFalse(isExceptionThrown);
    }

    private void setField(final String fieldName,
                          final Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field ruleSetPath = validateChangeLogMojo.getClass().getDeclaredField(fieldName);
        ruleSetPath.setAccessible(true);
        ruleSetPath.set(validateChangeLogMojo, value);
    }
}
