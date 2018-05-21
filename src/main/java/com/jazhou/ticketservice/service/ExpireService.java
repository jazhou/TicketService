package com.jazhou.ticketservice.service;

import java.sql.Timestamp;

/**
 * Created by k23754 on 5/19/18.
 */
public interface ExpireService
{
    void deleteExpiredSeatHolds(Timestamp now, int durationInSeconds);
}
