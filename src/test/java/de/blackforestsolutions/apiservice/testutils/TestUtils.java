package de.blackforestsolutions.apiservice.testutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.apiservice.configuration.TimeConfiguration;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.TravelPoint;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
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
import java.util.Properties;
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

    public static String getPropertyFromFileAsString(String fileName, String propertyName) {
        try {
            Resource resource = new FileSystemResource(fileName);
            if (!resource.exists()) {
                resource = new ClassPathResource(fileName);
            }
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            return properties.getProperty(propertyName);
        } catch (Exception ignored) {
            return null;
        }
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

    public static ZonedDateTime generateTimeFromString(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")).atDate(LocalDate.now()).atZone(TimeConfiguration.GERMAN_TIME_ZONE);
    }

    public static <T> T retrieveJsonToPojoFromResponse(ResponseEntity<String> response, Class<T> pojo) {
        try {
            Objects.requireNonNull(response.getBody(), "response body is not allowed to be null");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), pojo);
        } catch (Exception e) {
            log.info("Exception while parsing string to pojo: ", e);
            return null;
        }
    }

    public static <T> T retrieveJsonToPojo(String json, Class<T> pojo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            return mapper.readValue(json, pojo);
        } catch (Exception e) {
            log.info("Exception while parsing string to pojo: ", e);
            return null;
        }
    }

    public static <T> List<T> retrieveListJsonToPojoFromResponse(ResponseEntity<String> response, Class<T> pojo) {
        try {
            Objects.requireNonNull(response.getBody(), "response body is not allowed to be null");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, pojo));
        } catch (Exception e) {
            log.info("Exception while parsing string to pojo: ", e);
            return null;
        }
    }

    public static <T> List<T> retrieveListJsonToPojoFromResponse(String json, Class<T> pojo) {
        try {
            Objects.requireNonNull(json, "response body is not allowed to be null");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, pojo));
        } catch (Exception e) {
            log.info("Exception while parsing string to pojo: ", e);
            return null;
        }
    }

    public static <T> String convertObjectToJsonString(T object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("Error during parsing object to json for testing: ", e);
            return null;
        }
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

    public static String toJson(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        LocoJsonMapper mapper = new LocoJsonMapper();
        try {
            return mapper.map(apiTokenAndUrlInformation);
        } catch (JsonProcessingException e) {
            log.error("Mapping ApiToken was not possible: ", e);
            return apiTokenAndUrlInformation.toString();
        }
    }

    public static String toJson(Journey journey) {
        LocoJsonMapper mapper = new LocoJsonMapper();
        try {
            return mapper.map(journey);
        } catch (JsonProcessingException e) {
            log.error("Mapping Journey was not possible: ", e);
            return journey.toString();
        }
    }

    public static String toJson(TravelPoint travelPoint) {
        LocoJsonMapper mapper = new LocoJsonMapper();
        try {
            return mapper.map(travelPoint);
        } catch (JsonProcessingException e) {
            log.error("Mapping TravelPoint was not possible: ", e);
            return travelPoint.toString();
        }
    }

}
