package com.jazhou.ticketservice.exception;

/**
 * Resource already exists exception
 */
public class ResourceAlreadyExistException extends RuntimeException
{
    public ResourceAlreadyExistException(String message)
    {
        super(message);
    }

    public ResourceAlreadyExistException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ResourceAlreadyExistException(Throwable cause)
    {
        super(cause);
    }
}
