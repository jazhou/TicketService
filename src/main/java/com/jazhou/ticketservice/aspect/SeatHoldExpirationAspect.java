package com.jazhou.ticketservice.aspect;

import com.jazhou.ticketservice.common.Constants;
import com.jazhou.ticketservice.common.Utils;
import com.jazhou.ticketservice.service.ExpireService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Aspect that knows how to delete expired seat holds
 */
@Aspect
@Component
public class SeatHoldExpirationAspect
{
    @Autowired
    private ExpireService expireService;

    @Before("@annotation(com.jazhou.ticketservice.annotation.ExpirationServiceEnabled)")
    public void cleanUpExpiredSeatHolds(JoinPoint joinPoint) {
        expireService.deleteExpiredSeatHolds(Utils.getCurrentTimeStamp(), Constants.SEAT_HOLD_DURATION_IN_SECONDS);
    }

}
