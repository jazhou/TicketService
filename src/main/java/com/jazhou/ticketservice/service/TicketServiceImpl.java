package com.jazhou.ticketservice.service;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import com.jazhou.ticketservice.annotation.ExpirationServiceEnabled;
import com.jazhou.ticketservice.common.Utils;
import com.jazhou.ticketservice.dao.SeatHoldRepository;
import com.jazhou.ticketservice.dao.SeatRepository;
import com.jazhou.ticketservice.dto.SeatHold;
import com.jazhou.ticketservice.exception.ExceedLimitException;
import com.jazhou.ticketservice.exception.ResourceNotFoundException;
import com.jazhou.ticketservice.model.SeatEntity;
import com.jazhou.ticketservice.model.SeatHoldEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implementation class of {@link TicketService}
 */
@Service
@Transactional
public class TicketServiceImpl implements TicketService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatHoldRepository seatHoldRepository;

    @Override
    @ExpirationServiceEnabled // clean up the expired seat holds
    public int numSeatsAvailable()
    {
        LOGGER.info("Retrieving number of seating available in the venue");
        return (int) seatRepository.findAllBySeatHoldNullOrderByRowAsc().size();
    }

    @Override
    @ExpirationServiceEnabled // clean up the expired seat holds
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail)
    {
        Assert.isTrue(numSeats > 0, "Parameter 'seatHoldId' must be a positive integer.");
        Assert.hasText(customerEmail, "Parameter 'customerEmail' must not be blank");

        LOGGER.info(String.format("Holding seats. numSeats=%d, customerEmail=%s", numSeats, customerEmail));

        List<SeatEntity> seatEntityList = seatRepository.findAllBySeatHoldNullOrderByRowAsc();
        // do we have enough seats ?
        if (seatEntityList.size() < numSeats)
        {
            throw new ExceedLimitException(String.format("Insufficient seats to accommodate user %s", customerEmail));
        }

        Timestamp now = Utils.getCurrentTimeStamp();
        SeatHoldEntity seatHoldEntity = new SeatHoldEntity();
        seatHoldEntity.setReserved(false);
        seatHoldEntity.setCreatedBy(customerEmail);
        seatHoldEntity.setCreatedOn(now);
        seatHoldEntity.setUpdatedBy(customerEmail);
        seatHoldEntity.setUpdatedOn(now);

        for (int num = 0; num < numSeats; num++)
        {
            // the seat list is sorted basd on the row number in ascending order. So the best seats will be picked up first.
            SeatEntity seatEntity = seatEntityList.get(num);
            seatEntity.setSeatHold(seatHoldEntity);
            seatHoldEntity.getSeats().add(seatEntity);
        }

        // flush the entity to ensure the id is generated
        seatHoldRepository.saveAndFlush(seatHoldEntity);

        return new SeatHold(seatHoldEntity.getId());
    }

    @Override
    @ExpirationServiceEnabled // clean up the expired seat holds
    public String reserveSeats(int seatHoldId, String customerEmail)
    {
        Assert.isTrue(seatHoldId >= 0, "Parameter 'seatHoldId' must be a non-negative integer.");
        Assert.hasText(customerEmail, "Parameter 'customerEmail' must not be blank");

        LOGGER.info(String.format("Reserving seats. seatHoldId=%d, customerEmail=%s", seatHoldId, customerEmail));

        SeatHoldEntity seatHoldEntity = seatHoldRepository.findById((long) seatHoldId)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("The seat hold %d does not exist.", seatHoldId))); //404

        // customer email does not match?? returns 404
        if (!seatHoldEntity.getCreatedBy().equals(customerEmail))
        {
            throw new ResourceNotFoundException(String.format("Cannot locate the seat hold with id=%d, customerEmail=%s", seatHoldId, customerEmail));
        }

        // already reserved ?? simply return the confirmation created previously
        if (!seatHoldEntity.isReserved())
        {
            // now set the seat hold as reserved
            seatHoldEntity.setReserved(true);
            seatHoldEntity.setUpdatedBy(customerEmail);
            seatHoldEntity.setUpdatedOn(Utils.getCurrentTimeStamp());
            seatHoldEntity.setConfirmationId(Utils.getUniqueId());
        }

        return seatHoldEntity.getConfirmationId();
    }
}
