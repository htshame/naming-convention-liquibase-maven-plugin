package io.github.htshame;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidateChangeLogMojoFailureTest {

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
        setField("shouldFailBuild", true);
        setField("changeLogFormat", "xml");
    }

    /**
     * Integration test for {@link ValidateChangeLogMojo#execute()}. Should fail rule parsing.
     *
     * @throws NoSuchFieldException   - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Test
    public void testExecuteRuleParseFailure() throws NoSuchFieldException, IllegalAccessException {
        // arrange
        boolean isExceptionThrown = false;
        setField("changeLogDirectory", new File("src/test/resources/db/xml"));
        setField("pathToExclusionsFile", new File("src/test/resources/exclusions.xml"));
        setField("pathToRulesFile",
                new File("src/test/resources/io/github/htshame/failure/rules_failure.xml"));

        // act
        try {
            validateChangeLogMojo.execute();
        } catch (MojoExecutionException e) {
            // assert
            isExceptionThrown = true;
            assertEquals("Error parsing ruleset XML file. Message: Content is not allowed in prolog.",
                    e.getMessage());
        }
        assertTrue(isExceptionThrown);
    }

    /**
     * Integration test for {@link ValidateChangeLogMojo#execute()}. Should fail exclusion parsing.
     *
     * @throws NoSuchFieldException   - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Test
    public void testExecuteExclusionParseFailure() throws NoSuchFieldException, IllegalAccessException {
        // arrange
        boolean isExceptionThrown = false;
        setField("changeLogDirectory", new File("src/test/resources/db/xml"));
        setField("pathToRulesFile", new File("src/test/resources/rules.xml"));
        setField("pathToExclusionsFile",
                new File("src/test/resources/io/github/htshame/failure/exclusions_failure.xml"));

        // act
        try {
            validateChangeLogMojo.execute();
        } catch (MojoExecutionException e) {
            // assert
            isExceptionThrown = true;
            assertEquals("Error parsing exclusion XML file", e.getMessage());
        }
        assertTrue(isExceptionThrown);
    }

    /**
     * Integration test for {@link ValidateChangeLogMojo#execute()}. Should fail changeLog parsing.
     *
     * @throws NoSuchFieldException   - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Test
    public void testExecuteChangeLogParseFailure() throws NoSuchFieldException, IllegalAccessException {
        // arrange
        boolean isExceptionThrown = false;
        setField("changeLogDirectory",
                new File("src/test/resources/io/github/htshame/failure/changeLog"));
        setField("pathToRulesFile", new File("src/test/resources/rules.xml"));
        setField("pathToExclusionsFile", new File("src/test/resources/exclusions.xml"));

        // act
        try {
            validateChangeLogMojo.execute();
        } catch (MojoExecutionException e) {
            // assert
            isExceptionThrown = true;
            assertEquals("Validation failed: 3 violation(s) found.", e.getMessage());
        }
        assertTrue(isExceptionThrown);
    }

    /**
     * Integration test for {@link ValidateChangeLogMojo#execute()}. Should fail because of unsupported format.
     *
     * @throws NoSuchFieldException   - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Test
    public void testExecuteWrongFormatFailure() throws NoSuchFieldException, IllegalAccessException {
        // arrange
        boolean isExceptionThrown = false;
        setField("pathToRulesFile", new File("src/test/resources/rules.xml"));
        setField("pathToExclusionsFile", new File("src/test/resources/exclusions.xml"));
        setField("changeLogDirectory", new File("src/test/resources/db/xml"));
        setField("changeLogFormat", "xml1");

        // act
        try {
            validateChangeLogMojo.execute();
        } catch (MojoExecutionException e) {
            // assert
            isExceptionThrown = true;
            assertEquals("ChangeLog format [xml1] is not supported", e.getMessage());
        }
        assertTrue(isExceptionThrown);
    }

    /**
     * Integration test for {@link ValidateChangeLogMojo#execute()}. Should fail because of wrong changeLog directory.
     *
     * @throws NoSuchFieldException   - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Test
    public void testExecuteWrongChangeLogDirectoryFailure() throws NoSuchFieldException, IllegalAccessException {
        // arrange
        boolean isExceptionThrown = false;
        String changeLogPath = "src\\test\\resources\\db\\xml1";
        setField("pathToRulesFile", new File("src/test/resources/rules.xml"));
        setField("pathToExclusionsFile", new File("src/test/resources/exclusions.xml"));
        setField("changeLogDirectory", new File(changeLogPath));

        // act
        try {
            validateChangeLogMojo.execute();
        } catch (MojoExecutionException e) {
            // assert
            isExceptionThrown = true;
            assertEquals("Invalid path: " + changeLogPath, e.getMessage());
        }
        assertTrue(isExceptionThrown);
    }

    /**
     * Integration test for {@link ValidateChangeLogMojo#execute()}. Should fail because of wrong rule file path.
     *
     * @throws NoSuchFieldException   - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Test
    public void testExecuteWrongRuleFilePathFailure() throws NoSuchFieldException, IllegalAccessException {
        // arrange
        boolean isExceptionThrown = false;
        String wrongRulePath = "src\\test\\resources\\rules.xml111";
        setField("pathToRulesFile", new File(wrongRulePath));
        setField("pathToExclusionsFile", new File("src/test/resources/exclusions.xml"));
        setField("changeLogDirectory", new File("src/test/resources/db/xml"));

        // act
        try {
            validateChangeLogMojo.execute();
        } catch (MojoExecutionException e) {
            // assert
            isExceptionThrown = true;
            assertEquals("Invalid path: " + wrongRulePath, e.getMessage());
        }
        assertTrue(isExceptionThrown);
    }

    /**
     * Integration test for {@link ValidateChangeLogMojo#execute()}. Should fail because of wrong exclusion file path.
     *
     * @throws NoSuchFieldException   - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Test
    public void testExecuteWrongExclusionFilePathFailure() throws NoSuchFieldException, IllegalAccessException {
        // arrange
        boolean isExceptionThrown = false;
        String wrongExclusionPath = "src\\test\\resources\\exclusions.xml111";
        setField("pathToRulesFile", new File("src/test/resources/rules.xml"));
        setField("pathToExclusionsFile", new File(wrongExclusionPath));
        setField("changeLogDirectory", new File("src/test/resources/db/xml"));

        // act
        try {
            validateChangeLogMojo.execute();
        } catch (MojoExecutionException e) {
            // assert
            isExceptionThrown = true;
            assertEquals("Invalid path: " + wrongExclusionPath, e.getMessage());
        }
        assertTrue(isExceptionThrown);
    }

    /**
     * Integration test for {@link ValidateChangeLogMojo#execute()}.
     * Should not fail because of empty rule set.
     *
     * @throws NoSuchFieldException   - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Test
    public void testExecuteEmptyRulesSuccess() throws NoSuchFieldException, IllegalAccessException {
        // arrange
        boolean isExceptionThrown = false;
        setField("pathToRulesFile",
                new File("src/test/resources/io/github/htshame/failure/rules_empty.xml"));
        setField("pathToExclusionsFile", null);
        setField("changeLogDirectory", new File("src/test/resources/db/xml"));
        setField("changeLogFormat", "xml");

        // act
        try {
            validateChangeLogMojo.execute();
        } catch (MojoExecutionException e) {
            // assert
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
