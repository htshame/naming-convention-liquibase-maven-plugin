package io.github.htshame.change.log;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.exception.ChangeLogParseException;

import java.io.File;
import java.util.List;

/**
 * ChangeLog parser interface.
 */
public interface ChangeLogParser {

    /**
     * ChangeLog file parser.
     *
     * @param changeLogFile - changeLog file.
     * @return list of changeSets.
     * @throws ChangeLogParseException - thrown in parsing fails.
     */
    List<ChangeSetElement> parseChangeLog(File changeLogFile) throws ChangeLogParseException;
}
