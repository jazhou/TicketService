package com.jazhou.ticketservice.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * A base entity class that provides auditing properties.
 */
@MappedSuperclass
public abstract class AuditableEntity
{

    private Timestamp createdOn;

    private String createdBy;

    private Timestamp updatedOn;

    private String updatedBy;

    //Added optimistic locking support
    private Long version;

    @Column(name = "CREAT_TS")
    public Timestamp getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn)
    {
        this.createdOn = createdOn;
    }

    @Column(name = "CREAT_USER_ID")
    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    @Column(name = "UPDT_TS")
    public Timestamp getUpdatedOn()
    {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn)
    {
        this.updatedOn = updatedOn;
    }

    @Column(name = "UPDT_USER_ID")
    public String getUpdatedBy()
    {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    @Version
    @Column(name = "VRSN")
    public Long getVersion()
    {
        return version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }
}