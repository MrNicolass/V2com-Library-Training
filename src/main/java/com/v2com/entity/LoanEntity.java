package com.v2com.entity;

import java.sql.Date;
import java.util.UUID;

import com.v2com.entity.enums.LoanStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "loans")
public class LoanEntity {

    //region Properties

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID loanId;

    @NotNull
    @ManyToOne
    private UserEntity user;

    @NotNull
    @ManyToOne
    private BookEntity book;

    @NotNull
    private Date loanDate = new java.sql.Date(System.currentTimeMillis());
    
    //By default, due date is the date when the book as loaned + 30 days
    private Date loanDueDate = new java.sql.Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);

    private Date returnDate;

    private LoanStatus loanStatus = LoanStatus.ACTIVE;

    @Override
    public String toString() {
        return "LoanEntity{" +
                "loanId=" + loanId +
                ", user='" + user + '\'' +
                ", book='" + book + '\'' +
                ", loanDate='" + loanDate + '\'' +
                ", loanDueDate=" + loanDueDate +
                ", returnDate=" + returnDate +
                ", loanStatus=" + loanStatus +
                '}';
    }

    //endregion

    //region constructors

    public LoanEntity(){}

    public LoanEntity(UserEntity user, BookEntity book, Date loanDate, Date loanDueDate, Date returnDate, LoanStatus loanStatus) {
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.loanDueDate = loanDueDate;
        this.returnDate = returnDate;
        this.loanStatus = loanStatus;
    }

    public LoanEntity(UserEntity user, BookEntity book) {
        this(user, book, new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000), null, LoanStatus.ACTIVE);
    }

    //endregion

    //region Getters and Setters

    public UUID getLoanId() {
        return loanId;
    }

    public void setLoanId(UUID loanId) {
        this.loanId = loanId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getLoanDueDate() {
        return loanDueDate;
    }

    public void setLoanDueDate(Date loanDueDate) {
        this.loanDueDate = loanDueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    //endregion
}