package com.jazhou.ticketservice.dto;

import java.io.Serializable;

/**
 * Seat reservation create request DTO
 */
public class SeatReservationCreateRequest implements Serializable
{
    private int seatHoldId;

    private String email;

    public int getSeatHoldId()
    {
        return seatHoldId;
    }

    public void setSeatHoldId(int seatHoldId)
    {
        this.seatHoldId = seatHoldId;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
