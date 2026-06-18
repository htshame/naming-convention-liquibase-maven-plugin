package io.github.htshame.change.parser;

import io.github.htshame.change.element.ChangeLogElement;
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
     * @throws ChangeLogParseException - thrown if parsing fails.
     */
    List<ChangeLogElement> parseChangeSets(File changeLogFile) throws ChangeLogParseException;

    /**
     * Parse non-changeSet elements of changeLog.
     *
     * @param changeLogFile - changeLog file.
     * @return list of non-changeSet elements.
     * @throws ChangeLogParseException - if parsing goes wrong.
     */
    List<ChangeLogElement> parseNonChangeSets(File changeLogFile) throws ChangeLogParseException;
}
