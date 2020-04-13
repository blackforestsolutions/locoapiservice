package de.blackforestsolutions.apiservice.testutils;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestUtils {

    /**
     * Reads given resource file as a string.
     *
     * @param fileName the path to the resource file
     * @return the file's contents or null if the file could not be opened
     */
    public static String getResourceFileAsString(String fileName) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        return null;
    }

    /**
     * @param now Given date.
     * @return returns formated date
     * @throws ParseException when formating is failing
     */
    public static Date formatDate(Date now) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(now);
        return new SimpleDateFormat("yyyy-MM-dd").parse(format);
    }

    public static Date generateDateFromPatternAndString(String pattern, String date) throws ParseException {
        return new SimpleDateFormat(pattern).parse(date);
    }

    public static Date buildDateFrom(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateStringFixed = date.replace("/", "-");
        return simpleDateFormat.parse(dateStringFixed);
    }

    public static Date generateDateFrom(String datePattern, String date, String timePattern, String time) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDate datePart = LocalDate.parse(date, dateFormatter);
        if (time.length() == 8) {
            time = StringUtils.substring(time, 2);
            datePart = datePart.plusDays(1);
        }
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timePattern);
        LocalTime timePart = LocalTime.parse(time, timeFormatter);
        LocalDateTime dateTime = LocalDateTime.of(datePart, timePart);
        return convertDateTimeToDate(dateTime);
    }

    public static Date generateTimeFromString(String date) throws ParseException {
        return new SimpleDateFormat("HH:mm").parse(date);
    }

    public static Duration generateDurationFromStartToDestination(Date start, Date destination) {
        return Duration.between(LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault()), LocalDateTime.ofInstant(destination.toInstant(), ZoneId.systemDefault()));
    }

    private static Date convertDateTimeToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    private static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(
                dateToConvert.toInstant(),
                ZoneId.systemDefault());
    }

    public static Duration buildDurationBetween(Date departure, Date arrival) {
        return Duration.between(
                convertToLocalDateTime(departure),
                convertToLocalDateTime(arrival)
        );
    }
}
