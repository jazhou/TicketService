package com.jazhou.ticketservice.dto;

import java.io.Serializable;

/**
 * Seat hold create request DTO
 */
public class SeatHoldCreateRequest implements Serializable
{
    private int numberOfSeats;

    private String email;

    public int getNumberOfSeats()
    {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats)
    {
        this.numberOfSeats = numberOfSeats;
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
