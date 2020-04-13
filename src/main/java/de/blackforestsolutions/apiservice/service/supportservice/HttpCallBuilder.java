package de.blackforestsolutions.apiservice.service.supportservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

@Slf4j
public class HttpCallBuilder {

    public static URL buildUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        try {
            if (apiTokenAndUrlInformation.getPort() == 0 || apiTokenAndUrlInformation.getPort() == -1) {
                return new URL(apiTokenAndUrlInformation.getProtocol(), apiTokenAndUrlInformation.getHost(), apiTokenAndUrlInformation.getPath());
            }
            return new URL(apiTokenAndUrlInformation.getProtocol(), apiTokenAndUrlInformation.getHost(), apiTokenAndUrlInformation.getPort(), apiTokenAndUrlInformation.getPath());
        } catch (MalformedURLException e) {
            log.error("Url could not be build because of wrong part in url");
            throw new UncheckedIOException(e);
        }
    }

    public static void setFormatToJsonFor(HttpHeaders httpHeaders) {
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    public static void setFormatToXmlFor(HttpHeaders httpHeaders) {
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
        httpHeaders.setContentType(MediaType.APPLICATION_XML);
    }

    public static <T> String convertHttpModelToJson(T model) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            log.error("error while parsing http object to json, was replace by empty string: ", e);
            return "";
        }
    }

    /**
     * This method transforms Date types to string in a normalized Lufthansa format yyyy-mm-dd
     *
     * @param date a java date type
     * @return transformed date string
     */
    public static String transformDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    @SuppressWarnings("rawtypes")
    public static HttpEntity buildEmptyHttpEntity() {
        return new HttpEntity<>(new HttpHeaders());
    }
}
