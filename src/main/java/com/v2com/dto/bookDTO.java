package com.v2com.dto;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

public class bookDTO {

    private UUID bookId;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Author is required")
    private String author;

    private String isbn = "";
    private Date publicationDate = new Date();

    @NotEmpty(message = "Availability is required")
    private Boolean isAvailable;

    public bookDTO() {
    }

    public bookDTO(UUID bookId, String title, String author, String isbn, Date publicationDate, Boolean isAvailable) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.isAvailable = isAvailable;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}