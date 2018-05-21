package com.jazhou.ticketservice.dao;

import java.sql.Timestamp;
import java.util.List;

import com.jazhou.ticketservice.model.SeatHoldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatHoldRepository extends JpaRepository<SeatHoldEntity, Long>
{
    /**
     * Find all expired seat holds. The seat hold can only be active for a certain number of seconds since its creation.
     * If the creation time is before the earliest active time, the seat hold is considered to be expired.
     * @param earliestActiveTime -- the earliest possible active time. If the seat hold's creation time is after the earliest
     * active time, the seat hold is not expired.
     * @return all expired seat holds
     */
    @Query("select u from SeatHoldEntity u where createdOn < :earliestActiveTime and reserved=false")
    List<SeatHoldEntity> findAllExpiredSeatHolds(@Param("earliestActiveTime") Timestamp earliestActiveTime);
}
