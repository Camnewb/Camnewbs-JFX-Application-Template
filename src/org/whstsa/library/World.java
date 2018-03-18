package org.whstsa.library;

import java.util.Date;

/**
 * Class that manages the local time of the application
 */
public class World {

    private static Date currentDate = null;

    public static Date getDate() {
        return currentDate == null ? new Date() : currentDate;
    }

    public static void setDate(Date date) {
        currentDate = date;
    }

}
