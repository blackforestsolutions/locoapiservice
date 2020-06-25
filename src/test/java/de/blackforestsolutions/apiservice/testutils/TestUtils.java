package de.blackforestsolutions.apiservice.testutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestUtils {

    public static Date convertToDate(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault())
                .toInstant());
    }

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

    public static <T> T retrieveJsonPojoFromResponse(ResponseEntity<String> response, Class<T> pojo) throws JsonProcessingException {
        Objects.requireNonNull(response.getBody(), "response body is not allowed to be null");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getBody(), pojo);
    }

    public static <T> List<T> retrieveListJsonPojoFromResponse(ResponseEntity<String> response, Class<T> pojo) throws JsonProcessingException {
        Objects.requireNonNull(response.getBody(), "response body is not allowed to be null");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, pojo));
    }

    public static <T> T retrieveXmlPojoFromResponse(ResponseEntity<String> response, Class<T> pojo) throws JAXBException {
        Objects.requireNonNull(response.getBody(), "response body is not allowed to be null");
        StringReader readerResultBody = new StringReader(response.getBody());
        JAXBContext jaxbContext = JAXBContext.newInstance(pojo);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (T) unmarshaller.unmarshal(readerResultBody);
    }

    public static JourneyStatus createJourneyStatusWith(Journey journey) {
        JourneyStatus journeyStatus = new JourneyStatus();
        journeyStatus.setJourney(Optional.of(journey));
        journeyStatus.setProblem(Optional.empty());
        return journeyStatus;
    }

}
