package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class BBCHttpCallBuilderServiceImpl implements BBCHttpCallBuilderService {

    @Override
    public URL buildUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        try {
            if (apiTokenAndUrlInformation.getPort() == 0 || apiTokenAndUrlInformation.getPort() == -1) {
                return new URL(apiTokenAndUrlInformation.getProtocol(), apiTokenAndUrlInformation.getHost(), apiTokenAndUrlInformation.getPath());
            }
            return new URL(apiTokenAndUrlInformation.getProtocol(), apiTokenAndUrlInformation.getHost(), apiTokenAndUrlInformation.getPort(), apiTokenAndUrlInformation.getPath());
        } catch (MalformedURLException e) {
            log.error("URL could not be build because it was not valid: {}", apiTokenAndUrlInformation.getProtocol() + apiTokenAndUrlInformation.getHost() + apiTokenAndUrlInformation.getPort() + apiTokenAndUrlInformation.getPath());
            throw new UncheckedIOException(e);
        }
    }


    @Override
    public String bbcBuildPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "apiversion is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathvariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDeparture(), "departure is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrival(), "arrival is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departureDate is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getApiVersion())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("fn=")
                .concat(apiTokenAndUrlInformation.getDeparture())
                .concat("&")
                .concat("tn=")
                .concat(apiTokenAndUrlInformation.getArrival())
                .concat("&")
                .concat("db=")
                .concat(transformDateToString(apiTokenAndUrlInformation.getDepartureDate()));
    }

    @Override
    public HttpHeaders buildHttpHeadersForBbcWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders httpHeaders = buildBasicHttpheader();
        setFormatToJsonFor(httpHeaders);
        setBbcAuthorisationFor(httpHeaders, apiTokenAndUrlInformation);
        return httpHeaders;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity buildHttpEntityForBbc(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(buildHttpHeadersForBbcWith(apiTokenAndUrlInformation));
    }

    private HttpHeaders buildBasicHttpheader() {
        return new HttpHeaders();
    }

    private void setFormatToJsonFor(HttpHeaders httpHeaders) {
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    private void setBbcAuthorisationFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        httpHeaders.add(apiTokenAndUrlInformation.getAuthorizationKey(), apiTokenAndUrlInformation.getAuthorization());
    }

    private String transformDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

}
