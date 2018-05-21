package com.jazhou.ticketservice.service;

/**
 * Interface that provides all venue services
 */
public interface VenueService
{
    /**
     * Creates a venue with all seats available
     * @param numberOfRows - number of rows available in a venue
     * @param numberOfColumns - number of seats(columns) in each row in a venue
     */
    void createVenue(int numberOfRows, int numberOfColumns);
}
