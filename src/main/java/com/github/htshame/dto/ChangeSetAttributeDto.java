package com.github.htshame.dto;

public class ChangeSetAttributeDto {

    private String id;

    private String author;

    public ChangeSetAttributeDto(String id, String author) {
        this.id = id;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
