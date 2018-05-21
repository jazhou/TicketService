package com.jazhou.ticketservice.common;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

/**
 * Some common utility methods
 */
public final class Utils
{
    /**
     * Returns a unique id.
     *
     * @return a UUID value
     */
    public static String getUniqueId()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * Returns the current time stamp
     *
     * @return the current time stamp
     */
    public static Timestamp getCurrentTimeStamp()
    {
        return new Timestamp(Calendar.getInstance().getTime().getTime());
    }
}
