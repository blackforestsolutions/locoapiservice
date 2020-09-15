package de.blackforestsolutions.apiservice.testutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.configuration.TimeConfiguration;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.JourneyStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
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

    public static ZonedDateTime generateDateFromLocalDateTimeAndString(String localDateTimePattern, String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(localDateTimePattern)).atZone(TimeConfiguration.GERMAN_TIME_ZONE);
    }

    public static ZonedDateTime generateDateFromLocalDatePatternAndString(String localDatePattern, String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(localDatePattern)).atStartOfDay(TimeConfiguration.GERMAN_TIME_ZONE);
    }

    public static ZonedDateTime generateDateFromLocalDatePatternAndString(DateTimeFormatter localDatePattern, String date) {
        return LocalDate.parse(date, localDatePattern).atStartOfDay(TimeConfiguration.GERMAN_TIME_ZONE);
    }

    public static ZonedDateTime generateDateFrom(String datePattern, String date, String timePattern, String time) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDate datePart = LocalDate.parse(date, dateFormatter);
        if (time.length() == 8) {
            time = StringUtils.substring(time, 2);
            datePart = datePart.plusDays(1);
        }
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timePattern);
        LocalTime timePart = LocalTime.parse(time, timeFormatter);
        return ZonedDateTime.of(datePart, timePart, TimeConfiguration.GERMAN_TIME_ZONE);
    }

    public static <T> T retrieveJsonToPojoFromResponse(ResponseEntity<String> response, Class<T> pojo) throws JsonProcessingException {
        Objects.requireNonNull(response.getBody(), "response body is not allowed to be null");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getBody(), pojo);
    }

    public static <T> T retrieveJsonToPojo(String json, Class<T> pojo) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(json, pojo);
    }

    public static <T> List<T> retrieveListJsonToPojoFromResponse(ResponseEntity<String> response, Class<T> pojo) throws JsonProcessingException {
        Objects.requireNonNull(response.getBody(), "response body is not allowed to be null");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, pojo));
    }

    public static <T> List<T> retrieveListJsonToPojoFromResponse(String json, Class<T> pojo) throws JsonProcessingException {
        Objects.requireNonNull(json, "response body is not allowed to be null");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, pojo));
    }

    public static <T> String convertObjectToJsonString(T object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("Error during parsing object to json for testing: ", e);
        }
        return null;
    }

    public static <T> T retrieveXmlToPojoFromResponse(ResponseEntity<String> response, Class<T> pojo) throws JAXBException {
        Objects.requireNonNull(response.getBody(), "response body is not allowed to be null");
        StringReader readerResultBody = new StringReader(response.getBody());
        JAXBContext jaxbContext = JAXBContext.newInstance(pojo);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        //noinspection unchecked
        return (T) unmarshaller.unmarshal(readerResultBody);
    }

    public static <T> T retrieveXmlToPojoFromResponse(String xml, Class<T> pojo) throws JAXBException {
        StringReader readerResultBody = new StringReader(xml);
        JAXBContext jaxbContext = JAXBContext.newInstance(pojo);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        //noinspection unchecked
        return (T) unmarshaller.unmarshal(readerResultBody);
    }

    public static JourneyStatus createJourneyStatusWith(Journey journey) {
        return new JourneyStatus(Optional.of(journey), Optional.empty());
    }

}
