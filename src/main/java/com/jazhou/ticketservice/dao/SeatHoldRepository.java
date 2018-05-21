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
     * Find all expired seat holds
     * @param earliestActiveTime --
     * @return all expired seat holds
     */
    @Query("select u from SeatHoldEntity u where createdOn < :earliestActiveTime and reserved=false")
    List<SeatHoldEntity> findAllExpiredSeatHolds(@Param("earliestActiveTime") Timestamp earliestActiveTime);
}
