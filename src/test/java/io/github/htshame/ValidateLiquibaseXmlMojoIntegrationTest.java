package io.github.htshame;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

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

        // act
        try {
            validateLiquibaseXmlMojo.execute();
        } catch (MojoExecutionException ae) {
            // assert
            assertEquals("Validation failed: 7 violation(s) found.", ae.getMessage());
        }
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
