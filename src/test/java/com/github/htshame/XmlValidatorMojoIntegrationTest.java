package com.github.htshame;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class XmlValidatorMojoIntegrationTest {

    /**
     * Integration test for {@link XmlValidatorMojo#execute()}.
     *
     * @throws NoSuchFieldException - thrown if required field is missing.
     * @throws IllegalAccessException - thrown if files not found.
     */
    @Test
    public void testExecute() throws NoSuchFieldException, IllegalAccessException {
        // arrange
        XmlValidatorMojo xmlValidatorMojo = new XmlValidatorMojo();
        setField(xmlValidatorMojo, "pathToRulesFile", new File("src/test/resources/rules.xml"));
        setField(xmlValidatorMojo, "pathToExclusionsFile", new File("src/test/resources/exclusions.xml"));
        setField(xmlValidatorMojo, "changeLogDirectory", new File("src/test/resources/db"));

        // act
        try {
            xmlValidatorMojo.execute();
        } catch (MojoExecutionException ae) {
            // assert
            assertEquals("Validation failed: 4 violation(s) found.", ae.getMessage());
        }
    }

    private void setField(final XmlValidatorMojo xmlValidatorMojo,
                          final String fieldName,
                          final Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field ruleSetPath = xmlValidatorMojo.getClass().getDeclaredField(fieldName);
        ruleSetPath.setAccessible(true);
        ruleSetPath.set(xmlValidatorMojo, value);
    }
}
