package com.jazhou.ticketservice.exception;

/**
 * The resource limit exceed exception
 */
public class ExceedLimitException extends RuntimeException
{
    public ExceedLimitException(String message)
    {
        super(message);
    }

    public ExceedLimitException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ExceedLimitException(Throwable cause)
    {
        super(cause);
    }
}
