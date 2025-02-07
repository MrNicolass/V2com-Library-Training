package com.v2com.dto;

import java.sql.Date;
import java.util.UUID;

import com.v2com.entity.enums.ReservationStatus;

import jakarta.validation.constraints.NotNull;

public class ReservationDTO {

    //region Properties

    private UUID reservationId;

    @NotNull(message = "At least one user is required!")
    private UUID userId;

    @NotNull(message = "At least one book is required!")
    private UUID bookId;

    @NotNull(message = "loan date is required!")
    private Date reservationDate = new java.sql.Date(System.currentTimeMillis());

    @NotNull
    private ReservationStatus status = ReservationStatus.PENDING;

    //endregion

    //region Constructors

    public ReservationDTO() {}

    public ReservationDTO(UUID reservationId, UUID userId, UUID bookId, Date reservationDate, ReservationStatus status) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.bookId = bookId;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    //endregion

    //region Getters and Setters

    public UUID getReservationId() {
        return reservationId;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
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

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    //endregion
}