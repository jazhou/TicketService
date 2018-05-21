package com.jazhou.ticketservice.dto;

import java.io.Serializable;

/**
 * A simple DTO that holds the id of seat hold entity object
 */
public class SeatHold implements Serializable
{
    private Long seatHoldId;

    public SeatHold(Long seatHoldId)
    {
        this.seatHoldId = seatHoldId;
    }

    public Long getSeatHoldId()
    {
        return seatHoldId;
    }

    public void setSeatHoldId(Long seatHoldId)
    {
        this.seatHoldId = seatHoldId;
    }
}
