package io.github.htshame.util;

import io.github.htshame.enums.ChangeLogFormatEnum;
import io.github.htshame.exception.ChangeLogCollectorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collects all XML files within the given changeLog directory.
 */
public final class ChangeLogFilesCollector {

    private ChangeLogFilesCollector() {

    }

    /**
     * Collect XML files from the changeLog directory.
     *
     * @param changeLogFilesPath - path to changeLog files.
     * @param fileExtension      - file extension.
     * @return list of changeLog files.
     * @throws ChangeLogCollectorException - thrown in case collection fails.
     */
    public static List<File> collectChangeLogFiles(final File changeLogFilesPath,
                                                   final ChangeLogFormatEnum fileExtension)
            throws ChangeLogCollectorException {
        try (Stream<Path> paths = Files.walk(changeLogFilesPath.toPath())) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith("." + fileExtension.getValue()))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ChangeLogCollectorException("Failed to walk directory: " + changeLogFilesPath, e);
        }
    }
}
