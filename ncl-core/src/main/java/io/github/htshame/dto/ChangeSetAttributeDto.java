package io.github.htshame.dto;

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
}
