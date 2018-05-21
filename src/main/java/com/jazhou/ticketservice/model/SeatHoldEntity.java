package com.jazhou.ticketservice.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Table(name = SeatHoldEntity.TABLE_NAME)
@Entity
public class SeatHoldEntity extends AuditableEntity
{
    /**
     * The table name.
     */
    public static final String TABLE_NAME = "SEAT_HOLD";

    private Long id;

    private Boolean reserved;

    private String confirmationId;

    private List<SeatEntity> seats = new ArrayList<>();

    @Id
    @Column(name = "SEAT_HOLD_ID")
    @SequenceGenerator(name = "SEAT_HOLD_SEQ", sequenceName = "SEAT_HOLD_ID_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEAT_HOLD_SEQ")
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Column(name = "RSRVD_FL")
    @Type(type = "yes_no")
    public Boolean isReserved()
    {
        return reserved;
    }

    public void setReserved(Boolean reserved)
    {
        this.reserved = reserved;
    }

    @Column(name = "CNFRT")
    public String getConfirmationId()
    {
        return confirmationId;
    }

    public void setConfirmationId(String confirmationId)
    {
        this.confirmationId = confirmationId;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = SeatEntity.SEAT_HOLD, cascade = CascadeType.ALL)
    public List<SeatEntity> getSeats()
    {
        return seats;
    }

    public void setSeats(List<SeatEntity> seats)
    {
        this.seats = seats;
    }


}
