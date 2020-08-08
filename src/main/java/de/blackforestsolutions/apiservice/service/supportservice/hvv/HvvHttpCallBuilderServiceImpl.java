package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.setFormatToJsonFor;
import static de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpBodyService.*;
import static de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilder.*;

@Service
public class HvvHttpCallBuilderServiceImpl implements HvvHttpCallBuilderService {

    @Override
    public HttpEntity<String> buildJourneyHttpEntityForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, HvvStation start, HvvStation destination) {
        String body = buildJourneyHttpBodyForHvv(apiTokenAndUrlInformation, start, destination);
        return new HttpEntity<>(body, buildHttpHeadersForHvvWith(apiTokenAndUrlInformation, body));
    }

    @Override
    public String buildJourneyPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "path variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getJourneyPathVariable(), "journey path variable is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat(apiTokenAndUrlInformation.getJourneyPathVariable());
    }

    @Override
    public HttpEntity<String> buildTravelPointHttpEntityForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        String body = buildTravelPointHttpBodyForHvv(apiTokenAndUrlInformation, station);
        return new HttpEntity<>(body, buildHttpHeadersForHvvWith(apiTokenAndUrlInformation, body));
    }

    @Override
    public String buildTravelPointPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "path variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getTravelPointPathVariable(), "travelpoint path variable is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat(apiTokenAndUrlInformation.getTravelPointPathVariable());
    }

    private HttpHeaders buildHttpHeadersForHvvWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String jsonBody) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setFormatToJsonFor(httpHeaders);
        setBaseHttpHeaderFor(httpHeaders, apiTokenAndUrlInformation);
        setHvvAuthentificationSignatureFor(httpHeaders, apiTokenAndUrlInformation, jsonBody.getBytes(StandardCharsets.UTF_8));
        return httpHeaders;
    }
}
