package com.v2com.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.v2com.dto.ReservationDTO;
import com.v2com.service.ReservationService;

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
    public Response createReservation(ReservationDTO reservation){
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Reservation registered!");
            response.put("reservation", reservationService.createReservation(reservation));
            return Response.ok(response).build();
        } catch (IllegalArgumentException il) {
            return Response.serverError().entity(il.getMessage()).build();
        } catch (Exception e) {
            return Response.ok().entity(e.getMessage()).build();
        }
    }

    // @GET
    // @Transactional
    // @Path("/{reservationId}")
    // public Response getReservationById(@PathParam("reservationId") UUID reservationId){
    //     try {
    //         Map<String, Object> response = new HashMap<>();
    //         response.put("message", "Reservation founded!");
    //         response.put("reservation", reservationService.getReservationById(reservationId));
    //         return Response.ok(response).build();
    //     } catch (Exception e) {
    //         return Response.serverError().entity(e.getMessage()).build();
    //     }
    // }

    // @GET
    // @Transactional
    // public Response getReservationsByFilters(@Context UriInfo uriInfo) {
    //     try {
    //         Map<String, String> filters = uriInfo.getQueryParameters().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));
    //         return Response.ok(reservationService.getReservationByFilters(filters)).build();
    //     } catch (Exception e) {
    //         return Response.serverError().entity(e.getMessage()).build();
    //     }
    // }

    // @DELETE
    // @Transactional
    // @Path("/{reservationId}")
    // public Response deleteBook(@PathParam("reservationId") UUID reservationId) {
    //     try {
    //         Map<String, Object> response = new HashMap<>();
    //         response.put("message", "reservation deleted!");
    //         response.put("reservation", reservationService.deleteReservation(reservationId));
    //         return Response.ok(response).build();
    //     } catch (Exception e) {
    //         return Response.serverError().entity(e.getMessage()).build();
    //     }
    // }

    // @PATCH
    // @Transactional
    // @Path("/{reservationId}")
    // public Response updateReservation(@PathParam("reservationId") UUID reservationId, ReservationDTO ReservationDTO) {
    //     try {
    //         Map<String, Object> response = new HashMap<>();
    //         response.put("message", "Book updated!");
    //         response.put("book", reservationService.updateReservation(reservationId, ReservationDTO));
    //         return Response.ok(response).build();
    //     } catch (Exception e) {
    //         return Response.serverError().entity(e.getMessage()).build();
    //     }
    // }

}