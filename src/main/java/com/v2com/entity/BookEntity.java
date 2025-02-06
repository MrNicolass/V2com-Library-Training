package com.v2com.entity;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "books")
public class BookEntity {

    //region Properties

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookId;

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    private String isbn = "";

    private Date publicationDate;

    @NotEmpty
    private Boolean isAvailable;

    //endregion

    //region Constructors

    public BookEntity() {
    }

    public BookEntity(String title, String author, String isbn, Date publicationDate, Boolean isAvailable) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.isAvailable = isAvailable;
    }

    //endregion
    
    //region Getters and Setters

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

    //endregion

}