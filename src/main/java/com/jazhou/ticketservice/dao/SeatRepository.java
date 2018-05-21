package com.jazhou.ticketservice.dao;

import java.util.List;

import com.jazhou.ticketservice.model.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long>
{
    List<SeatEntity> findAllBySeatHoldNull();
}
