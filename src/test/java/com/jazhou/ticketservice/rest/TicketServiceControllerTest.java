package com.jazhou.ticketservice.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jazhou.ticketservice.common.Constants;
import com.jazhou.ticketservice.dto.SeatHold;
import com.jazhou.ticketservice.dto.SeatReservationCreateResponse;
import com.jazhou.ticketservice.exception.ResourceNotFoundException;
import com.jazhou.ticketservice.service.TicketService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Unit tests for {@link TicketServiceController}
 */
public class TicketServiceControllerTest
{
    private static final int NUM_SEATS_AVAILABLE = 40;
    private static final int NUM_SEATS_HOLD = 10;
    private static final String TEST_EMAIL = "test@test.com";
    private static final long TEST_SEAT_HOLD_ID = 2L;
    private static final SeatHold TEST_SEAT_HOLD = new SeatHold(TEST_SEAT_HOLD_ID);
    private static final String CREATE_SEAT_HOLD_CONTENT_JSON = "{\"numberOfSeats\":2,\"email\":\"" + TEST_EMAIL + "\"}";
    private static final String CREATE_RESERVATION_CONTENT_JSON = "{\"seatHoldId\":2,\"email\":\"" + TEST_EMAIL + "\"}";
    private static final String CONFIRMATION_ID = "test_confirmation";
    private static final SeatReservationCreateResponse SEAT_RESERVATION_CREATE_RESPONSE =
        new SeatReservationCreateResponse(CONFIRMATION_ID);

    @InjectMocks
    private TicketServiceController ticketServiceController;
    @Mock
    private TicketService ticketService;

    private MockMvc mockMvc;

    @Before
    public void init() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ticketServiceController).setControllerAdvice(new RestControllerErrorHandler())
            .build();
    }

    @Test
    public void testGetNumberOfAvailableSeatsHappyPath() throws Exception
    {
        when(ticketService.numSeatsAvailable()).thenReturn(NUM_SEATS_AVAILABLE);
        mockMvc.perform(
            get(Constants.BASE_URL + Constants.AVAILABLE_SEATS_URL)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(Integer.toString(NUM_SEATS_AVAILABLE)));
    }

    @Test
    public void testGetNumberOfAvailableSeatsInternalException() throws Exception
    {
        //throw an error
        when(ticketService.numSeatsAvailable()).thenThrow(new RuntimeException("test"));
        mockMvc.perform(
            get(Constants.BASE_URL + Constants.AVAILABLE_SEATS_URL)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())  //verify that the satus code is 500
            .andExpect(jsonPath("message").value(Constants.INTERNAL_SERVER_ERROR_MESSAGE)); //error message
    }

    @Test
    public void testCreateSeatHoldHappyPath() throws Exception
    {
        when(ticketService.findAndHoldSeats(NUM_SEATS_HOLD, TEST_EMAIL)).thenReturn(TEST_SEAT_HOLD);

        mockMvc.perform(
            post(Constants.BASE_URL + Constants.CREATE_SEAT_HOLD_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_SEAT_HOLD_CONTENT_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void testCreateSeatReservationHappyPath() throws Exception
    {
        when(ticketService.reserveSeats((int)TEST_SEAT_HOLD_ID, TEST_EMAIL)).thenReturn(CONFIRMATION_ID);

        mockMvc.perform(
            post(Constants.BASE_URL + Constants.CREATE_SEAT_RESERVATION_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_RESERVATION_CONTENT_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("confirmationId").value(CONFIRMATION_ID));
    }

    @Test
    public void testCreateSeatReservationNotFoundException() throws Exception
    {
        when(ticketService.reserveSeats((int)TEST_SEAT_HOLD_ID, TEST_EMAIL))
            .thenThrow(new ResourceNotFoundException("test"));

        mockMvc.perform(
            post(Constants.BASE_URL + Constants.CREATE_SEAT_RESERVATION_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_RESERVATION_CONTENT_JSON))
            .andExpect(status().isNotFound());  //404
    }

    //TODO add more negative tests to cover all exception types
}
