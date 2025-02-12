package com.v2com.entity;

import java.sql.Date;
import java.util.UUID;

import com.v2com.entity.enums.ReservationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "reservations")
public class ReservationEntity {

    //region Properties

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reservationId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "bookId")
    private BookEntity book;

    @NotNull
    private Date reservationDate = new java.sql.Date(System.currentTimeMillis());

    @NotNull
    private ReservationStatus status = ReservationStatus.PENDING;

    @Override
    public String toString() {
        return "ReservationEntity{" +
                "reservationId=" + reservationId +
                ", user=" + user +
                ", book=" + book +
                ", reservationDate=" + reservationDate +
                ", status=" + status +
                '}';
    }

    //endregion

    //region Constructors

    public ReservationEntity(){}

    public ReservationEntity(UserEntity user, BookEntity book, Date reservationDate, ReservationStatus status){
        this.user = user;
        this.book = book;
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