package io.github.htshame;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidateLiquibaseXmlMojoIntegrationTest {

    /**
     * Integration test for {@link ValidateLiquibaseXmlMojo#execute()}.
     *
     * @throws NoSuchFieldException - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Test
    public void testExecute() throws NoSuchFieldException, IllegalAccessException {
        // arrange
        ValidateLiquibaseXmlMojo validateLiquibaseXmlMojo = new ValidateLiquibaseXmlMojo();
        setField(validateLiquibaseXmlMojo, "pathToRulesFile", new File("src/test/resources/rules.xml"));
        setField(validateLiquibaseXmlMojo, "pathToExclusionsFile", new File("src/test/resources/exclusions.xml"));
        setField(validateLiquibaseXmlMojo, "changeLogDirectory", new File("src/test/resources/db"));
        setField(validateLiquibaseXmlMojo, "shouldFailBuild", true);
        boolean isExceptionThrown = false;

        // act
        try {
            validateLiquibaseXmlMojo.execute();
        } catch (MojoExecutionException ae) {
            // assert
            isExceptionThrown = true;
            assertEquals("Validation failed: 14 violation(s) found.", ae.getMessage());
        }
        assertTrue(isExceptionThrown);
    }

    /**
     * Integration test for {@link ValidateLiquibaseXmlMojo#execute()}. Should not fail the build.
     *
     * @throws NoSuchFieldException - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Test
    public void testExecuteFailureShouldNotFailBuildFalse() throws NoSuchFieldException, IllegalAccessException {
        // arrange
        ValidateLiquibaseXmlMojo validateLiquibaseXmlMojo = new ValidateLiquibaseXmlMojo();
        setField(validateLiquibaseXmlMojo, "pathToRulesFile", new File("src/test/resources/rules.xml"));
        setField(validateLiquibaseXmlMojo, "pathToExclusionsFile", new File("src/test/resources/exclusions.xml"));
        setField(validateLiquibaseXmlMojo, "changeLogDirectory", new File("src/test/resources/db"));
        setField(validateLiquibaseXmlMojo, "shouldFailBuild", false);
        boolean isExceptionThrown = false;

        // act
        try {
            validateLiquibaseXmlMojo.execute();
        } catch (MojoExecutionException e) {
            isExceptionThrown = true;
        }

        assertFalse(isExceptionThrown);
    }

    private void setField(final ValidateLiquibaseXmlMojo validateLiquibaseXmlMojo,
                          final String fieldName,
                          final Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field ruleSetPath = validateLiquibaseXmlMojo.getClass().getDeclaredField(fieldName);
        ruleSetPath.setAccessible(true);
        ruleSetPath.set(validateLiquibaseXmlMojo, value);
    }
}
