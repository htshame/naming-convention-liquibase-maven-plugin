package io.github.htshame.gateway;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Config API gateway test.
 */
public class ConfigApiGatewayTest {

    /**
     * Test getting file from URL.
     *
     * @throws MalformedURLException - malformed URL exception.
     */
    @Test
    public void testGerFile() throws MalformedURLException {
        // arrange
        URL url = new URL("https://raw.githubusercontent.com/htshame/naming-convention-liquibase-maven-plugin/"
                + "refs/heads/main/docs/schema/example/rules_example.xml");

        // act
        File actual = new ConfigApiGateway().getFile(url);

        // assert
        Assert.assertNotNull(actual);
    }
}
