package com.v2com.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.v2com.dto.LoanDTO;
import com.v2com.service.LoanService;

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

@Path("/api/v1/loans")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoanController {
    
    private final LoanService loanService;

    public LoanController(LoanService loanService){
        this.loanService = loanService;
    }

    @POST
    @Transactional
    public Response createLoan(LoanDTO loan){
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Loan registered!");
            response.put("loan", loanService.createLoan(loan));
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    @Path("/{loanId}")
    public Response getLoanById(@PathParam("loanId") UUID loanId){
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Loan founded!");
            response.put("loan", loanService.getLoanById(loanId));
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    public Response getLoansByFilters(@Context UriInfo uriInfo) {
        try {
            Map<String, String> filters = uriInfo.getQueryParameters().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));
            return Response.ok(loanService.getLoansByFilters(filters)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{loanId}")
    public Response deleteBook(@PathParam("loanId") UUID loanId) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Loan deleted!");
            response.put("loan", loanService.deleteLoan(loanId));
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Transactional
    @Path("/{loanId}")
    public Response updateLoan(@PathParam("loanId") UUID loanId, LoanDTO loanDTO) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book updated!");
            response.put("book", loanService.updateLoan(loanId, loanDTO));
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}