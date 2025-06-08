package com.github.htshame.dto;

/**
 * Class to represent changeSet attributes.
 */
public class ChangeSetAttributeDto {

    private String id;

    private String author;

    /**
     * Constructor.
     *
     * @param id     - changeSet id.
     * @param author - changeSet author.
     */
    public ChangeSetAttributeDto(final String id,
                                 final String author) {
        this.id = id;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }
}
