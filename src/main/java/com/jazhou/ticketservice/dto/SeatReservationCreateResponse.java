package com.jazhou.ticketservice.dto;

import java.io.Serializable;

/**
 * The response to the user who makes a reservation request
 */
public class SeatReservationCreateResponse implements Serializable
{
    private String confirmationId;

    public SeatReservationCreateResponse(String confirmationId) {
        this.confirmationId = confirmationId;
    }

    public String getConfirmationId()
    {
        return confirmationId;
    }

    public void setConfirmationId(String confirmationId)
    {
        this.confirmationId = confirmationId;
    }
}
