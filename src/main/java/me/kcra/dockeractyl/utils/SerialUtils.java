package me.kcra.dockeractyl.utils;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class SerialUtils {
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z z");

    public Date fromTimestamp(String timestamp) {
        try {
            return DATE_FORMATTER.parse(timestamp);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public String toTimestamp(Date date) {
        return DATE_FORMATTER.format(date);
    }
}
