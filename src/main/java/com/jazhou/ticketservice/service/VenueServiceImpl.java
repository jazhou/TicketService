package com.jazhou.ticketservice.service;

import java.sql.Timestamp;

import javax.transaction.Transactional;

import com.jazhou.ticketservice.common.Utils;
import com.jazhou.ticketservice.dao.SeatRepository;
import com.jazhou.ticketservice.exception.ResourceAlreadyExistException;
import com.jazhou.ticketservice.model.SeatEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implementation class of {@link VenueService}
 */
@Service
@Transactional
public class VenueServiceImpl implements VenueService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public void createVenue(int numberOfRows, int numberOfColumns)
    {
        Assert.isTrue(numberOfRows > 0, "Parameter 'numberOfRows' must be a positive integer");
        Assert.isTrue(numberOfColumns > 0, "Parameter 'numberOfColumns' must be a positive integer");

        LOGGER.info("Creating a venue...");
        if (seatRepository.findAll().size() > 0)
        {
            throw new ResourceAlreadyExistException(String.format("Cannot create a venue since it already exists!"));
        }

        for (int row = 0; row < numberOfRows; row++)
        {
            for (int column = 0; column < numberOfColumns; column++)
            {
                SeatEntity seatEntity = new SeatEntity();
                seatEntity.setRow((long) row);
                seatEntity.setColumn((long) column);
                Timestamp now = Utils.getCurrentTimeStamp();
                seatEntity.setCreatedOn(now);
                seatEntity.setUpdatedOn(now);
                seatRepository.save(seatEntity);
            }
        }
    }
}
