package com.jazhou.ticketservice.service;

import java.sql.Timestamp;

import com.jazhou.ticketservice.dao.SeatHoldRepository;
import com.jazhou.ticketservice.model.SeatHoldEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation class of {@link ExpireService}
 */
@Service
@Transactional
public class ExpireServiceImpl implements ExpireService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpireServiceImpl.class);

    @Autowired
    private SeatHoldRepository seatHoldRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public synchronized  void deleteExpiredSeatHolds(Timestamp now, int durationInSeconds)
    {
        Assert.notNull(now, "Parameter 'now' must not be null");
        Assert.notNull(durationInSeconds > 0, "Parameter 'durationInSeconds' must be a positive integer");

        LOGGER.info("Running deleting expired seat holds");

        Timestamp earliestActiveTime = new Timestamp(now.getTime() - durationInSeconds * 1000);
        seatHoldRepository.findAllExpiredSeatHolds(earliestActiveTime).stream().forEach(seatHoldEntity ->
        {
            LOGGER.info(String.format("Deleting seat hold id=%d", seatHoldEntity.getId()));

            deleteSeatHoldEntity(seatHoldEntity);
        });

    }

    private void deleteSeatHoldEntity(SeatHoldEntity seatHoldEntity) {
        seatHoldEntity.getSeats()
            .stream()
            .forEach(seatEntity -> {
                seatEntity.setSeatHold(null);
            });
        seatHoldEntity.getSeats().clear();
        seatHoldRepository.delete(seatHoldEntity);
    }
}
