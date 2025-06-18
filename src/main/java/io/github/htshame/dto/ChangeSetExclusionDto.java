package io.github.htshame.dto;

import java.util.Objects;

/**
 * Class to represent changeSet exclusion.
 */
public class ChangeSetExclusionDto {

    private String fileName;

    private String id;

    private String author;

    /**
     * Constructor.
     *
     * @param fileName - changeSet file name.
     * @param id       - changeSet id.
     * @param author   - changeSet author.
     */
    public ChangeSetExclusionDto(final String fileName,
                                 final String id,
                                 final String author) {
        this.fileName = fileName;
        this.id = id;
        this.author = author;
    }

    /**
     * File name.
     *
     * @return file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set file name.
     *
     * @param fileName - file name.
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get id.
     *
     * @return id.
     */
    public String getId() {
        return id;
    }

    /**
     * Set id.
     *
     * @param id - id.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Get author.
     *
     * @return author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set author.
     *
     * @param author - author.
     */
    public void setAuthor(final String author) {
        this.author = author;
    }

    /**
     * Equals.
     *
     * @param o - object.
     * @return true if equals, false - if not.
     */
    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChangeSetExclusionDto that = (ChangeSetExclusionDto) o;
        return Objects.equals(fileName, that.fileName)
                && Objects.equals(id, that.id)
                && Objects.equals(author, that.author);
    }

    /**
     * Hash code.
     *
     * @return hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(fileName, id, author);
    }
}
