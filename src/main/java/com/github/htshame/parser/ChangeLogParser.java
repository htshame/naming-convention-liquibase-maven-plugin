package com.github.htshame.parser;

import com.github.htshame.exception.ChangeLogCollectorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChangeLogParser {

    private static final String XML_EXTENSION = ".xml";

    public static List<File> collectChangeLogFiles(File changeLogFilesPath) throws ChangeLogCollectorException {
        try (Stream<Path> paths = Files.walk(changeLogFilesPath.toPath())) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(XML_EXTENSION))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ChangeLogCollectorException("Failed to walk directory: " + changeLogFilesPath, e);
        }
    }
}
