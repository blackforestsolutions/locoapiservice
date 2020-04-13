package de.blackforestsolutions.apiservice.util;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class TimeUtil {

    /**
     * This method transforms Date types to string in a normalized Lufthansa format yyyy-mm-dd
     *
     * @param date a java date type
     * @return transformed date
     */
    public static Date transformToYyyyMMDdWith(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
            log.error("Error while parsing date to lufthansa date format", e);
        }
        return null;
    }
}
