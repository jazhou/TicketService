package com.jazhou.ticketservice.exception;

/**
 * Resource not found exception
 */
public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException(String message)
    {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause)
    {
        super(cause);
    }
}
