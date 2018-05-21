package com.jazhou.ticketservice.rest;

import static com.jazhou.ticketservice.common.Constants.AVAILABLE_SEATS_URL;
import static com.jazhou.ticketservice.common.Constants.BASE_URL;
import static com.jazhou.ticketservice.common.Constants.CREATE_SEAT_HOLD_URL;
import static com.jazhou.ticketservice.common.Constants.CREATE_SEAT_RESERVATION_URL;
import static com.jazhou.ticketservice.common.Constants.CREATE_VENUE_URL;

import com.jazhou.ticketservice.dto.SeatHold;
import com.jazhou.ticketservice.dto.SeatHoldCreateRequest;
import com.jazhou.ticketservice.dto.SeatReservationCreateRequest;
import com.jazhou.ticketservice.dto.SeatReservationCreateResponse;
import com.jazhou.ticketservice.service.TicketService;
import com.jazhou.ticketservice.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BASE_URL)
public class TicketServiceController
{
    @Autowired
    private TicketService ticketService;

    @Autowired
    private VenueService venueService;

    @RequestMapping(value = CREATE_VENUE_URL,
        method = RequestMethod.POST,
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public void createVenue(@PathVariable int numOfRows, @PathVariable int numOfColumns)
    {
        venueService.createVenue(numOfRows, numOfColumns);
    }

    @RequestMapping(value = AVAILABLE_SEATS_URL,
        method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public int getNumberOfAvailableSeats()
    {
        return ticketService.numSeatsAvailable();
    }

    @RequestMapping(value = CREATE_SEAT_HOLD_URL,
        method = RequestMethod.POST,
        consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
        MediaType.APPLICATION_JSON_VALUE})
    public SeatHold createSeatHold(@RequestBody SeatHoldCreateRequest seatHoldCreateRequest)
    {
        Assert.notNull(seatHoldCreateRequest, String.format("Request Body for %s must not be empty", CREATE_SEAT_HOLD_URL));

        return ticketService.findAndHoldSeats(seatHoldCreateRequest.getNumberOfSeats(), seatHoldCreateRequest.getEmail());
    }

    @RequestMapping(value = CREATE_SEAT_RESERVATION_URL,
        method = RequestMethod.POST,
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public SeatReservationCreateResponse createSeatReservation(@RequestBody SeatReservationCreateRequest seatReservationCreateRequest)
    {
        Assert.notNull(seatReservationCreateRequest, String.format("Request Body for %s must not be empty", CREATE_SEAT_RESERVATION_URL));

        return new SeatReservationCreateResponse(
            ticketService.reserveSeats(seatReservationCreateRequest.getSeatHoldId(), seatReservationCreateRequest.getEmail()));
    }

}
