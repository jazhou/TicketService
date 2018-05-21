package com.jazhou.ticketservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name = SeatEntity.TABLE_NAME)
@Entity
public class SeatEntity extends AuditableEntity
{
    /**
     * The table name.
     */
    public static final String TABLE_NAME = "SEAT";

    public static final String SEAT_HOLD = "seatHold";

    private Long id;

    private Long row;

    private Long column;

    private SeatHoldEntity seatHold;

    @Id
    @Column(name = "SEAT_ID")
    @SequenceGenerator(name = "SEAT_SEQ", sequenceName = "SEAT_ID_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEAT_SEQ")
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Column(name = "ROW", nullable = false)
    public Long getRow()
    {
        return row;
    }

    public void setRow(Long row)
    {
        this.row = row;
    }

    @Column(name = "CLMN", nullable = false)
    public Long getColumn()
    {
        return column;
    }

    public void setColumn(Long column)
    {
        this.column = column;
    }

    @ManyToOne
    @JoinColumn(name = "SEAT_HOLD_ID", nullable = true)
    public SeatHoldEntity getSeatHold()
    {
        return seatHold;
    }

    public void setSeatHold(SeatHoldEntity seatHold)
    {
        this.seatHold = seatHold;
    }
}
