package com.jazhou.ticketservice.dao;

import java.util.List;

import com.jazhou.ticketservice.model.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long>
{
    /**
     * Find all available seats. If a seat is not available, its seat hold id should be populated.
     *
     * THe list is sorted by the row number in ascending order so the list is sorted from best seats to worse seats.
     *
     * @return list of seats
     */
    List<SeatEntity> findAllBySeatHoldNullOrderByRowAsc();
}
