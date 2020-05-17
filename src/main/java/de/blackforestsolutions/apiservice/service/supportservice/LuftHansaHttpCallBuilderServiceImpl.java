package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Objects;

@Service
public class LuftHansaHttpCallBuilderServiceImpl extends HttpCallBuilder implements LuftHansaHttpCallBuilderService {


    @Override
    public URL buildLuftHansaUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return buildUrlWith(apiTokenAndUrlInformation);
    }

    @Override
    public String buildLuftHansaPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "apiversion is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "pathvariable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDeparture(), "departure is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrival(), "arrival is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departureDate is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getApiVersion())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getDeparture())
                .concat("/")
                .concat(apiTokenAndUrlInformation.getArrival())
                .concat("/")
                .concat(transformDateToString(apiTokenAndUrlInformation.getDepartureDate()));
    }

    private HttpHeaders buildHttpHeadersForLufthansaWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setFormatToJsonFor(httpHeaders);
        setLufthansaAuthorisationFor(httpHeaders, apiTokenAndUrlInformation);
        return httpHeaders;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity buildHttpEntityForLuftHansa(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(buildHttpHeadersForLufthansaWith(apiTokenAndUrlInformation));
    }

    private void setLufthansaAuthorisationFor(HttpHeaders httpHeaders, ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        httpHeaders.add(HttpHeaders.AUTHORIZATION, apiTokenAndUrlInformation.getAuthorization());
    }


}
