package com.jazhou.ticketservice.common;

/**
 * Error information returned to the user in case of exception from rest endpoint
 */
public class RestErrorInformation
{
    private String id;

    private String message;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
