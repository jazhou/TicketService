package com.jazhou.ticketservice.servce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.jazhou.ticketservice.dao.SeatHoldRepository;
import com.jazhou.ticketservice.dao.SeatRepository;
import com.jazhou.ticketservice.dto.SeatHold;
import com.jazhou.ticketservice.exception.ExceedLimitException;
import com.jazhou.ticketservice.exception.ResourceNotFoundException;
import com.jazhou.ticketservice.model.SeatEntity;
import com.jazhou.ticketservice.model.SeatHoldEntity;
import com.jazhou.ticketservice.service.TicketService;
import com.jazhou.ticketservice.service.TicketServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * unit tests for {@link com.jazhou.ticketservice.service.TicketService}
 */
public class TicketServiceTest
{
    private static final int NUM_SEATS = 2;
    private static final String CUSTOMER_EMAIL = "test@test.com";
    private static final int SEAT_HOLD_ID = 10;
    private static final SeatHoldEntity SEAT_HOLD_ENTITY = new SeatHoldEntity() {{
       setId((long)SEAT_HOLD_ID);
       setCreatedBy(CUSTOMER_EMAIL);
       setUpdatedBy(CUSTOMER_EMAIL);
       setReserved(false);
    }};


    @InjectMocks
    private TicketService ticketService = new TicketServiceImpl();

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private SeatHoldRepository seatHoldRepository;

    @Captor
    private ArgumentCaptor<SeatHoldEntity> seatHoldEntityArgumentCaptor;

    @Before
    public void init() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNumSeatsAvailable() {
        when(seatRepository.findAllBySeatHoldNullOrderByRowAsc()).thenReturn(Collections.emptyList());
        assertEquals(ticketService.numSeatsAvailable(), 0);

        verify(seatRepository, times(1)).findAllBySeatHoldNullOrderByRowAsc();
    }

    @Test
    public void testFindAndHoldSeatsHappyPath() {
        // Create a list of two seats
        final List<SeatEntity> seatEntityList = Arrays.asList(
            new SeatEntity() {{
                setRow(1L);
                setColumn(1L);
            }},

            new SeatEntity() {{
                setRow(1L);
                setColumn(2L);
            }}
        );
        when(seatRepository.findAllBySeatHoldNullOrderByRowAsc()).thenReturn(seatEntityList);
        SeatHold seatHold = ticketService.findAndHoldSeats(NUM_SEATS, CUSTOMER_EMAIL);
        assertNotNull(seatHold);
        verify(seatRepository, times(1)).findAllBySeatHoldNullOrderByRowAsc();

        verify(seatHoldRepository, times(1)).saveAndFlush(seatHoldEntityArgumentCaptor.capture());
        SeatHoldEntity seatHoldEntity = seatHoldEntityArgumentCaptor.getValue();
        assertEquals(CUSTOMER_EMAIL, seatHoldEntity.getCreatedBy());
        assertEquals(CUSTOMER_EMAIL, seatHoldEntity.getUpdatedBy());
        assertFalse(seatHoldEntity.isReserved());
        assertEquals(2, seatHoldEntity.getSeats().size());
    }

    @Test(expected = ExceedLimitException.class)
    public void testFindAndHoldSeatsInsufficientSeats() {
        // Create a list of one seat
        List<SeatEntity> seatEntityList = Arrays.asList(
            new SeatEntity() {{
                setRow(1L);
                setColumn(1L);
            }}
        );
        when(seatRepository.findAllBySeatHoldNullOrderByRowAsc()).thenReturn(seatEntityList);
        ticketService.findAndHoldSeats(NUM_SEATS, CUSTOMER_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAndHoldSeatsInvalidNumSeat() {
        // make number of seats a negative value
        ticketService.findAndHoldSeats(-1, CUSTOMER_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAndHoldSeatsBlankCustomerEmail() {
        // make customer email a blank value
        ticketService.findAndHoldSeats(NUM_SEATS, "");
    }

    @Test
    public void testReserveSeatsHappyPath() {
        when(seatHoldRepository.findById((long)SEAT_HOLD_ID)).thenReturn(Optional.of(SEAT_HOLD_ENTITY));

        ticketService.reserveSeats(SEAT_HOLD_ID, CUSTOMER_EMAIL);

        verify(seatHoldRepository, times(1)).findById((long)SEAT_HOLD_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testReserveSeatsSeatHoldNotFound() {
        when(seatHoldRepository.findById((long)SEAT_HOLD_ID)).thenReturn(Optional.empty());

        ticketService.reserveSeats(SEAT_HOLD_ID, CUSTOMER_EMAIL);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testReserveSeatsSeatHoldCustomerEmailNotMatch() {
        when(seatHoldRepository.findById((long)SEAT_HOLD_ID)).thenReturn(Optional.empty());

        ticketService.reserveSeats(SEAT_HOLD_ID, "invalid email");
    }

}
