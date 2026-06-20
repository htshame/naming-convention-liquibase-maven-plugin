package io.github.htshame.gateway;

import io.github.htshame.exception.ConfigApiGatewayException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Configuration API gateway.
 */
public class ConfigApiGateway {

    private static final int OK_200 = 200;
    private static final int BUFFER_SIZE = 8192;

    /**
     * Default constructor.
     */
    public ConfigApiGateway() {

    }

    /**
     * Get config file.
     *
     * @param configFileUrl - config file URL
     * @return config file.
     */
    public File getFile(final URL configFileUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) configFileUrl.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            connection.setInstanceFollowRedirects(true);

            int status = connection.getResponseCode();
            if (status != OK_200) {
                throw new ConfigApiGatewayException("Error reading configuration content from URL: " + configFileUrl
                        + ". Status code != 200");
            }

            File xmlFile = File.createTempFile("api-response-", ".xml");

            try (InputStream in = connection.getInputStream();
                 FileOutputStream out = new FileOutputStream(xmlFile)) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                connection.disconnect();
            }
            return xmlFile;
        } catch (IOException e) {
            throw new ConfigApiGatewayException("Error getting config file from URL: " + configFileUrl
                    + ". Error message: " + e.getMessage());
        }
    }
}
