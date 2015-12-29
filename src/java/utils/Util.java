package utils;

import java.util.Calendar;

public class Util {

    public static java.util.Date sqlToJDate(java.sql.Date sqlDate) {

        return new java.util.Date(sqlDate.getTime());
    }

    public static java.sql.Date getCurrentSQLDate() {

        return new java.sql.Date(Calendar.getInstance().getTime().getTime());
    }

    public static String marked(Object string) {

        return "\"" + string.toString() + "\"";
    }

    public static long toLong(String string) {

        long l = Long.MIN_VALUE;

        try {
            l = Long.parseLong(string.trim());
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
        }

        return l;
    }
}
