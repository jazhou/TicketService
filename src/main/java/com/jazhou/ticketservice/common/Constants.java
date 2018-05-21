package com.jazhou.ticketservice.common;

/**
 * Collected constants of general utility
 */
public final class Constants
{
    /**
     * A seat hold can be only kept for a number of seconds. After that the seat hold should be deleted, and all its seats should become available again.
     */
    public static final int SEAT_HOLD_DURATION_IN_SECONDS = 60;

    /**
     * REST ENDPOINT URLs
     */
    public static final String BASE_URL = "/TicketService/v1";

    public static final String CREATE_VENUE_URL = "/venues/rows/{numOfRows}/columns/{numOfColumns}";

    public static final String AVAILABLE_SEATS_URL = "/seats";

    public static final String CREATE_SEAT_HOLD_URL = "/seatHolds";

    public static final String CREATE_SEAT_RESERVATION_URL = "/seatReservations";

    /**
     * Error messages to the user in case of exception
     */
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Sorry! The application has encountered an internal error.";

    public static final String CONCURRENT_UPDATE_ERROR_MESSAGE = "This action cannot be performed because the same data was being updated by another user.";

}
