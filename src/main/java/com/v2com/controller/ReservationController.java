package com.v2com.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.v2com.Exceptions.BookNotFoundException;
import com.v2com.Exceptions.FilterInvalidException;
import com.v2com.Exceptions.LoanNotFoundException;
import com.v2com.Exceptions.NoLoansFoundException;
import com.v2com.Exceptions.ReservationDateIsNullException;
import com.v2com.Exceptions.ReservationNotFoundException;
import com.v2com.Exceptions.UserAlreadyReservedException;
import com.v2com.Exceptions.UserNotFoundException;
import com.v2com.dto.ReservationDTO;
import com.v2com.service.ReservationService;

import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("api/v1/reservations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @POST
    @Transactional
    @PermitAll
    public Response createReservation(ReservationDTO reservation){
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Reservation registered!");
            response.put("reservation", reservationService.createReservation(reservation));
            return Response.status(201).entity(response).build();
        } catch (UserNotFoundException | BookNotFoundException | ReservationNotFoundException notFound) {
            return Response.status(404).entity(notFound.getMessage()).build();
        } catch (ReservationDateIsNullException reservationDate) {
            return Response.status(409).entity(reservationDate.getMessage()).build();
        } catch (NoLoansFoundException LoanNotFound) {
            return Response.status(201).entity(LoanNotFound.getMessage()).build();
        } catch (UserAlreadyReservedException alreadyReserv) {
            return Response.status(403).entity(alreadyReserv.getMessage()).build();
        } catch (Exception e) {
            return Response.ok().entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    @PermitAll
    @Path("/{reservationId}")
    public Response getReservationById(@PathParam("reservationId") UUID reservationId){
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Reservation founded!");
            response.put("reservation", reservationService.getReservationById(reservationId));
            return Response.ok(response).build();
        } catch (ReservationNotFoundException notFound) {
            return Response.status(404).entity(notFound.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    @PermitAll
    public Response getReservationsByFilters(@Context UriInfo uriInfo) {
        try {
            Map<String, String> filters = uriInfo.getQueryParameters().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));
            return Response.ok(reservationService.getReservationsByFilters(filters)).build();
        } catch (ReservationNotFoundException notFound) {
            return Response.status(404).entity(notFound.getMessage()).build();
        } catch (FilterInvalidException invalid) {
            return Response.status(406).entity(invalid.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Transactional
    @PermitAll
    @Path("/{reservationId}")
    public Response deleteBook(@PathParam("reservationId") UUID reservationId) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "reservation deleted!");
            response.put("reservation", reservationService.deleteReservation(reservationId));
            return Response.status(410).entity(response).build();
        } catch (ReservationNotFoundException | UserNotFoundException | BookNotFoundException notFound) {
            return Response.status(404).entity(notFound.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Transactional
    @PermitAll
    @Path("/{reservationId}")
    public Response updateReservation(@PathParam("reservationId") UUID reservationId, ReservationDTO reservationDTO) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Reservation updated!");
            response.put("reservation", reservationService.updateReservation(reservationId, reservationDTO));
            return Response.ok(response).build();
        } catch (LoanNotFoundException | UserNotFoundException | BookNotFoundException notFound) {
            return Response.status(404).entity(notFound.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

}