package io.github.htshame.gateway;

import com.sun.net.httpserver.HttpServer;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Config API gateway test.
 */
public class ConfigApiGatewayTest {

    private static final int OK_200 = 200;

    /**
     * Test getting file from URL.
     *
     */
    @Test
    public void testGetFileHttp() {
        // arrange
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(0), 0);
        } catch (IOException e) {
            throw new AssertionError("Failed to start test HTTP server", e);
        }
        server.createContext("/rules.xml", exchange -> {
            byte[] body = "<rules/>".getBytes(java.nio.charset.StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/xml");
            exchange.sendResponseHeaders(OK_200, body.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body);
            }
        });
        server.start();
        try {
            URL url = new URL("http://localhost:" + server.getAddress().getPort() + "/rules.xml");

            // act
            File actual = new ConfigApiGateway().getFile(url);

            // assert
            Assert.assertTrue(actual.exists());
            Assert.assertTrue(actual.length() > 0);
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        } finally {
            server.stop(0);
        }
    }
}
