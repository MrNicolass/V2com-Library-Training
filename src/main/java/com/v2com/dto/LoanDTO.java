package com.v2com.dto;

import java.sql.Date;
import java.util.UUID;

import com.v2com.entity.enums.LoanStatus;

import jakarta.validation.constraints.NotNull;

public class LoanDTO {
    
    //region Properties

    private UUID loanId;

    @NotNull(message = "At least one user is required!")
    private UUID userId;

    @NotNull(message = "At least one book is required!")
    private UUID bookId;

    @NotNull(message = "loan date is required!")
    private Date loanDate;

    private Date loanDueDate = new java.sql.Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);

    private Date returnDate;

    private LoanStatus loanStatus = LoanStatus.ACTIVE;

    //endregion

    //region Constructors

    public LoanDTO(){}

    public LoanDTO(UUID loanId, UUID userId, UUID bookId, Date loanDate, Date loanDueDate, Date returnDate, LoanStatus loanStatus){
        this.loanId = loanId;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.loanDueDate = loanDueDate;
        this.returnDate = returnDate;
        this.loanStatus = loanStatus;
    }

    //endregion

    //region Getters and Setters

    public UUID getLoanId() {
        return loanId;
    }

    public void setLoanId(UUID loanId) {
        this.loanId = loanId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
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

    public void setLoanStatus(LoanStatus loadStatus) {
        this.loanStatus = loadStatus;
    }

    //endregion
}